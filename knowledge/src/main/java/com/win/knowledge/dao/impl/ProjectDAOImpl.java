package com.win.knowledge.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.win.knowledge.dao.ProjectDAO;
import com.win.knowledge.entity.ProjectEntity;

@Repository
public class ProjectDAOImpl extends BaseDAOImpl<ProjectEntity, String>
		implements ProjectDAO {


	@SuppressWarnings("unchecked")
	public List<ProjectEntity> getChildrenById(String pid) {
		if (StringUtils.hasLength(pid)) {
			return getSession()
					.createQuery("from ProjectEntity where pid=:pid ")
					.setParameter("pid", pid).list();
		} else {
			return getSession().createQuery(
					"from ProjectEntity where pid is null ").list();
		}
	}
}
