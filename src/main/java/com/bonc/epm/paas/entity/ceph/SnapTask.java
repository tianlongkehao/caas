package com.bonc.epm.paas.entity.ceph;

import java.util.Date;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.bonc.epm.paas.SpringApplicationContext;
import com.bonc.epm.paas.controller.CephController;
import com.bonc.epm.paas.dao.SnapStrategyDao;

public class SnapTask extends TimerTask {

	private CephController cephController;

	private CephRbdInfo cephRbdInfo;

	private SnapStrategy snapStrategy;

	public SnapTask(CephRbdInfo cephRbdInfo, SnapStrategy snapStrategy) {
		this.cephRbdInfo = cephRbdInfo;
		this.snapStrategy = snapStrategy;
	}

	@Override
	public void run() {
		cephController = SpringApplicationContext.getBean(CephController.class);
		Date date = new Date();
		Date endDate = snapStrategy.getEndData();
		if (endDate.compareTo(date) >= 0) {
			int day = date.getDay();
			String week = snapStrategy.getWeek();
			if (!StringUtils.isEmpty(week)) {
				String[] weeks = week.split(",");
				if (weeks.length != 0) {
					for (String w : weeks) {
						if (day == Integer.parseInt(w)) {
							cephController.autoCreateSnap(cephRbdInfo.getPool(), cephRbdInfo.getName(),
									cephRbdInfo.getName() + UUID.randomUUID().toString(), "快照策略自动拍照" + date.toString());
							break;
						}
					}
				}
			}
		}

	}

}
