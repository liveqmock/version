package com.win.knowledge.dao;

import java.util.List;

import com.win.knowledge.entity.ProjectEntity;

/**
 * 项目DAO
 * 
 * @author 袁晓冬
 * 
 */
public interface ProjectDAO extends BaseDAO<ProjectEntity, String> {
	/**
	 * 获取根节点的下级项目，即项目的PID为空的记录
	 * 
	 * @return
	 */
	List<ProjectEntity> getChildrenById(String pid);
}
