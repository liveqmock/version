package com.win.knowledge.ui;

import org.springframework.stereotype.Component;

import com.win.knowledge.entity.ProjectEntity;
import com.win.knowledge.service.ProjectService;
import com.win.knowledge.service.impl.ProjectServiceImpl;

@Component
public class KnowledgeMain {
	private ProjectService projectService = new ProjectServiceImpl();
	private TreeTest test = null;

	public void start() {
		test.setVisible(true);
	}

	private void initData() {

		ProjectEntity entity = new ProjectEntity();
		entity.setName("1");
		entity.setLeaf(false);
		entity.setRemark("测试1");
		projectService.save(entity);

		ProjectEntity entity2 = new ProjectEntity();
		entity2.setName("2");
		entity2.setLeaf(true);
		entity2.setRemark("测试2");
		entity2.setPid(entity.getId());
		projectService.save(entity2);

		ProjectEntity entity3 = new ProjectEntity();
		entity3.setName("2");
		entity3.setLeaf(true);
		entity3.setRemark("测试2");
		entity3.setPid(entity.getId());
		projectService.save(entity3);

	}
}
