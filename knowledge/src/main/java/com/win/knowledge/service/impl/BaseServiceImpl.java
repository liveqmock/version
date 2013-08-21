package com.win.knowledge.service.impl;

import java.io.Serializable;
import java.util.Collection;

import com.win.knowledge.dao.BaseDAO;
import com.win.knowledge.service.BaseService;
import com.win.tools.easy.platform.entity.BaseEntity;

public abstract class BaseServiceImpl<E extends BaseEntity, PK extends Serializable>
		implements BaseService<E, PK> {

	public abstract BaseDAO<E, PK> getBaseDAO();

	@Override
	public void save(E entity) {
		getBaseDAO().saveOrUpdate(entity);
	}

	@Override
	public void save(Collection<E> entities) {
		getBaseDAO().save(entities);
	}

	@Override
	public E get(PK id) {
		return getBaseDAO().get(id);
	}
	@Override
	public void delete(PK id) {
		getBaseDAO().delete(id);
	}
	@Override
	public void delete(E entity) {
		getBaseDAO().delete(entity);
	}
}
