package com.bonc.epm.paas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.ceph.rados.Rados;
import com.ceph.rbd.Rbd;
import com.ceph.rbd.RbdException;

import com.ceph.rados.exceptions.RadosException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bonc.epm.paas.util.CurrentUserUtils;
import com.ceph.rados.IoCTX;
import com.ceph.fs.CephMount;

@Controller
public class CephController {

	private static final Logger logger = LoggerFactory.getLogger(CephController.class);

	// cluster
	private Rados cluster;

	// cephMount
	private CephMount cephMount;

	/**
	 * connectCephFS
	 */
	public void connectCephFS() {
		try {
			logger.info("进入方法：connectCephFS");
			cephMount = new CephMount("admin");
			cephMount.conf_read_file("/etc/ceph/ceph.conf");
			cephMount.mount("/");

			logger.info("打印根目录下的所有目录");
			String[] listdir = cephMount.listdir("/");
			for (String strDir : listdir) {
				System.out.println("dir:" + strDir);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * createNamespaceCephFS
	 */
	public void createNamespaceCephFS(String namespace) {
		try {
			logger.info("进入方法：createNamespaceCephFS");
			cephMount.mkdir("/" + namespace, CephMount.O_RDWR);

			logger.info("打印" + namespace + "下的所有目录");
			String[] listdir = cephMount.listdir("/" + namespace);
			for (String strDir : listdir) {
				System.out.println("dir:" + strDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * createStorageCephFS
	 */
	public void createStorageCephFS(String storageName, boolean isVolReadOnly) {
		try {
			logger.info("进入方法：createStorageCephFS");
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
			int readOrWrite = CephMount.O_RDWR;
			if (isVolReadOnly){
				readOrWrite = CephMount.O_RDONLY;
			}
			// 指定当前工作目录
			cephMount.chdir("/" + namespace);
			// 创建挂载卷目录
			cephMount.mkdir(storageName, readOrWrite);

			logger.info("打印" + namespace + "下的所有目录");
			String[] listdir2 = cephMount.listdir("/" + namespace);
			for (String strDir : listdir2) {
				System.out.println("dir:" + strDir);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * deleteStorageCephFS
	 */
	public void deleteStorageCephFS(String storageName) {
		try {
			logger.info("进入方法：deleteStorageCephFS");
			// 获取NAMESPACE
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
			logger.info("删除前,打印" + namespace + "下的所有目录");
			String[] listDirBef = cephMount.listdir("/" + namespace);
			for (String strDir : listDirBef) {
				System.out.println("dir:" + strDir);
			}

			// 指定当前工作目录&&删除挂载卷目录
			cephMount.chdir("/" + namespace);
			cephMount.rmdir(storageName);

			logger.info("删除前,打印" + namespace + "下的所有目录");
			String[] listDirAft = cephMount.listdir("/" + namespace);
			for (String strDir : listDirAft) {
				System.out.println("dir:" + strDir);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 连结ceph服务
	 */
	public void conCeph(String radosName) {
		try {
		   logger.info("radosName:" + radosName);
			cluster = new Rados(radosName); // Created cluster handle

			File f = new File("/etc/ceph/ceph.conf");
			cluster.confReadFile(f); // Read the configuration file.

			cluster.connect(); // Connected to the cluster.
		} catch (RadosException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 创建pool
	 */
	public void createPoolByNameSpace() {
		try {
		    logger.info("进入方法：createPoolByNameSpace");
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
	}

	/**
	 * 创建image
	 * 
	 * @param conName
	 */
	public void createCephImage(String conName) {
		try {
			logger.info("进入方法：createCephImage,conName:-"+conName);
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
				// TODO feature的值，还没弄明白
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
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (RbdException e) {
		   logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连结
	 */
	public void clusterShutDown() {
	    try {
	          // 关闭连结
	        cluster.shutDown();
         }
       catch (Exception e) {
           logger.error(e.getMessage());
           e.printStackTrace();
        }

	}

	/**
	 * 文件转byte[]
	 * 
	 * @param filePath
	 * @return byte[]
	 */
	public byte[] File2byte(String filePath) {
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
		    logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
		    logger.error(e.getMessage());
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * byte[]转文件
	 * 
	 * @param buf
	 * @param filePath
	 * @param fileName
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
		    logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				    logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				    logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
