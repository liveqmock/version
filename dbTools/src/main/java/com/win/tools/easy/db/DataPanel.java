package com.win.tools.easy.db;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DataPanel extends JPanel {
	private JTable table = null;
	private DataTableModel dataTableModel = null;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8682266876802911859L;

	public DataPanel() {
		setLayout(new BorderLayout());
		// 工具栏
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton test = new JButton("test");
		toolbar.add(test);
		this.add(toolbar, BorderLayout.NORTH);
		// 数据
		JScrollPane dataScrollPane = new JScrollPane();
		dataTableModel = new DataTableModel();
		table = new JTable(dataTableModel);
		dataScrollPane.setViewportView(table);
		this.add(dataScrollPane, BorderLayout.CENTER);
	}

	/**
	 * 设置当前显示的数据集
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	public void setResultSet(ResultSet rs) throws SQLException {
		dataTableModel.setResultSet(rs);
	}
}
