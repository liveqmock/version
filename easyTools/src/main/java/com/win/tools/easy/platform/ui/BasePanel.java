package com.win.tools.easy.platform.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.win.tools.easy.platform.entity.BaseEntity;
import com.win.tools.easy.platform.model.BaseTableModel;

/**
 * 面板基类
 * 
 * @author 袁晓冬
 * 
 */
public class BasePanel<T extends BaseEntity> extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4137759063237545777L;
	/** 列表数据模型 */
	protected BaseTableModel<T> baseTableModel = null;
	/** 列表控件 */
	protected JTable dataTable = null;

	/**
	 * 初始化BorderLayout样式面板
	 */
	protected void initBorderLayout() {
		setLayout(new BorderLayout());
		JComponent toolbar = createToolbarComponent();
		if (null != toolbar) {
			add(toolbar, BorderLayout.NORTH);
		}
		JComponent queryComp = createQueryComponent();
		if (null != queryComp) {
			add(queryComp, BorderLayout.SOUTH);
		}
		JComponent tableComp = createCenterComponent();
		if (null != tableComp) {
			add(tableComp, BorderLayout.CENTER);
		}
	}

	/**
	 * 工具栏面板-NORTH
	 * 
	 * @return JPanel
	 */
	protected JComponent createToolbarComponent() {
		return null;
	}

	/**
	 * 查询面板-SOUTH
	 * 
	 * @return JPanel
	 */
	protected JComponent createQueryComponent() {
		return null;
	}

	/**
	 * table列表面板-CENTER
	 * 
	 * @return JPanel
	 */
	protected JComponent createCenterComponent() {
		return null;
	}

	/**
	 * 一览查询
	 * 
	 * @return
	 */
	public List<T> queryRecords() {
		return null;
	}

	/**
	 * 刷新画面
	 */
	public void refresh() {
		List<T> entities = queryRecords();
		if (null != entities)
			loadRecords(entities);
	}

	/**
	 * 一览加载
	 * 
	 * @param entities
	 */
	public void loadRecords(List<T> entities) {
		baseTableModel.setData(entities);
	}
	
}
