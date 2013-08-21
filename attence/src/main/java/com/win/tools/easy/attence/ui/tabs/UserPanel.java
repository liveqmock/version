package com.win.tools.easy.attence.ui.tabs;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.win.tools.easy.attence.entity.UserEntity;
import com.win.tools.easy.attence.model.UserTableModel;
import com.win.tools.easy.attence.service.UserService;

/**
 * 用户面板
 * 
 * @author 袁晓冬
 * 
 */
public class UserPanel extends BasePanel {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1985721469287618355L;
	/** 查询面板-人员名称 */
	private JTextField userName = null;
	/** 查询面板-是否有效 */
	private JComboBox sfyxComboBox = null;
	/** 请假列表 */
	private UserTableModel userTableModel = null;
	/** table列表控件 */
	private JTable dataTable = null;
	/** 右键菜单-追加 */
	private JMenuItem popupInsertItem = null;
	/** 右键菜单-删除 */
	private JMenuItem popupDeleteItem = null;
	/** 右键菜单-更新 */
	private JMenuItem popupUpdateItem = null;
	/** table右键菜单 */
	private JPopupMenu tablePopupMenu = null;
	/** 用户服务 */
	private UserService service = null;

	public UserPanel() {
		service = new UserService();
		initBorderLayout();
	}

	@Override
	protected JComponent createToolbarComponent() {
		// 工具栏面板
		JPanel managerToolbar = new JPanel();
		JButton exportBtn = new JButton("导出");
		JButton refreshBtn = new JButton("查询");
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadRecords();
			}
		});
		managerToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		managerToolbar.add(exportBtn);
		managerToolbar.add(refreshBtn);
		return managerToolbar;
	}

	@Override
	protected JComponent createQueryComponent() {
		// 查询面板
		JPanel managerQuery = new JPanel();
		managerQuery.setLayout(new FlowLayout());
		managerQuery.add(new JLabel("人员名称："));
		userName = new JTextField(10);
		managerQuery.add(userName);
		managerQuery.add(new JLabel("是否有效："));
		sfyxComboBox = new JComboBox(new String[] { "", "是", "否" });
		managerQuery.add(sfyxComboBox);
		return managerQuery;
	}

	@Override
	protected JComponent createCenterComponent() {
		/** render渲染控制 */
		final DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -1138422127695925811L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				String rowStatus = String.valueOf(userTableModel.getValueAt(
						row, 2));
				if ("是".equals(rowStatus)) {
					setForeground(Color.BLACK);
				} else {
					setForeground(Color.RED);
				}
				setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}
		};
		userTableModel = new UserTableModel();
		// 数据一览
		dataTable = new JTable(userTableModel) {
			/** 序列号 */
			private static final long serialVersionUID = -2471831198768136302L;
			/** 请假类型下拉框 */
			private TableCellEditor comboxCellEditor = new DefaultCellEditor(
					new JComboBox(new String[] { "是", "否" }));

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				return render;
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				if (column == 2) {
					return comboxCellEditor;
				}
				return super.getCellEditor(row, column);
			}
		};
		dataTable.setRowHeight(22);
		MouseAdapter mouseAdapter = new MouseAdapter() {
			/**
			 * 右键菜单
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu popupMenu = getTablePopupMenu();
					JTable table = dataTable;
					// 右键所单击的行
					int row = table.rowAtPoint(e.getPoint());
					popupMenu.removeAll();
					if (row == -1) {
						popupMenu.add(popupInsertItem);
					} else {
						int[] rows = table.getSelectedRows();
						boolean inSelected = false;
						for (int r : rows) {
							if (row == r) {
								inSelected = true;
								break;
							}
						}
						popupMenu.add(popupDeleteItem);
						popupMenu.add(popupUpdateItem);
						// 当前鼠标右键所在行不被选中则高亮显示选中行
						if (!inSelected) {
							popupMenu.add(popupInsertItem);
							table.setRowSelectionInterval(row, row);
						} else {
							// 选中多行
							if (rows.length == 1) {
								popupMenu.add(popupInsertItem);
							}
						}
					}
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		dataTable.addMouseListener(mouseAdapter);
		dataTable.getTableHeader().addMouseListener(mouseAdapter);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(dataTable);
		return scrollPane;
	}

	/***
	 * 加载考勤数据
	 */
	private void loadRecords() {
		userTableModel.setData(queryRecords());
	}

	/**
	 * 根据画面条件查询记录
	 * 
	 * @return
	 */
	private List<UserEntity> queryRecords() {
		List<UserEntity> entities = null;
		String sfyx = this.sfyxComboBox.getSelectedItem().toString();
		if ("是".equals(sfyx)) {
			sfyx = "1";
		} else if ("否".equals(sfyx)) {
			sfyx = "0";
		} else {
			sfyx = "";
		}
		entities = service.getRecords(userName.getText());
		return entities;
	}

	/**
	 * 考勤面板右键菜单
	 * 
	 * @return
	 */
	private JPopupMenu getTablePopupMenu() {
		if (null == tablePopupMenu) {
			tablePopupMenu = new JPopupMenu();
			popupInsertItem = new JMenuItem("追加");
			popupInsertItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<UserEntity> records = queryRecords();
					records.add(0, new UserEntity());
					userTableModel.setData(records);
				}
			});
			popupDeleteItem = new JMenuItem("删除");
			popupDeleteItem.addActionListener(new ActionListener() {
				/**
				 * 删除处理
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					// 当前选中的entity
					List<UserEntity> selects = getSelectedEntities();
					service.deleteRecords(selects);
					loadRecords();
				}
			});
			popupUpdateItem = new JMenuItem("保存");
			popupUpdateItem.addActionListener(new ActionListener() {
				/**
				 * 更新处理
				 * 
				 * @param e
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					// 当前选中的entity
					List<UserEntity> updateList = getSelectedEntities();
					List<UserEntity> insertList = new ArrayList<UserEntity>(
							updateList.size());

					// 需要新增的记录放入插入集合中
					for (UserEntity entity : updateList) {
						if (entity.getId() == null
								|| entity.getId().length() == 0
								|| entity.getId().equals("null")) {
							insertList.add(entity);
						}
					}
					// 剔除新增记录
					updateList.removeAll(insertList);

					if (insertList.size() > 0) {
						service.insertRecords(insertList);
					}
					if (updateList.size() > 0) {
						service.updateRecords(updateList);
					}
					loadRecords();
				}
			});
		}
		return tablePopupMenu;
	}

	/**
	 * 获取当前选中的记录
	 * 
	 * @return
	 */
	private List<UserEntity> getSelectedEntities() {
		// 当前选中行
		int[] rows = dataTable.getSelectedRows();
		if (null == rows || rows.length == 0) {
			return null;
		}
		List<UserEntity> entities = new ArrayList<UserEntity>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			UserEntity entity = new UserEntity();
			// ID
			entity.setId(String.valueOf(userTableModel.getValueAt(rows[i], 4)));
			// 用户名
			entity.setName(String.valueOf(userTableModel.getValueAt(rows[i], 0)));
			// 邮箱
			entity.setEmail(String.valueOf(userTableModel
					.getValueAt(rows[i], 1)));
			// 备注
			String status = String.valueOf(userTableModel
					.getValueAt(rows[i], 2));
			if ("是".equals(status)) {
				entity.setStatus("1");
			} else {
				entity.setStatus("0");
			}
			// 备注
			entity.setRemark(String.valueOf(userTableModel.getValueAt(rows[i],
					3)));
			entities.add(entity);
		}
		return entities;
	}
}
