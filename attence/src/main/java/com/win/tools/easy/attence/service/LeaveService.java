package com.win.tools.easy.attence.service;

import java.util.Date;
import java.util.List;

import com.win.tools.easy.attence.dao.LeaveDAO;
import com.win.tools.easy.attence.entity.LeaveEntity;

/**
 * 考勤服务
 * 
 * @author 袁晓冬
 * 
 */
public class LeaveService {
	private LeaveDAO dao;

	public LeaveService() {
		dao = new LeaveDAO();
	}

	public List<LeaveEntity> getRecords(Date start, Date end, String userName) {
		return dao.getRecords(start, end, userName);
	}

	/**
	 * 插入记录
	 * 
	 * @param entities
	 */
	public void insertRecords(List<LeaveEntity> entities) {
		dao.insertRecords(entities);
	}

	/**
	 * 删除记录
	 * 
	 * @param entities
	 */
	public void deleteRecords(List<LeaveEntity> entities) {
		dao.deleteRecords(entities);
	}

	/**
	 * 更新记录
	 * 
	 * @param entities
	 */
	public void updateRecords(List<LeaveEntity> entities) {
		dao.updateRecords(entities);
	}
}
