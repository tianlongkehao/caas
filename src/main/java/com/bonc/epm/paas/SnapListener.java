package com.bonc.epm.paas;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bonc.epm.paas.dao.CephRbdInfoDao;
import com.bonc.epm.paas.dao.SnapStrategyDao;
import com.bonc.epm.paas.entity.ceph.CephRbdInfo;
import com.bonc.epm.paas.entity.ceph.SnapStrategy;
import com.bonc.epm.paas.entity.ceph.SnapTask;

public class SnapListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private CephRbdInfoDao cephRbdInfoDao;

	@Autowired
	private SnapStrategyDao snapStrategyDao;

	private static Map<CephRbdInfo, Timer> map;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		map = new HashMap<CephRbdInfo, Timer>();
		Iterable<CephRbdInfo> iterable = cephRbdInfoDao.findAll();
		while (iterable.iterator().hasNext()) {
			CephRbdInfo cephRbdInfo = iterable.iterator().next();
			if (cephRbdInfo.getStrategyId() != 0) {
				long strategyId = cephRbdInfo.getStrategyId();
				SnapStrategy snapStrategy = snapStrategyDao.findById(strategyId);
				if (snapStrategy != null) {
					String[] times = snapStrategy.getTime().split(",");
					if (times.length != 0) {
						Timer timer = new Timer();
						for (String time : times) {
							SnapTask snapTask = new SnapTask(cephRbdInfo, snapStrategy);
							Date date = new Date();
							date.setTime(Integer.parseInt(time));
							timer.scheduleAtFixedRate(snapTask, date, 24 * 3600 * 1000);// 第一次时间是今天某一刻，周期为一天
						}
						map.put(cephRbdInfo, timer);
					}
				}
			}
		}
	}

	// 删除快照任务
	public static void removeTimer(CephRbdInfo cephRbdInfo) {
		if (map != null) {
			Timer timer = map.get(cephRbdInfo);
			timer.cancel();
			map.remove(cephRbdInfo);
		}
	}

	//增加快照任务
	public static void addTimer(CephRbdInfo cephRbdInfo, SnapStrategy snapStrategy) {
		if (map.containsKey(cephRbdInfo)) {
			Timer timer = map.get(cephRbdInfo);
			timer.cancel();
			map.remove(cephRbdInfo);
		}
		if (cephRbdInfo.getStrategyId() != 0) {
			if (snapStrategy != null) {
				String[] times = snapStrategy.getTime().split(",");
				if (times.length != 0) {
					Timer timer = new Timer();
					for (String time : times) {
						SnapTask snapTask = new SnapTask(cephRbdInfo, snapStrategy);
						Date date = new Date();
						date.setTime(Integer.parseInt(time));
						timer.scheduleAtFixedRate(snapTask, date, 24 * 3600 * 1000);// 第一次时间是今天某一刻，周期为一天
					}
					map.put(cephRbdInfo, timer);
				}
			}
		}
	}
}
