package com.win.knowledge.service;

import java.util.List;

import com.win.knowledge.entity.ProjectEntity;

/**
 * 项目服务
 * 
 * @author 袁晓冬
 * 
 */
public interface ProjectService extends BaseService<ProjectEntity, String> {
	/**
	 * 获取根节点的下级项目
	 * 
	 * @return
	 */
	List<ProjectEntity> getChildrenById(String pid);
}
