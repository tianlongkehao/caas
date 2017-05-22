package com.bonc.epm.paas.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;
import com.ceph.fs.CephMount;
import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.exceptions.RadosException;
import com.ceph.rbd.Rbd;
import com.ceph.rbd.RbdException;

/**
 * CephController
 *
 * @author YuanPeng
 * @version 2016年9月5日
 * @see CephController
 * @since
 */
@SuppressWarnings("restriction")
@Controller
public class CephController {
	/**
	 * 输出日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CephController.class);
	/**
	 * 簇
	 */
	//private Rados cluster;

	//private IoCTX ioctx;
	
	private static File f = new File("/etc/ceph/ceph.conf");
	/**
	 * 连接url
	 */
	// @Value("${ceph.ssh.url}")
	// private String url;

	/**
	 * 用户名
	 */
	// @Value("${ceph.ssh.username}")
	// private String username;

	/**
	 * 密码
	 */
	// @Value("${ceph.ssh.password}")
	// private String password;

	/**
	 * 挂载指令
	 */
	@Value("${ceph.ssh.mountexec}")
	private String mountexec;

	/**
	 * 挂载点
	 */
	@Value("${ceph.ssh.mountpoint}")
	private String mountpoint;

	/**
	 * 执行前挂载需要cd到的目录
	 */
	@Value("${ceph.ssh.cephdir}")
	private String cephDir;

	@Value("${ceph.key}")
	private String CEPH_KEY;
	/**
	 * CephMount
	 */
	private CephMount cephMount;

	/**
	 * 模式
	 */
	private int mode = 511;

	/**
	 * connectCephFS
	 */
	public void connectCephFS() throws Exception {
		LOGGER.info("进入方法：connectCephFS");
		cephMount = new CephMount("admin");
		cephMount.conf_read_file("/etc/ceph/ceph.conf");
		cephMount.mount("/");
		cephMount.chmod("/", mode);
		LOGGER.info("打印根目录下的所有目录");
		String[] listdir = cephMount.listdir("/");
		for (String strDir : listdir) {
			LOGGER.info("dir:" + strDir);
		}
	}

	/**
	 * createNamespaceCephFS 文件属主 文件属组 其他所有用户(x:执行；w：写；r：读) O_CREAT x O_TRUNC w
	 * O_RDWR w O_RDONLY x O_APPEND r O_WRONLY x O_EXCL r
	 *
	 * @param namespace
	 * @throws FileNotFoundException
	 */
	public void createNamespaceCephFS(String namespace) throws Exception {
		LOGGER.info("进入方法：createNamespaceCephFS");
		String[] listdir = cephMount.listdir("/");
		if (StringUtils.isBlank(namespace)) {
			return;
		}
		int flag = 0;
		for (String strDir : listdir) {
			if (strDir.equals(namespace)) {
				flag = 1;
				break;
			}
		}
		if (0 == flag) {
			cephMount.mkdir("/" + namespace, mode);
		}
		LOGGER.info("打印" + namespace + "下的所有目录");
		String[] listdir2 = cephMount.listdir("/" + namespace);
		for (String strDir : listdir2) {
			LOGGER.info("dir:" + strDir);
		}
	}

