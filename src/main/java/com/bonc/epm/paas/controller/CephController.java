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

	private static final Logger log = LoggerFactory.getLogger(CephController.class);

	// cluster
	private Rados cluster;

	// cephMount
	private CephMount cephMount;

	/**
	 * connectCephFS
	 */
	public void connectCephFS() {

		try {
			System.out.println("进入方法：connectCephFS");

			cephMount = new CephMount("admin");
			System.out.println("new CephMount(admin)");

			cephMount.conf_read_file("/etc/ceph/ceph.conf");
			System.out.println("Read the configuration file.");

			cephMount.mount("/");

			System.out.println("打印根目录下的所有目录");
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
			System.out.println("进入方法：createNamespaceCephFS");
			
			cephMount.mkdir("/" + namespace, CephMount.O_RDWR);
			System.out.println("创建目录：" + "/" + namespace);
			
			System.out.println("打印"+namespace+"下的所有目录");
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
	public void createStorageCephFS(String storageName) {

		try {
			System.out.println("进入方法：createStorageCephFS");
			
			// 获取NAMESPACE
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();

			//指定当前工作目录
			cephMount.chdir("/" + namespace);
			
			cephMount.mkdir(storageName, CephMount.O_RDWR);
			System.out.println("创建目录：" + storageName);
			
			System.out.println("打印根目录下的所有目录");
			String[] listdir1 = cephMount.listdir("/");
			for (String strDir : listdir1) {
				System.out.println("dir:" + strDir);
			}
			
			System.out.println("打印"+namespace+"下的所有目录");
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
	 * 连结ceph服务
	 */
	public void conCeph(String radosName) {

		try {
			System.out.println("radosName:" + radosName);
			cluster = new Rados(radosName);
			System.out.println("Created cluster handle:" + cluster);

			File f = new File("/etc/ceph/ceph.conf");
			cluster.confReadFile(f);
			System.out.println("Read the configuration file.");

			cluster.connect();
			System.out.println("Connected to the cluster.");
		} catch (RadosException e) {
			log.debug(e.getMessage());
			System.out.println(e.getMessage() + ": " + e.getReturnValue());
		}
	}

	/**
	 * 创建pool
	 */
	public void createPoolByNameSpace() {

		try {
			System.out.println("进入方法：createPoolByNameSpace");

			// 获取NAMESPACE
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
			System.out.println("namespace:" + namespace);

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
				cluster.poolCreate(namespace);
				System.out.println("创建pool:" + namespace);
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
			System.out.println("进入方法：createCephImage");
			System.out.println("conName:" + conName);

			// 获取NAMESPACE
			String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();

			// 创建ioCtx
			IoCTX ioctx = cluster.ioCtxCreate(namespace);
			System.out.println("创建ioctx成功");

			// 創建RBD
			Rbd rbd = new Rbd(ioctx);

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
				// 創建image并指定空间大小以及feature和format
				long size_1G = 1024 * 1024 * 1024;
				// TODO feature的值，还没弄明白
				long feature_layering = 8 * 8;
				int order = 22;
				System.out.println("feature_layering:" + feature_layering);
				rbd.create(conName, size_1G, feature_layering, order);

				System.out.println("开始创建ceph-image:" + conName);
				System.out.println("创建ceph-image:" + conName + "成功");
			}

			// 打开image
			// RbdImage rbdImage = rbd.open(conName);
			// 关闭image
			// rbd.close(rbdImage);

			// 清除ioCtx
			cluster.ioCtxDestroy(ioctx);
		} catch (RadosException e) {
			log.debug(e.getMessage());
			System.out.println(e.getMessage() + ": " + e.getReturnValue());
		} catch (RbdException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连结
	 */
	public void clusterShutDown() {
		// 关闭连结
		cluster.shutDown();
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
			e.printStackTrace();
		} catch (IOException e) {
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
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
