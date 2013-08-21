package com.win.knowledge.ui;

import java.util.List;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.win.knowledge.entity.ProjectEntity;
import com.win.knowledge.service.ProjectService;

/**
 * 项目树模型
 * 
 * @author 袁晓冬
 * 
 */
@Component
public class ProjectTreeModel extends DefaultTreeModel implements
		TreeWillExpandListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6970955796459535597L;

	private ProjectService service;

	@Autowired
	public void setService(ProjectService service) {
		this.service = service;
		List<ProjectEntity> firstProjects = this.service.getChildrenById(null);
		for (ProjectEntity entity : firstProjects) {
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;
			rootNode.add(new DefaultMutableTreeNode(entity));
		}
	}

	/**
	 * 创建根节点为“项目”的项目树，是否存在子节点由节点方法判断
	 */
	public ProjectTreeModel() {
		this(true);
	}

	/**
	 * 指定asksAllowsChildren来创建根节点为“项目”的项目树
	 * 
	 * @param asksAllowsChildren
	 */
	public ProjectTreeModel(boolean asksAllowsChildren) {
		super(new DefaultMutableTreeNode("项目", true), asksAllowsChildren);
	}

	/**
	 * 节点展开前处理
	 */
	@Override
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {
		TreePath path = event.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		ProjectEntity entity = (ProjectEntity) node.getUserObject();
		List<ProjectEntity> children = service.getChildrenById(entity.getId());
		for (ProjectEntity child : children) {
			DefaultMutableTreeNode cNode = new DefaultMutableTreeNode(child);
			node.add(cNode);
		}
	}

	/**
	 * 节点折叠前处理
	 */
	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {

	}

}
