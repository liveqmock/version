package com.win.tools.easy.attence.ui.tabs;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.win.tools.easy.attence.entity.WorkdayEntity;
import com.win.tools.easy.attence.model.WorkdayTableModel;
import com.win.tools.easy.attence.service.WorkdayService;

/**
 * 工作日面板
 * 
 * @author 袁晓冬
 * 
 */
public class WorkdayPanel extends BasePanel {

	/**
	 * serialVersion
	 */
	private static final long serialVersionUID = 4462639246709617341L;
	/** 考勤列表 */
	private WorkdayTableModel workdayTableModel = null;
	/** table列表控件 */
	private JTable dataTable = null;
	/** table右键菜单 */
	private JPopupMenu tablePopupMenu = null;
	/** 右键菜单-追加 */
	private JMenuItem popupInsertItem = null;
	/** 右键菜单-删除 */
	private JMenuItem popupDeleteItem = null;
	/** 右键菜单-更新 */
	private JMenuItem popupUpdateItem = null;
	/** 查询面板-开始时间 */
	private JTextField startTime = null;
	/** 查询面板-结束时间 */
	private JTextField endTime = null;
	/** 工作日服务 */
	WorkdayService service = null;

	public WorkdayPanel() {
		service = new WorkdayService();
		initBorderLayout();
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
				String iswork = String.valueOf(workdayTableModel.getValueAt(
						row, 1));
				if ("是".equals(iswork)) {
					setForeground(Color.BLACK);
				} else {
					setForeground(Color.RED);
				}
				setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}
		};
		workdayTableModel = new WorkdayTableModel();
		// 数据一览
		dataTable = new JTable(workdayTableModel) {
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
				if (column == 1) {
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
		dataTable.getTableHeader().addMouseListener(mouseAdapter);
		dataTable.addMouseListener(mouseAdapter);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(dataTable);
		return scrollPane;
	}

	@Override
	protected JComponent createQueryComponent() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		// 查询面板
		JPanel managerQuery = new JPanel();
		managerQuery.setLayout(new FlowLayout());
		managerQuery.add(new JLabel("开始时间："));
		startTime = new JTextField(10);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 24);
		startTime.setText(df.format(cal.getTime()));
		managerQuery.add(startTime);
		managerQuery.add(new JLabel("结束时间："));
		endTime = new JTextField(10);
		cal.add(Calendar.MONTH, 1);
		endTime.setText(df.format(cal.getTime()));
		managerQuery.add(endTime);
		return managerQuery;
	}

	/**
	 * 工具栏
	 */
	@Override
	protected JComponent createToolbarComponent() {
		// 工具栏面板
		JPanel managerToolbar = new JPanel();
		JButton importBtn = new JButton("初始化工作日");
		importBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doWorkInit();
			}
		});
		JButton refreshBtn = new JButton("查询");
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadRecords();
			}
		});
		managerToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		managerToolbar.add(importBtn);
		managerToolbar.add(refreshBtn);
		return managerToolbar;
	}

	/**
	 * 初始化工作日
	 */
	private void doWorkInit() {
		// 获取画面开始时间和结束时间，空时不准执行导入
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date startDate = null;
		String startDateText = startTime.getText();
		if (null != startDateText && startDateText.length() > 0) {
			try {
				startDate = df.parse(startDateText);
			} catch (Exception e2) {
			}
		}
		Date endDate = null;
		String endDateText = endTime.getText();
		if (null != endDateText && endDateText.length() > 0) {
			try {
				endDate = df.parse(endDateText);
			} catch (Exception e2) {
			}
		}
		if (null == startDate || null == endDate) {
			JOptionPane.showMessageDialog(this, "开始时间和结束时间不可为空！");
			return;
		}
		service.doWorkInit(startDate, endDate);
	}

	private DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyyMMdd");
	}

	/***
	 * 加载考勤数据
	 */
	private void loadRecords() {
		workdayTableModel.setData(queryRecords());
	}

	/**
	 * 根据画面条件查询记录
	 * 
	 * @return
	 */
	private List<WorkdayEntity> queryRecords() {

		String start = startTime.getText();
		String end = endTime.getText();
		DateFormat df = getDateFormat();
		List<WorkdayEntity> entities = null;
		Date startDate = null;
		Date endDate = null;
		try {
			if (null != start && start.length() > 0) {
				startDate = df.parse(start);
			}
		} catch (ParseException e) {
		}
		try {
			if (null != end && end.length() > 0) {
				endDate = df.parse(end);
			}
		} catch (ParseException e) {
		}
		entities = service.getRecords(startDate, endDate);
		return entities;
	}

	/**
	 * 一览右键菜单
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
					List<WorkdayEntity> records = queryRecords();
					records.add(0, new WorkdayEntity());
					workdayTableModel.setData(records);
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
					List<WorkdayEntity> selects = getSelectedEntities();
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
					List<WorkdayEntity> updateList = getSelectedEntities();
					List<WorkdayEntity> insertList = new ArrayList<WorkdayEntity>(
							updateList.size());

					// 需要新增的记录放入插入集合中
					for (WorkdayEntity entity : updateList) {
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
	private List<WorkdayEntity> getSelectedEntities() {
		// 当前选中行
		int[] rows = dataTable.getSelectedRows();
		if (null == rows || rows.length == 0) {
			return null;
		}
		List<WorkdayEntity> entities = new ArrayList<WorkdayEntity>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			WorkdayEntity entity = new WorkdayEntity();
			// 日期
			entity.setWdate(String.valueOf(workdayTableModel.getValueAt(
					rows[i], 0)));
			// 是否工作日
			String status = String.valueOf(workdayTableModel.getValueAt(
					rows[i], 1));
			if ("是".equals(status)) {
				entity.setIswork("1");
			} else {
				entity.setIswork("0");
			}
			// 备注
			entity.setRemark(String.valueOf(workdayTableModel.getValueAt(
					rows[i], 2)));
			// ID
			entity.setId(String.valueOf(workdayTableModel
					.getValueAt(rows[i], 3)));
			entities.add(entity);
		}
		return entities;
	}
}
