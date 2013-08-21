package com.win.tools.easy.attence.service;

import java.util.Date;
import java.util.List;

import com.win.tools.easy.attence.dao.WorkdayDAO;
import com.win.tools.easy.attence.entity.WorkdayEntity;

/**
 * 考勤服务
 * 
 * @author 袁晓冬
 * 
 */
public class WorkdayService {
	private WorkdayDAO dao;

	public WorkdayService() {
		dao = new WorkdayDAO();
	}

	/**
	 * 初始化开始日期和结束日期之间的工作日
	 * 
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 */
	public void doWorkInit(Date start, Date end) {
		dao.doWorkInit(start, end);
	}

	/**
	 * 查询记录
	 * 
	 * @return
	 */
	public List<WorkdayEntity> getRecords(Date start, Date end) {
		return dao.getRecords(start, end);
	}

	/**
	 * 删除记录
	 * 
	 * @param entities
	 */
	public void deleteRecords(List<WorkdayEntity> entities) {
		dao.deleteRecords(entities);
	}

	/**
	 * 更新记录
	 * 
	 * @param entities
	 */
	public void updateRecords(List<WorkdayEntity> entities) {
		dao.updateRecords(entities);
	}

	/**
	 * 插入记录
	 * 
	 * @param entities
	 */
	public void insertRecords(List<WorkdayEntity> entities) {
		dao.insertRecords(entities);
	}
}
