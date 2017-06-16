package com.bonc.epm.paas.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.SnapListener;
import com.bonc.epm.paas.constant.CommConstant;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.CephRbdInfoDao;
import com.bonc.epm.paas.dao.CephSnapDao;
import com.bonc.epm.paas.dao.CommonOperationLogDao;
import com.bonc.epm.paas.dao.ServiceRbdDao;
import com.bonc.epm.paas.dao.SnapStrategyDao;
import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.CommonOprationLogUtils;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.ceph.CephRbdInfo;
import com.bonc.epm.paas.entity.ceph.CephSnap;
import com.bonc.epm.paas.entity.ceph.ServiceCephRbd;
import com.bonc.epm.paas.entity.ceph.SnapStrategy;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;
import com.ceph.fs.CephMount;
import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.exceptions.RadosException;
import com.ceph.rbd.Rbd;
import com.ceph.rbd.RbdException;
import com.ceph.rbd.RbdImage;
import com.ceph.rbd.jna.RbdSnapInfo;

import io.netty.handler.codec.http.HttpContentEncoder.Result;

/**
 * CephController
 *
 * @author YuanPeng
 * @version 2016年9月5日
 * @see CephController
 * @since
 */
@Controller
public class CephController {
	/**
	 * 输出日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CephController.class);
	/**
	 * 簇
	 */
	private Rados cluster;

	/**
	 * 用户名
	 */
	@Value("${ceph.name}")
	private String CEPH_NAME;

	/**
	 * 挂载指令
	 */
	@Value("${ceph.ssh.mountexec}")
	private String MOUNT_EXEC;

	/**
	 * 挂载点
	 */
	@Value("${ceph.ssh.mountpoint}")
	private String MOUNT_POINT;

	/**
	 * 执行前挂载需要cd到的目录
	 */
	@Value("${ceph.ssh.cephdir}")
	private String CEPH_DIR;

	@Value("${ceph.conf}")
	private String CEPH_CONF;

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
	 * 通用操作记录信息层接口
	 */
	@Autowired
	private CommonOperationLogDao commonOperationLogDao;

	/**
	 * ceph块存储信息持久化接口
	 */
	@Autowired
	private CephRbdInfoDao cephRbdInfoDao;

	/**
	 * 快照信息持久化接口
	 */
	@Autowired
	private CephSnapDao cephSnapDao;

	/**
	 * 快照与服务关联持久化接口
	 */
	@Autowired
	private ServiceRbdDao serviceRbdDao;

	@Autowired
	private SnapStrategyDao snapStrategyDao;

	/*
	 * @Autowired private ServiceDao serviceDao;
	 */

