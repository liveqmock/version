package com.win.tools.easy.attence.service;

import java.util.List;

import com.win.tools.easy.attence.dao.UserDAO;
import com.win.tools.easy.attence.entity.UserEntity;

/**
 * 考勤服务
 * 
 * @author 袁晓冬
 * 
 */
public class UserService {
	private UserDAO dao;

	public UserService() {
		dao = new UserDAO();
	}

	public List<UserEntity> getRecords(String userName) {
		return dao.getRecords(userName, null);
	}

	/**
	 * 快速创建用户
	 * 
	 * @param names
	 * @return
	 */
	public boolean fastSaveUser(List<String> names) {
		return dao.fastSaveUser(names);
	}

	/**
	 * 插入记录
	 * @param entities
	 */
	public void insertRecords(List<UserEntity> entities) {
		dao.insertRecords(entities);
	}

	/**
	 * 删除记录
	 * 
	 * @param entities
	 */
	public void deleteRecords(List<UserEntity> entities) {
		dao.deleteRecords(entities);
	}

	/**
	 * 更新记录
	 * 
	 * @param entities
	 */
	public void updateRecords(List<UserEntity> entities) {
		dao.updateRecords(entities);
	}
}
