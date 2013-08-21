package com.win.tools.easy.db;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.win.tools.easy.platform.ui.InternalFrame;

/**
 * 数据库主面板窗口窗口
 * 
 * @author 袁晓冬
 * 
 */
public class DatabaseFrame {
	/** 窗口对象 */
	private InternalFrame internalFrame;
	/** 连接信息 */
	private ConnectInfo info;
	/** table列表 */
	private final TableListModel tableListModel = new TableListModel();
	/** table控件 */
	private final JList tableList = new JList(tableListModel);
	/** 数据列表 */
	private DataPanel dataPanel = new DataPanel();
	/** 选项卡 */
	private JTabbedPane tabbedPane = null;
	/** columns列表 */
	private ColumnListModel columnModel = null;

	/**
	 * 构造函数
	 * 
	 * @param internalFrame
	 * @param info
	 */
	public DatabaseFrame(InternalFrame internalFrame, ConnectInfo info) {
		this.internalFrame = internalFrame;
		this.info = info;
	}

	/**
	 * 更新链接配置
	 * 
	 * @param info
	 */
	public void updateConnectionInfo(ConnectInfo info) {
		this.info = info;
	}

	/**
	 * 测试连接数据库
	 * 
	 * @return
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public boolean tryConnect() throws Exception {
		Connection connection = null;
		try {
			connection = getConnection();
			return connection != null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Connection connection = null;
		Class.forName(info.getDriver());
		connection = DriverManager.getConnection(info.getUrl(), info.getUser(),
				info.getPassword());
		return connection;
	}

	/**
	 * 显示画面
	 */
	public void show() {
		try {
			internalFrame.setMaximum(true);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}
		// 添加table列表
		tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				int index = tableList.getSelectedIndex();
				if (index < 0) {
					return;
				}
				Object obj = tableListModel.getElementAt(index);
				showTable(String.valueOf(obj));
			}
		});
		internalFrame.add(tableList, BorderLayout.WEST);
		// 详细列表
		internalFrame.add(getContentPanel(), BorderLayout.CENTER);
		// 显示窗口
		internalFrame.setVisible(true);
		// 刷新table列表
		refreshTableNames();
	}

	/**
	 * 显示table的详细信息
	 * 
	 * @param tableName
	 */
	private void showTable(String tableName) {
		int selectIndex = tabbedPane.getSelectedIndex();
		String selectTitle = tabbedPane.getTitleAt(selectIndex);
		if ("columns".equals(selectTitle)) {
			showTableColumns(tableName);
		} else if ("data".equals(selectTitle)) {
			showTableData(tableName);
		}
	}

	/**
	 * 显示table的columns信息
	 * 
	 * @param tableName
	 */
	private void showTableColumns(String tableName) {
		Connection connection = null;
		ResultSet coumnSet = null;
		ResultSet pkSet = null;
		List<ColumnInfo> columnList = null;
		try {
			connection = getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();
			coumnSet = dbmd.getColumns(null, "%", tableName, "%");
			columnList = ColumnInfo.getColumnInfosFromResultSet(coumnSet);
			pkSet = dbmd.getPrimaryKeys(null, "%", tableName);
			ColumnInfo.addPK(columnList, pkSet);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != coumnSet) {
				try {
					coumnSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != pkSet) {
				try {
					pkSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != connection) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		Collections.sort(columnList, new Comparator<ColumnInfo>() {
			@Override
			public int compare(ColumnInfo o1, ColumnInfo o2) {
				return o1.getOrdinalPosition() - o2.getOrdinalPosition();
			}
		});
		columnModel.setData(columnList);
	}

	/**
	 * 显示table的数据
	 * 
	 * @param tableName
	 */
	private void showTableData(String tableName) {
		String sql = "select * from " + tableName;
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			rs = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(sql);
			dataPanel.setResultSet(rs);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取内容面板
	 * 
	 * @return
	 */
	public JPanel getContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		// 添加选项卡控件
		tabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = tableList.getSelectedIndex();
				if (index < 0) {
					return;
				}
				Object obj = tableListModel.getElementAt(index);
				showTable(String.valueOf(obj));
			}
		});
		panel.add(tabbedPane, BorderLayout.CENTER);
		columnModel = new ColumnListModel();
		JTable table = new JTable(columnModel) {
			private static final long serialVersionUID = 1L;

			/**
			 * 设置单元格只读
			 */
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			/**
			 * 居中显示
			 */
			@Override
			public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
				DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super
						.getDefaultRenderer(columnClass);
				cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return cr;
			}
		};
		JScrollPane sPane = new JScrollPane();
		sPane.setViewportView(table);
		JPanel columnsPanel = new JPanel();
		columnsPanel.setLayout(new BorderLayout());
		columnsPanel.add(sPane, BorderLayout.CENTER);
		JPanel toolbar = new JPanel();
		JButton refreshBtn = new JButton("刷新");
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = tableList.getSelectedIndex();
				if (index < 0) {
					return;
				}
				Object obj = tableListModel.getElementAt(index);
				showTable(String.valueOf(obj));
			}
		});
		toolbar.add(refreshBtn);
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		columnsPanel.add(toolbar, BorderLayout.NORTH);
		tabbedPane.addTab("columns", columnsPanel);
		tabbedPane.addTab("data", dataPanel);
		return panel;
	}

	/**
	 * 刷新列表
	 */
	protected void refreshTableNames() {
		Connection connection = null;
		ResultSet tableSet = null;
		try {
			connection = getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();
			tableSet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
			while (tableSet.next()) {
				tableListModel.addElement(tableSet.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != tableSet) {
				try {
					tableSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (null != connection) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
