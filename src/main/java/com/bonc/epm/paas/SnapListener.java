package com.bonc.epm.paas;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import org.hibernate.type.descriptor.java.CalendarDateTypeDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bonc.epm.paas.dao.CephRbdInfoDao;
import com.bonc.epm.paas.dao.SnapStrategyDao;
import com.bonc.epm.paas.entity.ceph.CephRbdInfo;
import com.bonc.epm.paas.entity.ceph.SnapStrategy;
import com.bonc.epm.paas.entity.ceph.SnapTask;

public class SnapListener implements ApplicationListener<ContextRefreshedEvent> {

	private CephRbdInfoDao cephRbdInfoDao;

	private SnapStrategyDao snapStrategyDao;

	private static Map<CephRbdInfo, Timer> map;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		map = new HashMap<CephRbdInfo, Timer>();
		snapStrategyDao = SpringApplicationContext.getBean(SnapStrategyDao.class);
		cephRbdInfoDao = SpringApplicationContext.getBean(CephRbdInfoDao.class);
		Iterable<CephRbdInfo> iterable = cephRbdInfoDao.findAll();
		Iterator<CephRbdInfo> iterator = iterable.iterator();
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		while (iterator.hasNext()) {
			CephRbdInfo cephRbdInfo = iterator.next();
			if (cephRbdInfo.getStrategyId() != 0 && cephRbdInfo.isStrategyexcuting()) {
				long strategyId = cephRbdInfo.getStrategyId();
				SnapStrategy snapStrategy = snapStrategyDao.findById(strategyId);
				if (snapStrategy != null) {
					Date endDate = snapStrategy.getEndData();
					if (now.after(endDate)) { // 已经到期
						cephRbdInfo.setStrategyexcuting(false);
						cephRbdInfoDao.save(cephRbdInfo);
						continue;
					}

					String[] times = snapStrategy.getTime().split(",");
					if (times.length != 0) {
						Timer timer = new Timer();
						for (String time : times) {
							SnapTask snapTask = new SnapTask(cephRbdInfo, snapStrategy);

							calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time));
							calendar.set(Calendar.MINUTE, 0);
							calendar.set(Calendar.SECOND, 0);
							Date date = calendar.getTime();

							calendar.add(Calendar.DATE, 1);
							Date tomorrow = calendar.getTime();

							if (now.before(date)) {
								timer.scheduleAtFixedRate(snapTask, date, 24l * 3600l * 1000l);// 第一次时间是今天某一刻，周期为一天
							} else {
								timer.scheduleAtFixedRate(snapTask, tomorrow, 24l * 3600l * 1000l);
							}

						}
						map.put(cephRbdInfo, timer);
					}
				}
			}
		}
	}

	// 删除快照任务
	public static boolean removeTimer(CephRbdInfo cephRbdInfo) {
		if (map != null) {
			if (map.containsKey(cephRbdInfo)) {
				Timer timer = map.get(cephRbdInfo);
				timer.cancel();
				timer.purge();
				map.remove(cephRbdInfo);
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	// 增加快照任务
	public static boolean addTimer(CephRbdInfo cephRbdInfo, SnapStrategy snapStrategy) {
		if (map.containsKey(cephRbdInfo)) {
			Timer timer = map.get(cephRbdInfo);
			timer.cancel();
			timer.purge();
			map.remove(cephRbdInfo);
		}
		if (cephRbdInfo.getStrategyId() != 0) {
			if (snapStrategy != null) {
				Calendar calendar = Calendar.getInstance();
				Date now = calendar.getTime();
				Date endDate = snapStrategy.getEndData();
				if (now.after(endDate)) { // 已经到期
					return false;
				}

				String[] times = snapStrategy.getTime().split(",");
				if (times.length != 0) {
					Timer timer = new Timer();
					for (String time : times) {
						SnapTask snapTask = new SnapTask(cephRbdInfo, snapStrategy);

						calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time));
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						Date date = calendar.getTime();

						calendar.add(Calendar.DATE, 1);
						Date tomorrow = calendar.getTime();

						if (now.before(date)) {
							timer.scheduleAtFixedRate(snapTask, date, 24l * 3600l * 1000l);// 第一次时间是今天某一刻，周期为一天
						} else {
							timer.scheduleAtFixedRate(snapTask, tomorrow, 24l * 3600l * 1000l);
						}
					}
					map.put(cephRbdInfo, timer);
				} else {
					return false;
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean containRbd(CephRbdInfo cephRbdInfo) {
		if (map == null) {
			return false;
		}
		return map.containsKey(cephRbdInfo);
	}

	public static boolean containStrategy(SnapStrategy snapStrategy) {
		if (map == null) {
			return false;
		}
		return map.containsValue(snapStrategy);
	}
}
