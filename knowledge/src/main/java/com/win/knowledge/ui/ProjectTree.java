package com.win.knowledge.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.springframework.beans.factory.annotation.Autowired;

import com.win.knowledge.entity.ProjectEntity;

/**
 * 项目树
 * 
 * @author 袁晓冬
 * 
 */
public class ProjectTree extends JTree {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3081372138628883822L;

	/** 树图模型 */
	private ProjectTreeModel projectTreeModel;

	/**
	 * 以ProjectTreeModel创建项目树
	 */
	@Autowired
	public ProjectTree(ProjectTreeModel model) {
		super(model);
		this.projectTreeModel = model;
		// 添加节点展开前处理
		this.addTreeWillExpandListener(this.projectTreeModel);
		this.setCellRenderer(new FileTreeRenderer());
	}
}

class FileTreeRenderer extends DefaultTreeCellRenderer {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7733501585876709028L;

	public FileTreeRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent(javax.swing.JTree tree,
			java.lang.Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value,
				sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.isRoot()) {
			return label;
		}
		ProjectEntity projectEntity = (ProjectEntity) node.getUserObject();
		label.setText(projectEntity.getName());
		label.setOpaque(false);
		return label;
	}
}