	/**
	 * connectCephFS
	 */
	public void connectCephFS() throws Exception {
		LOGGER.info("进入方法：connectCephFS");
		try {
			cephMount = new CephMount(CEPH_NAME);
		} catch (Exception e) {
			throw e;
		}
		cephMount.conf_read_file(CEPH_DIR + CEPH_CONF);
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
	 * 创建pool
	 */
	public void createPool() throws RadosException {
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

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
				cluster.poolCreate(namespace);
			}

		} catch (RadosException e) {
			throw e;
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}

	}

	/**
	 * 删除pool，清空数据库中的相关记录
	 *
	 * @param namespace
	 * @throws RadosException
	 */
	public void clearPool(String namespace) throws RadosException {
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			// 获取所有pool
			String[] pools = cluster.poolList();
			boolean poolExist = false;
			for (String pool : pools) {
				if (namespace.equals(pool)) {
					poolExist = true;
					break;
				}
			}

			if (poolExist) {
				// 停止快照策略的执行
				List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByPool(namespace);
				if (!CollectionUtils.isEmpty(cephRbdInfos)) {
					for (CephRbdInfo cephRbdInfo : cephRbdInfos) {
						if (cephRbdInfo.isStrategyexcuting()) {
							SnapListener.removeTimer(cephRbdInfo);
						}
					}
				}
				// 删除pool
				cluster.poolDelete(namespace);
				// 删除数据库记录
				cephRbdInfoDao.deleteByPool(namespace);
				cephSnapDao.deleteByPool(namespace);
				snapStrategyDao.deleteByNamespace(namespace);
			}

		} catch (RadosException e) {
			throw e;
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 清空cephrbd
	 *
	 * @return
	 */
	@RequestMapping(value = { "ceph/clearcephrbd" }, method = RequestMethod.GET)
	@ResponseBody
	public String clearRbd(String imgName) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		map.put("status", "200");
		String msg = "";
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			IoCTX ioctx = cluster.ioCtxCreate(namespace);
			try {
				Rbd rbd = new Rbd(ioctx); // RBD
				RbdImage rbdImage = rbd.open(imgName);
				long size = rbdImage.stat().size;
				rbdImage.getPointer().clear(size);
				rbd.close(rbdImage);
				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "指定的镜像不存在！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}

		} catch (RadosException e) {
			msg = "ceph异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 创建Ceph块存储 rbd大小的单位是B
	 */
	@RequestMapping(value = { "ceph/createcephrbd" }, method = RequestMethod.GET)
	@ResponseBody
	public String createCephRbd(String imgname, String disksize, String diskdetail) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		String msg = "";
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

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
				cluster.poolCreate(namespace);
			}

			IoCTX ioctx = cluster.ioCtxCreate(namespace);
			long temp = Long.parseLong(disksize);
			try {
				Rbd rbd = new Rbd(ioctx); // RBD
				long size = temp * 1024l * 1024l * 1024l;
				rbd.create(imgname, size, 1l);
				CephRbdInfo cephRbdInfo = saveCephRbdInfo(imgname, temp, diskdetail);

				// 记录日志
				String extraInfo = "新增ceph块存储 " + JSON.toJSONString(cephRbdInfo);
				LOGGER.info(extraInfo);
				CommonOperationLog log = CommonOprationLogUtils.getOprationLog(imgname, extraInfo,
						CommConstant.CEPH_RBD, CommConstant.OPERATION_TYPE_CREATED);
				commonOperationLogDao.save(log);

				map.put("status", "200");
				return JSON.toJSONString(map);
			} catch (RbdException e) {
				LOGGER.error(e.getMessage());
				msg = "镜像" + imgname + "创建失败!";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}

		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			msg = "pool" + namespace + "创建失败!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 创建快照
	 *
	 * @param imgname
	 * @param snapname
	 * @param snapdetail
	 * @return
	 */
	@RequestMapping(value = { "ceph/createsnap" }, method = RequestMethod.GET)
	@ResponseBody
	public String createSnap(String imgname, String snapname, String snapdetail) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		String msg = "";
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

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
				cluster.poolCreate(namespace);
			}

			IoCTX ioctx = cluster.ioCtxCreate(namespace);
			Rbd rbd = new Rbd(ioctx); // RBD
			RbdImage rbdImage = null;
			try {
				rbdImage = rbd.open(imgname);
				rbdImage.snapCreate(snapname);
				rbd.close(rbdImage);
				CephSnap cephSnap = saveSnapInfo(imgname, snapname, snapdetail);
				// 记录日志
				String extraInfo = "新增快照 " + JSON.toJSONString(cephSnap);
				LOGGER.info(extraInfo);
				CommonOperationLog log = CommonOprationLogUtils.getOprationLog(snapname, extraInfo,
						CommConstant.CEPH_SNAP, CommConstant.OPERATION_TYPE_CREATED);
				commonOperationLogDao.save(log);

				map.put("status", "200");
				return JSON.toJSONString(map);
			} catch (RbdException e) {
				LOGGER.error(e.getMessage());
				msg = "快照" + snapname + "创建失败!";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}

		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			msg = "pool" + namespace + "创建失败!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 快照策略定时创建快照
	 *
	 * @param poolname
	 * @param imgname
	 * @param snapname
	 * @param snapdetail
	 */
	public boolean autoCreateSnap(String poolname, String imgname, String snapname, String snapdetail) {
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			// 获取所有pool
			String[] pools = cluster.poolList();
			boolean poolExist = false;
			for (String pool : pools) {
				if (poolname.equals(pool)) {
					poolExist = true;
					break;
				}
			}

			if (!poolExist) {
				return false;
			}

			IoCTX ioctx = cluster.ioCtxCreate(poolname);
			Rbd rbd = new Rbd(ioctx); // RBD
			RbdImage rbdImage = null;
			try {
				rbdImage = rbd.open(imgname);
				rbdImage.snapCreate(snapname);
				rbd.close(rbdImage);
				CephSnap cephSnap = saveSnapInfo(poolname, imgname, snapname, snapdetail);
				// 记录日志
				String extraInfo = "快照策略自动拍照，新增快照 " + JSON.toJSONString(cephSnap);
				LOGGER.info(extraInfo);

				return true;
			} catch (RbdException e) {
				LOGGER.error(e.getMessage());
				return false;
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}

		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			return false;
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 检查指定镜像的快照是否存在
	 *
	 * @param imgname
	 * @param snapname
	 * @return
	 */
	@RequestMapping(value = { "ceph/checksnap" }, method = RequestMethod.GET)
	@ResponseBody
	public String checksnap(String imgname, String snapname) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		String msg = "";

		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			try {
				RbdImage rbdImage = rbd.open(imgname);
				List<RbdSnapInfo> snaplist = rbdImage.snapList();
				rbd.close(rbdImage);

				boolean snapexist = false;
				for (RbdSnapInfo snap : snaplist) {
					if (snapname.equals(snap.name)) {
						snapexist = true;
						break;
					}
				}

				if (snapexist) {
					map.put("exist", "1");
				} else {
					map.put("exist", "0");
				}

				map.put("status", "200");
				return JSON.toJSONString(map);
			} catch (RbdException e) {
				LOGGER.error(e.getMessage());
				msg = "ceph集群异常！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			LOGGER.error(e.getMessage());
			msg = "ceph集群异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 删除指定快照
	 *
	 * @param imgname
	 * @param snapname
	 * @return
	 */
	@RequestMapping(value = { "ceph/deletesnap" }, method = RequestMethod.GET)
	@ResponseBody
	public String deletesnap(String imgname, String snapname) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		map.put("status", "200");
		String msg = "";
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();
			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			try {
				RbdImage rbdImage = rbd.open(imgname);
				List<RbdSnapInfo> snaplist = rbdImage.snapList();
				boolean snapExist = false;
				for (RbdSnapInfo info : snaplist) {
					if (snapname.equals(info.name)) {
						snapExist = true;
						break;
					}
				}

				// 删除快照
				if (snapExist) {
					rbdImage.snapRemove(snapname);
				}
				rbd.close(rbdImage);

				// 数据库中删除快照
				cephSnapDao.deleteByNameAndImgname(snapname, imgname);

				// 记录日志
				String extraInfo = "删除快照 :" + imgname + "@" + snapname;
				LOGGER.info(extraInfo);
				CommonOperationLog log = CommonOprationLogUtils.getOprationLog(snapname, extraInfo,
						CommConstant.CEPH_SNAP, CommConstant.OPERATION_TYPE_DELETE);
				commonOperationLogDao.save(log);

				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "快照" + snapname + "删除失败！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			msg = "ceph集群异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 保存cephrbdinfo到数据库
	 *
	 * @param imgname
	 * @param disksize
	 * @param diskdetail
	 * @return
	 */
	private CephRbdInfo saveCephRbdInfo(String imgname, long disksize, String diskdetail) {
		CephRbdInfo info = new CephRbdInfo();
		info.setName(imgname);
		info.setPool(CurrentUserUtils.getInstance().getUser().getNamespace());
		info.setCreator(CurrentUserUtils.getInstance().getUser().getId());
		info.setSize(disksize);
		info.setDetail(diskdetail);
		info.setCreateDate(new Date());
		info.setUpdateDate(new Date());
		return cephRbdInfoDao.save(info);
	}

	/**
	 * 创建快照
	 *
	 * @param imgname
	 * @param snapname
	 * @param snapdetail
	 * @return
	 */
	private CephSnap saveSnapInfo(String imgname, String snapname, String snapdetail) {
		CephSnap snap = new CephSnap();
		snap.setImgname(imgname);
		snap.setName(snapname);
		snap.setPool(CurrentUserUtils.getInstance().getUser().getNamespace());
		snap.setCreator(CurrentUserUtils.getInstance().getUser().getId());
		snap.setSnapdetail(snapdetail);
		snap.setCreateDate(new Date());

		CephRbdInfo cephRbdInfo = cephRbdInfoDao
				.findByPoolAndName(CurrentUserUtils.getInstance().getUser().getNamespace(), imgname).get(0);
		snap.setImgId(cephRbdInfo.getId());
		cephSnapDao.save(snap);
		return snap;
	}

	/**
	 * 创建快照
	 *
	 * @param imgname
	 * @param snapname
	 * @param snapdetail
	 * @return
	 */
	private CephSnap saveSnapInfo(String poolname, String imgname, String snapname, String snapdetail) {
		CephSnap snap = new CephSnap();
		snap.setImgname(imgname);
		snap.setName(snapname);
		snap.setPool(poolname);
		snap.setSnapdetail(snapdetail);
		snap.setCreateDate(new Date());

		CephRbdInfo cephRbdInfo = cephRbdInfoDao
				.findByPoolAndName(poolname,imgname).get(0);
		snap.setImgId(cephRbdInfo.getId());
		cephSnapDao.save(snap);
		return snap;
	}

	/**
	 * 查看镜像是否存在
	 *
	 * @param imgname
	 * @return
	 */
	@RequestMapping(value = { "ceph/checkrbd" }, method = RequestMethod.GET)
	@ResponseBody
	public String checkrbd(String imgname) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		String msg = "";

		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

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
				cluster.poolCreate(namespace);
			}

			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			String[] images;
			try {
				images = rbd.list();
				boolean imageExist = false;
				for (String image : images) {
					if (imgname.equals(image)) {
						imageExist = true;
					}
				}

				if (imageExist) {
					map.put("exist", "1");
				} else {
					map.put("exist", "0");
				}

				map.put("status", "200");
				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "读取pool" + namespace + "中的镜像失败！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			msg = "ceph异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 删除cephrbd镜像
	 *
	 * @param imgname
	 * @return
	 */
	@RequestMapping(value = { "ceph/deleterbd" }, method = RequestMethod.GET)
	@ResponseBody
	public String deleteCephRbd(long imgId) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		map.put("status", "200");
		String msg = "";
		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(imgId);
		String imgname = cephRbdInfo.getName();
		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();
			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			String[] images;
			try {
				images = rbd.list();
				boolean imageExist = false;
				for (String image : images) {
					if (imgname.equals(image)) {
						imageExist = true;
						break;
					}
				}

				// 删除rbd
				if (imageExist) {
					// 停止快照策略
					if (cephRbdInfo.isStrategyexcuting()) {
						boolean result = SnapListener.removeTimer(cephRbdInfo);
						if (!result) {
							msg = "镜像" + imgname + "的快照策略取消失败！";
							map.put("msg", msg);
							map.put("status", "500");
							return JSON.toJSONString(map);
						}
					}

					// 删除镜像下所有的快照
					RbdImage rbdImage = rbd.open(imgname);
					List<RbdSnapInfo> snapInfos = rbdImage.snapList();
					for (RbdSnapInfo snapInfo : snapInfos) {
						rbdImage.snapRemove(snapInfo.name);

						// 数据库中删除快照
						cephSnapDao.deleteByName(snapInfo.name);
						LOGGER.error("快照" + imgname + "@" + snapInfo.name + "被删除！");
					}
					rbd.close(rbdImage);

					rbd.remove(imgname);

					// 数据库中删除镜像
					cephRbdInfoDao.delete(cephRbdInfo);
					serviceRbdDao.deleteByCephrbdId(imgId);

					// 记录日志
					String extraInfo = "删除镜像 " + JSON.toJSONString(cephRbdInfo);

					LOGGER.info(extraInfo);
					CommonOperationLog log = CommonOprationLogUtils.getOprationLog(imgname, extraInfo,
							CommConstant.CEPH_RBD, CommConstant.OPERATION_TYPE_DELETE);
					commonOperationLogDao.save(log);
				} else {
					msg = "镜像" + imgname + "在集群中不存在！";
					LOGGER.info(msg);
					map.put("msg", msg);
					map.put("status", "500");

					cephRbdInfoDao.delete(imgId);
					serviceRbdDao.deleteByCephrbdId(imgId);
				}

				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "镜像" + imgname + "删除失败！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			msg = "ceph集群异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 更改rbd块存储的大小
	 *
	 * @return
	 */
	@RequestMapping(value = { "ceph/updaterbdsize" }, method = RequestMethod.GET)
	@ResponseBody
	public String updateRbdSize(String imgname, String size) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		map.put("status", "200");
		String msg = "";

		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			String[] images;
			try {
				images = rbd.list();
				boolean imageExist = false;
				for (String image : images) {
					if (imgname.equals(image)) {
						imageExist = true;
						break;
					}
				}

				if (imageExist) {
					RbdImage rbdImage = rbd.open(imgname);
					long temp = Long.parseLong(size);
					rbdImage.resize(temp * 1024l * 1024l * 1024l);
					rbd.close(rbdImage);

					// 更改数据库
					List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByPoolAndName(namespace, imgname);
					cephRbdInfos.get(0).setSize(temp);
					cephRbdInfos.get(0).setUpdateDate(new Date());
					cephRbdInfoDao.save(cephRbdInfos.get(0));

					// 记录日志
					String extraInfo = "更改镜像大小 " + JSON.toJSONString(cephRbdInfos.get(0));
					LOGGER.info(extraInfo);
					CommonOperationLog log = CommonOprationLogUtils.getOprationLog(imgname, extraInfo,
							CommConstant.CEPH_RBD, CommConstant.OPERATION_TYPE_UPDATE);
					commonOperationLogDao.save(log);
				} else {
					msg = "镜像" + imgname + "不存在！";
					map.put("msg", msg);
					map.put("status", "500");
				}

				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "ceph集群异常！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			msg = "ceph集群异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 更改rbd块存储的描述
	 *
	 * @return
	 */
	@RequestMapping(value = { "ceph/updaterbddetail" }, method = RequestMethod.GET)
	@ResponseBody
	public String updateRbdDetail(String imgname, String detail) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		map.put("status", "200");
		String msg = "";

		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			String[] images;
			try {
				images = rbd.list();
				boolean imageExist = false;
				for (String image : images) {
					if (imgname.equals(image)) {
						imageExist = true;
						break;
					}
				}

				if (imageExist) {
					// 更改数据库
					List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByPoolAndName(namespace, imgname);
					cephRbdInfos.get(0).setDetail(detail);
					cephRbdInfos.get(0).setUpdateDate(new Date());
					cephRbdInfoDao.save(cephRbdInfos.get(0));

					// 记录日志
					String extraInfo = "更改镜像描述 " + JSON.toJSONString(cephRbdInfos.get(0));
					LOGGER.info(extraInfo);
					CommonOperationLog log = CommonOprationLogUtils.getOprationLog(imgname, extraInfo,
							CommConstant.CEPH_RBD, CommConstant.OPERATION_TYPE_UPDATE);
					commonOperationLogDao.save(log);
				} else {
					msg = "镜像" + imgname + "不存在！";
					map.put("msg", msg);
					map.put("status", "500");
				}

				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "ceph集群异常！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			msg = "ceph集群异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 更改rbd块存储的属性
	 *
	 * @return
	 */
	@RequestMapping(value = { "ceph/updaterbdproperty" }, method = RequestMethod.GET)
	@ResponseBody
	public String updateRbdProperty(String imgname, boolean release) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		map.put("status", "200");
		String msg = "";

		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			String[] images;
			try {
				images = rbd.list();
				boolean imageExist = false;
				for (String image : images) {
					if (imgname.equals(image)) {
						imageExist = true;
						break;
					}
				}

				if (imageExist) {
					// 更改数据库
					List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByPoolAndName(namespace, imgname);
					cephRbdInfos.get(0).setReleaseWhenServiceDown(release);
					cephRbdInfos.get(0).setUpdateDate(new Date());
					cephRbdInfoDao.save(cephRbdInfos.get(0));

					// 记录日志
					String extraInfo = "更改镜像描述 " + JSON.toJSONString(cephRbdInfos.get(0));
					LOGGER.info(extraInfo);
					CommonOperationLog log = CommonOprationLogUtils.getOprationLog(imgname, extraInfo,
							CommConstant.CEPH_RBD, CommConstant.OPERATION_TYPE_UPDATE);
					commonOperationLogDao.save(log);
				} else {
					msg = "镜像" + imgname + "不存在！";
					map.put("msg", msg);
					map.put("status", "500");
				}

				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "ceph集群异常！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			msg = "ceph集群异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 快照回滚
	 *
	 * @param imgname
	 * @param snapname
	 * @return
	 */
	@RequestMapping(value = { "ceph/rollback" }, method = RequestMethod.GET)
	@ResponseBody
	public String rollBack(String imgname, String snapname) {
		Map<String, String> map = new HashMap<>();
		String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
		map.put("status", "200");
		String msg = "";

		try {
			cluster = new Rados(CEPH_NAME);
			File f = new File(CEPH_DIR + CEPH_CONF);
			cluster.confReadFile(f);
			cluster.connect();

			IoCTX ioctx = cluster.ioCtxCreate(namespace);

			// 获取所有images
			Rbd rbd = new Rbd(ioctx);
			String[] images;
			try {
				images = rbd.list();
				boolean imageExist = false;
				for (String image : images) {
					if (imgname.equals(image)) {
						imageExist = true;
						break;
					}
				}

				if (imageExist) {
					RbdImage rbdImage = rbd.open(imgname);
					boolean snapExist = false;
					List<RbdSnapInfo> snapInfos = rbdImage.snapList();
					if (!CollectionUtils.isEmpty(snapInfos)) {
						for (RbdSnapInfo snapInfo : snapInfos) {
							if (snapInfo.name.equals(snapname)) {
								snapExist = true;
								break;
							}
						}
					}

					if (!snapExist) {
						msg = "快照" + snapname + "不存在！";
						map.put("msg", msg);
						map.put("status", "500");
					} else {
						// 回滚
						RbdImage snapImage = rbd.open(imgname, snapname);
						rbd.copy(snapImage, rbdImage);
						rbd.close(snapImage);

						// 记录日志
						String extraInfo = "使用快照" + snapname + "回滚！";
						LOGGER.info(extraInfo);
						CommonOperationLog log = CommonOprationLogUtils.getOprationLog(imgname, extraInfo,
								CommConstant.CEPH_SNAP, CommConstant.OPERATION_TYPE_UPDATE);
						commonOperationLogDao.save(log);
					}
					rbd.close(rbdImage);
				} else {
					msg = "镜像" + imgname + "不存在！";
					map.put("msg", msg);
					map.put("status", "500");
				}
				return JSON.toJSONString(map);
			} catch (RbdException e) {
				msg = "ceph集群异常！";
				map.put("msg", msg);
				map.put("status", "500");
				return JSON.toJSONString(map);
			} finally {
				if (cluster != null) {
					cluster.ioCtxDestroy(ioctx);
				}
			}
		} catch (RadosException e) {
			msg = "ceph集群异常！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		} finally {
			if (cluster != null) {
				cluster.shutDown();
			}
		}
	}

	/**
	 * 检查是否有服务正在使用rbd
	 *
	 * @param imgname
	 * @return
	 */
	@RequestMapping(value = { "ceph/checkrbdrunning" }, method = RequestMethod.GET)
	@ResponseBody
	public String checkRbdRunning(long imgId) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");
		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(imgId);

		if (cephRbdInfo == null) {
			msg = "数据库中找不到块设备!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		/*
		 * List<ServiceCephRbd> serviceCephRbds =
		 * serviceRbdDao.findByCephrbdId(imgId); if
		 * (CollectionUtils.isEmpty(serviceCephRbds)) { return
		 * JSON.toJSONString(map); }else{ long serviceId =
		 * serviceCephRbds.get(0).getServiceId(); Service service =
		 * serviceDao.findOne(serviceId);
		 * if(service==null||service.getStatus()!=3){ return
		 * JSON.toJSONString(map); }else{ msg = "服务: " +
		 * service.getServiceName() + "正在使用磁盘: " + cephRbdInfo.getName() + "," +
		 * "请先停止服务再进行操作，重启服务生效！"; map.put("msg", msg); map.put("status", "500");
		 * return JSON.toJSONString(map); } }
		 */

		List<ServiceCephRbd> serviceCephRbds = serviceRbdDao.findByCephrbdId(imgId);
		if (CollectionUtils.isEmpty(serviceCephRbds)) {
			return JSON.toJSONString(map);
		}

		if (!cephRbdInfo.isUsed()) {
			return JSON.toJSONString(map);
		} else {
			msg = "服务: " + serviceCephRbds.get(0).getServicename() + "正在使用磁盘: " + cephRbdInfo.getName() + ","
					+ "请先停止服务再进行操作，重启服务生效！";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}
	}

	/**
	 * 检查快照策略是否存在
	 *
	 * @param name
	 * @return
	 */
	@RequestMapping(value = { "ceph/checkSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String checkSnapStrategy(String name) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		if (StringUtils.isEmpty(name)) {
			msg = "快照策略名称为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		List<SnapStrategy> snapStrategies = snapStrategyDao.findByName(name);
		if (!CollectionUtils.isEmpty(snapStrategies)) {
			msg = "快照策略名称已经存在!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 创建快照策略
	 *
	 *
	 */
	@RequestMapping(value = { "ceph/createSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String createSnapStrategy(SnapStrategy snapStrategy) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		if (StringUtils.isEmpty(snapStrategy.getName())) {
			msg = "快照策略名称为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		if (StringUtils.isEmpty(snapStrategy.getTime())) {
			msg = "创建时间为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		if (StringUtils.isEmpty(snapStrategy.getWeek())) {
			msg = "重复日期为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		if (snapStrategy.getKeep() == 0) {
			msg = "保留时间为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		snapStrategy.setCreateDate(new Date());
		snapStrategy.setNamespace(CurrentUserUtils.getInstance().getUser().getNamespace());
		snapStrategy.setUserId(CurrentUserUtils.getInstance().getUser().getId());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, snapStrategy.getKeep());
		snapStrategy.setEndData(calendar.getTime());

		snapStrategyDao.save(snapStrategy);

		// 记录日志
		String extraInfo = "新建快照策略 " + JSON.toJSONString(snapStrategy);
		LOGGER.info(extraInfo);
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(snapStrategy.getName(), extraInfo,
				CommConstant.CEPH_SNAP_STRATEGY, CommConstant.OPERATION_TYPE_CREATED);
		commonOperationLogDao.save(log);

		return JSON.toJSONString(map);
	}

	/**
	 * 更新快照策略
	 *
	 * @param snapStrategy
	 * @return
	 */
	@RequestMapping(value = { "ceph/updateSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String updateSnapStrategy(SnapStrategy snapStrategy) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		long strategyId = snapStrategy.getId();
		if (strategyId == 0) {
			msg = "快照策略的Id为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByStrategyId(strategyId);
		if (!CollectionUtils.isEmpty(cephRbdInfos)) {
			for (CephRbdInfo cephRbdInfo : cephRbdInfos) {
				if (cephRbdInfo.isStrategyexcuting()) {
					msg = "快照策略正在执行，请取消执行后再更新!";
					map.put("msg", msg);
					map.put("status", "500");
					return JSON.toJSONString(map);
				}
			}
		}

		if (StringUtils.isEmpty(snapStrategy.getName())) {
			msg = "快照策略名称为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		if (StringUtils.isEmpty(snapStrategy.getTime())) {
			msg = "创建时间为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		if (StringUtils.isEmpty(snapStrategy.getWeek())) {
			msg = "重复日期为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		if (snapStrategy.getKeep() == 0) {
			msg = "保留时间为空!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		SnapStrategy orginSnapStrategy = snapStrategyDao.findOne(strategyId);
		orginSnapStrategy.setName(snapStrategy.getName());
		orginSnapStrategy.setTime(snapStrategy.getTime());
		orginSnapStrategy.setWeek(snapStrategy.getWeek());
		orginSnapStrategy.setKeep(snapStrategy.getKeep());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(orginSnapStrategy.getCreateDate());
		calendar.add(Calendar.DATE, snapStrategy.getKeep());
		orginSnapStrategy.setEndData(calendar.getTime());

		snapStrategyDao.save(orginSnapStrategy);

		// 记录日志
		String extraInfo = "更新快照策略 " + JSON.toJSONString(orginSnapStrategy);
		LOGGER.info(extraInfo);
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(orginSnapStrategy.getName(), extraInfo,
				CommConstant.CEPH_SNAP_STRATEGY, CommConstant.OPERATION_TYPE_UPDATE);
		commonOperationLogDao.save(log);

		return JSON.toJSONString(map);
	}

	/**
	 * 删除快照策略
	 *
	 * @param strategyId
	 * @return
	 */
	@RequestMapping(value = { "ceph/removeSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String removeSnapStrategy(long strategyId) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		SnapStrategy snapStrategy = snapStrategyDao.findById(strategyId);
		if (snapStrategy == null) {
			msg = "找不到指定的快照策略!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		boolean result = SnapListener.containStrategy(snapStrategy);
		if (result) {
			msg = "快照策略仍被块存储执行，请先取消执行再删除!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByStrategyId(strategyId);
		if (!CollectionUtils.isEmpty(cephRbdInfos)) {
			msg = "快照策略仍被块存储绑定，请先解除绑定再删除!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		SnapStrategy strategy = snapStrategyDao.findById(strategyId);
		snapStrategyDao.delete(snapStrategy);

		// 记录日志
		String extraInfo = "删除快照策略 " + JSON.toJSONString(strategy);
		LOGGER.info(extraInfo);
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(strategy.getName(), extraInfo,
				CommConstant.CEPH_SNAP_STRATEGY, CommConstant.OPERATION_TYPE_DELETE);
		commonOperationLogDao.save(log);

		return JSON.toJSONString(map);
	}

	/**
	 * 执行快照策略
	 *
	 * @param imgId
	 * @param strategyId
	 * @return
	 */
	@RequestMapping(value = { "ceph/excuteSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String excuteSnapStrategy(long imgId) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(imgId);
		long strategyId = cephRbdInfo.getStrategyId();
		if (strategyId == 0) {
			msg = "块设备没有配置快照策略!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		SnapStrategy snapStrategy = snapStrategyDao.findOne(strategyId);
		boolean result = SnapListener.addTimer(cephRbdInfo, snapStrategy);
		if (!result) {
			msg = "执行失败,快照策略可能已经到期!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		cephRbdInfo.setStrategyexcuting(true);
		cephRbdInfoDao.save(cephRbdInfo);

		// 记录日志
		String extraInfo = cephRbdInfo.getName() + "执行快照策略: " + JSON.toJSONString(snapStrategy);
		LOGGER.info(extraInfo);
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(
				cephRbdInfo.getName() + ":" + snapStrategy.getName(), extraInfo, CommConstant.CEPH_SNAP_STRATEGY,
				CommConstant.OPERATION_TYPE_DEPLOY);
		commonOperationLogDao.save(log);

		return JSON.toJSONString(map);
	}

	/**
	 * 取消快照策略的执行
	 *
	 * @param imgId
	 * @return
	 */
	@RequestMapping(value = { "ceph/cancelSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String cancelSnapStrategy(long imgId) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(imgId);
		boolean result = SnapListener.removeTimer(cephRbdInfo);
		if (!result) {
			msg = "取消快照策略失败!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		cephRbdInfo.setStrategyexcuting(false);
		cephRbdInfoDao.save(cephRbdInfo);

		// 记录日志
		String extraInfo = "取消快照策略: " + JSON.toJSONString(cephRbdInfo);
		LOGGER.info(extraInfo);
		CommonOperationLog log = CommonOprationLogUtils.getOprationLog(cephRbdInfo.getName(), extraInfo,
				CommConstant.CEPH_SNAP_STRATEGY, CommConstant.OPERATION_TYPE_DEPLOY);
		commonOperationLogDao.save(log);

		return JSON.toJSONString(map);
	}

	/**
	 * 块存储绑定快照策略
	 *
	 * @param imgId
	 * @param strategyId
	 * @return
	 */
	@RequestMapping(value = { "ceph/bindSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String bindSnapStrategy(long imgId, long strategyId) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(imgId);
		if (cephRbdInfo == null) {
			msg = "块存储绑定快照策略失败!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		cephRbdInfo.setStrategyId(strategyId);
		cephRbdInfoDao.save(cephRbdInfo);

		return JSON.toJSONString(map);
	}

	/**
	 * 将指定块存储的快照策略解除绑定
	 *
	 * @param imgId
	 * @return
	 */
	@RequestMapping(value = { "ceph/unbindSnapStrategy" }, method = RequestMethod.GET)
	@ResponseBody
	public String unbindSnapStrategy(long imgId) {
		Map<String, String> map = new HashMap<>();
		String msg = "";
		map.put("status", "200");

		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(imgId);
		if (cephRbdInfo == null) {
			msg = "找不到指定的块存储!";
			map.put("msg", msg);
			map.put("status", "500");
			return JSON.toJSONString(map);
		}

		cephRbdInfo.setStrategyId(0);
		cephRbdInfoDao.save(cephRbdInfo);

		return JSON.toJSONString(map);
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
		return MOUNT_EXEC;
	}

	public String getMountpoint() {
		return MOUNT_POINT;
	}

	public String getCephDir() {
		return CEPH_DIR;
	}

	/**
	 * 块存储主页面
	 * 管理员获取所有，租户和用户分别获取自己创建的，用户不共享租户的块设备
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "storage/storageBlock" }, method = RequestMethod.GET)
	public String storageQuick(Model model) {
		User user = CurrentUserUtils.getInstance().getUser();
		List<CephRbdInfo> cephRbdInfos=null;
		if(user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)){
			Iterable<CephRbdInfo> iterable = cephRbdInfoDao.findAll();
			if(null != iterable){
			Iterator<CephRbdInfo> iterator = iterable.iterator();
			cephRbdInfos = new ArrayList<CephRbdInfo>();
			while(iterator.hasNext()){
				cephRbdInfos.add(iterator.next());
			}
			}
		}else{
			cephRbdInfos= cephRbdInfoDao.findByPool("longlong");
			//cephRbdInfos = cephRbdInfoDao.findByCreator(user.getId());
			/*long parentId = user.getParent_id();
			List<CephRbdInfo> parentCeph = cephRbdInfoDao.findByCreator(parentId);
			if(CollectionUtils.isNotEmpty(parentCeph)){
				for(CephRbdInfo cephRbdInfo : parentCeph){
					cephRbdInfos.add(cephRbdInfo);
				}
			}*/
		}

		model.addAttribute("cephRbdInfos", cephRbdInfos);
		model.addAttribute("menu_flag", "storage");
		model.addAttribute("li_flag", "storageBlock");
		return "storage/storage-block.jsp";
	}

	/**
	 * 快照主页面
	 * 管理员获取所有，租户获取租户自建，用户获取用户自建
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "storage/storageSnap" }, method = RequestMethod.GET)
	public String storageSnap(Model model) {
		User user = CurrentUserUtils.getInstance().getUser();
		List<CephSnap> cephSnaps = new ArrayList<CephSnap>();
		if(user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)){
			Iterable<CephSnap> iterable = cephSnapDao.findAll();
			if(null != iterable){
			Iterator<CephSnap> iterator = iterable.iterator();
			while(iterator.hasNext()){
				cephSnaps.add(iterator.next());
			}
			}
		}else{
			List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByCreator(user.getId());
			if(CollectionUtils.isNotEmpty(cephRbdInfos)){
				for(CephRbdInfo cephRbdInfo:cephRbdInfos){
					cephSnaps.addAll(cephSnapDao.findByImgIdDesc(cephRbdInfo.getId()));
				}
			}
		}

		model.addAttribute("cephSnaps", cephSnaps);
		model.addAttribute("menu_flag", "storage");
		model.addAttribute("li_flag", "storageSnap");
		return "storage/storage-snap.jsp";
	}

	/**
	 * 快照策略主页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "storage/snapStrategy" }, method = RequestMethod.GET)
	public String storageSnapStrategy(Model model) {
		User user = CurrentUserUtils.getInstance().getUser();
		List<SnapStrategy> snapStrategies = null;
		if(user.getUser_autority().equals(UserConstant.AUTORITY_MANAGER)){
			Iterable<SnapStrategy> iterable = snapStrategyDao.findAll();
			if(null != iterable){
			Iterator<SnapStrategy> iterator = iterable.iterator();
			snapStrategies = new ArrayList<SnapStrategy>();
			while(iterator.hasNext()){
				snapStrategies.add(iterator.next());
			}
			}
		}else{
			snapStrategies = snapStrategyDao.findByUserId(user.getId());
		}

		if (!CollectionUtils.isEmpty(snapStrategies)) {
			for (SnapStrategy strategy : snapStrategies) {
				List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByStrategyId(strategy.getId());
				if (CollectionUtils.isEmpty(cephRbdInfos)) {
					strategy.setBindCount(0);
					strategy.setExcutingCount(0);
				} else {
					strategy.setBindCount(cephRbdInfos.size());
					int count = 0;
					for (CephRbdInfo cephRbdInfo : cephRbdInfos) {
						if (cephRbdInfo.isStrategyexcuting()) {
							count++;
						}
					}
					strategy.setExcutingCount(count);
				}
			}
		}

		model.addAttribute("snapStrategies", snapStrategies);
		model.addAttribute("menu_flag", "storage");
		model.addAttribute("li_flag", "snapStrategy");
		return "storage/storage-snapStrategy.jsp";
	}

	/**
	 * 获取指定快照策略的块存储信息
	 *
	 * @param model
	 * @param strategyId
	 * @return
	 */
	@RequestMapping(value = { "ceph/snapStrategyInfo" }, method = RequestMethod.GET)
	@ResponseBody
	public String getSnapStrategyBinded(long strategyId) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "200");

		SnapStrategy snapStrategy = snapStrategyDao.findOne(strategyId);
		List<CephRbdInfo> cephRbdInfos = cephRbdInfoDao.findByCreator(snapStrategy.getUserId());
		List<CephRbdInfo> bindedRbd = new ArrayList<CephRbdInfo>();
		List<CephRbdInfo> unbindedRbd = new ArrayList<CephRbdInfo>();
		if (!CollectionUtils.isEmpty(cephRbdInfos)) {
			for (CephRbdInfo cephRbdInfo : cephRbdInfos) {
				if (cephRbdInfo.getStrategyId() == strategyId) {
					bindedRbd.add(cephRbdInfo);
				} else {
					unbindedRbd.add(cephRbdInfo);
				}
			}
		}

		map.put("bindedRbd", bindedRbd);
		map.put("unbindedRbd", unbindedRbd);
		return JSON.toJSONString(map);
	}

	/**
	 * 获取指定块存储的快照策略
	 *
	 * @param imgId
	 * @return
	 */
	@RequestMapping(value = { "ceph/specifiedSnapStrategyInfo" }, method = RequestMethod.GET)
	@ResponseBody
	public String getSnapStrategyForSpecifiedRbd(long imgId) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "200");

		CephRbdInfo cephRbdInfo = cephRbdInfoDao.findOne(imgId);
		long strategyId = cephRbdInfo.getStrategyId();
		if (strategyId == 0) {
			map.put("status", "404");
			return JSON.toJSONString(map);
		}

		SnapStrategy snapStrategy = snapStrategyDao.findById(strategyId);
		boolean excuting = cephRbdInfo.isStrategyexcuting();

		map.put("snapStrategy", snapStrategy);
		map.put("excuting", excuting);

		return JSON.toJSONString(map);
	}

	/**
	 * 获取租户或者用户，没有被使用的块设备
	 * @return
	 */
	public List<CephRbdInfo> getUnUsedCephRbd(){
		User user = CurrentUserUtils.getInstance().getUser();
		List<CephRbdInfo> temp = cephRbdInfoDao.findByCreator(user.getId());
		List<CephRbdInfo> result = new ArrayList<CephRbdInfo>();
		if(CollectionUtils.isNotEmpty(temp)){
			for(CephRbdInfo cephRbdInfo:temp){
				if(serviceRbdDao.findByCephrbdId(cephRbdInfo.getId())==null){
					result.add(cephRbdInfo);
				}
			}
		}

		return result;
	}
}