	/**
	 * deleteStorageCephFS
	 */
	public void deleteNamespaceCephFS(String namespace) {
		try {
			LOGGER.info("进入方法：deleteNamespaceCephFS");
			LOGGER.info("删除前,打印根目录下的所有目录");
			String[] listDirBef = cephMount.listdir("/");
			for (String strDir : listDirBef) {
				LOGGER.info("dir:" + strDir);
			}

			LOGGER.info("删除前,打印" + namespace + "下的所有目录");
			String[] liststorageDir = cephMount.listdir("/" + namespace);
			for (String strDir : liststorageDir) {
				LOGGER.info("dir:" + strDir);
			}

			// 删除挂载卷目录
			cephMount.rmdir(namespace);

			LOGGER.info("删除后,打印根目录下的所有目录");
			String[] listDirAft = cephMount.listdir("/");
			for (String strDir : listDirAft) {
				LOGGER.info("dir:" + strDir);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建ceph文件系统卷组
	 *
	 * @param storageName
	 * @param isVolReadOnly
	 * @see
	 */
	public boolean createStorageCephFS(String storageName, boolean isVolReadOnly) {
		LOGGER.info("进入方法：createStorageCephFS");
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		int readOrWrite = mode;
		if (isVolReadOnly) {
			readOrWrite = 292;
		}
		try {
			// 指定当前工作目录
			cephMount.chdir("/" + namespace);
			// 创建挂载卷目录
			cephMount.mkdir(storageName, readOrWrite);
			cephMount.chmod("/" + namespace, mode);

			LOGGER.info("打印" + namespace + "下的所有目录");
			String[] listdir2 = cephMount.listdir("/" + namespace);
			for (String strDir : listdir2) {
				LOGGER.info("dir:" + strDir);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除ceph文件系统的卷组
	 *
	 * @param storageName
	 * @see
	 */
	public void deleteStorageCephFS(String storageName) {
		try {
			LOGGER.info("进入方法：deleteStorageCephFS");
			// 获取NAMESPACE
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
			LOGGER.info("删除前,打印" + namespace + "下的所有目录");
			String[] listDirBef = cephMount.listdir("/" + namespace);
			for (String strDir : listDirBef) {
				LOGGER.info("dir:" + strDir);
			}

			// 指定当前工作目录&&删除挂载卷目录
			cephMount.chdir("/" + namespace);
			cephMount.rmdir(storageName);

			LOGGER.info("删除前,打印" + namespace + "下的所有目录");
			String[] listDirAft = cephMount.listdir("/" + namespace);
			for (String strDir : listDirAft) {
				LOGGER.info("dir:" + strDir);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 格式化卷组
	 *
	 * @param storageName
	 * @param isVolReadOnly
	 * @throws FileNotFoundException
	 * @see
	 */
	public void formatStorageCephFS(String root, String storageName) throws FileNotFoundException {
		LOGGER.info("进入方法：deleteStorageCephFS");
		// 获取NAMESPACE
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		StringBuffer path = new StringBuffer(root);
		path.append(namespace).append("/").append(storageName);
		FileUtils.delAllFile(path.toString());
	}

	/**
	 * 连接Ceph
	 *
	 * @param radosName
	 * @see
	 */
	public void conCeph(String radosName) {
		try {
			LOGGER.info("radosName:" + radosName);
			Rados cluster = new Rados(radosName); // Created cluster handle

			File f = new File("/etc/ceph/ceph.conf");
			cluster.confReadFile(f); // Read the configuration file.

			cluster.connect(); // Connected to the cluster.
		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

/*	*//**
	 *
	 * Description: 根据命名空间创建POOL
	 *
	 * @see
	 *//*
	public void createPoolByNSpace() {
		try {
			LOGGER.info("进入方法：createPoolByNameSpace");
			// 获取NAMESPACE
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
            
			// 获取所有pool
			String[] pools = cluster.poolList();
			boolean poolExist = false;
			for (String pool : pools) {
				if (namespace.equals(pool)) {
					poolExist = true;
				}
			}
			// 如果pool不存在，创建pool
			if (!poolExist) {
				// 创建pool
				cluster.poolCreate(namespace); // 创建pool:namespace
			}
		} catch (RadosException e) {
			e.printStackTrace();
		}
	}*/

/*	*//**
	 * 创建image
	 *
	 * @param conName
	 *            conName
	 *//*
	public void createCephImage(String conName) {
		try {
			LOGGER.info("进入方法：createCephImage,conName:-" + conName);
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace(); // 获取NAMESPACE

			IoCTX ioctx = cluster.ioCtxCreate(namespace); // 创建ioCtx

			Rbd rbd = new Rbd(ioctx); // RBD

			// 获取所有images
			String[] images = rbd.list();
			boolean imageExist = false;
			for (String image : images) {
				if (conName.equals(image)) {
					imageExist = true;
				}
			}
			// 如果image不存在，创建image
			if (!imageExist) {
				// 创建image并指定空间大小以及feature和format
				long size_1G = 1024 * 1024 * 1024;
				long feature_layering = 8 * 8;
				int order = 22;
				rbd.create(conName, size_1G, feature_layering, order);
			}

			// 打开image
			// RbdImage rbdImage = rbd.open(conName);
			// 关闭image
			// rbd.close(rbdImage);

			// 清除ioCtx
			cluster.ioCtxDestroy(ioctx);
		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (RbdException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}*/

/*	*//**
	 * 关闭连结
	 *//*
	public void clusterShutDown() {
		try {
			// 关闭连结
			cluster.shutDown();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

	}*/

	/**
	 * 创建Ceph块存储 rbd大小的单位是B
	 */
	public void createCephRbd(String imgname) {
		Rados cluster = new Rados("admin");
		try {
			cluster.confReadFile(f);
			cluster.connect();
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();

			// 获取所有pool
			String[] pools = cluster.poolList();
			boolean poolExist = false;
			for (String pool : pools) {
				if (namespace.equals(pool)) {
					poolExist = true;
					break;
				}
			} 
			
			if (!poolExist) {
				cluster.poolCreate(namespace); // 创建pool:namespace
			}
			
			IoCTX ioctx = cluster.ioCtxCreate("longlong"); // 创建ioCtx
			try {
				Rbd rbd = new Rbd(ioctx); // RBD
				rbd.create(imgname, 1024);
			} catch (RbdException e) {
				//LOGGER.error(e.getMessage());
				e.printStackTrace();
			} finally {
				cluster.ioCtxDestroy(ioctx);
				//ioctx = null;
			}
			
		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			cluster.shutDown();
			//cluster = null;
		}
	}

	@RequestMapping(value = { "service/ceph/checkrbd" }, method = RequestMethod.GET)
	@ResponseBody
	public String checkrbd(String imgname) {
		Map<String, Integer> map = new HashMap<>();
		Rados	cluster = new Rados("admin");
		try {
			cluster.confReadFile(f);
			cluster.connect();

			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();

			// 获取所有pool
			String[] pools = cluster.poolList();
			boolean poolExist = false;
			for (String pool : pools) {
				if (namespace.equals(pool)) {
					poolExist = true;
				}
			}
			// 如果pool不存在，创建pool
			if (!poolExist) {
				// 创建pool
				cluster.poolCreate(namespace); // 创建pool:namespace
			}

			IoCTX ioctx = cluster.ioCtxCreate(namespace); // 创建ioCtx
			Rbd rbd = new Rbd(ioctx); // RBD

			// 获取所有images
			String[] images = rbd.list();
			boolean imageExist = false;
			for (String image : images) {
				if (imgname.equals(image)) {
					imageExist = true;
				}
			}
			if (imageExist) {
				map.put("exist", 1);
			} else {
				map.put("exist", 0);
			}

			cluster.ioCtxDestroy(ioctx);
			map.put("status", 200);
			return JSON.toJSONString(map);
		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			map.put("status", 500);
			return JSON.toJSONString(map);
		} catch (RbdException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			map.put("status", 500);
			return JSON.toJSONString(map);
		} finally {
			cluster.shutDown();
		}

	}

	/**
	 *
	 * 文件转byte[]
	 *
	 * @param filePath
	 *            文件路径
	 * @return byte[]
	 * @see
	 */
	public byte[] file2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * byte[]转文件
	 *
	 * @param buf
	 *            比特
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 */
	public void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public String getMountexec() {
		return mountexec;
	}

	public String getMountpoint() {
		return mountpoint;
	}

	public String getCephDir() {
		return cephDir;
	}

}
