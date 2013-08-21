package com.win.tools.easy.attence.service;

import com.win.tools.easy.attence.dao.SettingDAO;

/**
 * 考勤服务
 * 
 * @author 袁晓冬
 * 
 */
public class SettingService {
	private SettingDAO dao;

	public SettingService() {
		dao = new SettingDAO();
	}

	/**
	 * 初始化数据库表结构
	 */
	public void initDatabase() {
		dao.initDatabase();
	}
}
