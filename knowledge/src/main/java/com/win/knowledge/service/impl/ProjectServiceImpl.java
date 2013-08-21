package com.win.knowledge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.win.knowledge.dao.BaseDAO;
import com.win.knowledge.dao.ProjectDAO;
import com.win.knowledge.entity.ProjectEntity;
import com.win.knowledge.service.ProjectService;

@Service
public class ProjectServiceImpl extends BaseServiceImpl<ProjectEntity, String>
		implements ProjectService {
	@Autowired
	private ProjectDAO dao;

	@Override
	public BaseDAO<ProjectEntity, String> getBaseDAO() {
		return dao;
	}

	@Override
	public List<ProjectEntity> getChildrenById(String pid) {
		return dao.getChildrenById(pid);
	}
}
