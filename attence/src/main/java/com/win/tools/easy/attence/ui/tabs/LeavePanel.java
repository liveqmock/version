package com.win.tools.easy.attence.ui.tabs;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.win.tools.easy.attence.common.ExcelOperator;
import com.win.tools.easy.attence.entity.LeaveEntity;
import com.win.tools.easy.attence.model.LeaveTableModel;
import com.win.tools.easy.attence.service.LeaveService;

/**
 * 人员面板
 * 
 * @author 袁晓冬
 * 
 */
public class LeavePanel extends BasePanel {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5277091691196219230L;

	/** 考勤列表 */
	private LeaveTableModel leaveTableModel = null;
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
	/** 查询面板-人员名 */
	private JTextField userName = null;
	/** 查询面板-开始时间 */
	private JTextField startTime = null;
	/** 查询面板-结束时间 */
	private JTextField endTime = null;
	/** 考勤服务类 */
	private LeaveService service = null;

	public LeavePanel() {
		service = new LeaveService();
		initBorderLayout();
	}

	/**
	 * 工具栏面板
	 */
	@Override
	protected JPanel createToolbarComponent() {
		// 工具栏面板
		JPanel managerToolbar = new JPanel();
		JButton exportBtn = new JButton("导出");
		exportBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exportData();
			}
		});
		JButton refreshBtn = new JButton("查询");
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadAttenceRecord();
			}
		});
		managerToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		managerToolbar.add(exportBtn);
		managerToolbar.add(refreshBtn);
		return managerToolbar;
	}

	/**
	 * 导出文件
	 */
	private void exportData() {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter fileFilter = new FileNameExtensionFilter("EXCEL文件", "xls");
		fileChooser.setFileFilter(fileFilter);
		int returnVal = fileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".xls")) {
				file = new File(file.getParent(), file.getName() + ".xls");
			}
			toExcel(file);
		}
	}

	/**
	 * 将Excel写入文件
	 * 
	 * @param file
	 */
	private void toExcel(File file) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet ws = wb.createSheet("Sheet1");
		Object data[][] = this.leaveTableModel.getExcelData();
		fillWorkSheet(wb, ws, data);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (Exception exception2) {
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
		}
		return;
	}

	/**
	 * 设置Excel导出内容
	 * 
	 * @param wb
	 * @param ws
	 * @param data
	 */
	private void fillWorkSheet(HSSFWorkbook wb, HSSFSheet ws, Object data[][]) {
		ws.setDefaultColumnWidth(12);
		// 列名
		String[] columns = { "姓名", "请假类型", "开始日期", "开始时间", "结束日期", "结束时间" };
		int[] dataCol = { 0, 1, 2, 3, 2, 4 };
		int columnWidth[] = new int[columns.length];
		HSSFCellStyle cellStyle = wb.createCellStyle();
		ExcelOperator.setDataCellStyle(wb, cellStyle);
		HSSFCellStyle cellYellowStyle = wb.createCellStyle();
		ExcelOperator.setDataCellYellowStyle(wb, cellYellowStyle);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		ExcelOperator.setHeaderStyle(wb, headerStyle);
		HSSFRow row = null;
		HSSFCell cell = null;
		row = ws.createRow(0);
		for (int i = 0; i < columns.length; i++) {
			int len = 0;
			cell = row.createCell(i);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString(columns[i]));
			len = getStringLength(columns[i]);
			if (len > columnWidth[i])
				columnWidth[i] = len;

		}
		for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
			row = ws.createRow(rowIndex + 1);
			for (int i = 0; i < columns.length; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(cellStyle);
				String value = data[rowIndex][dataCol[i]] != null ? data[rowIndex][dataCol[i]]
						.toString() : "";
				cell.setCellValue(new HSSFRichTextString(value));
				int len = getStringLength(value);
				if (len > columnWidth[i])
					columnWidth[i] = len;
			}
		}
		for (int i = 0; i < columns.length; i++) {
			if (columnWidth[i] > 80)
				columnWidth[i] = 80;
			short width = (short) (300 * columnWidth[i]);
			ws.setColumnWidth(i, width);
		}
	}

	protected static int getStringLength(String value) {
		int len = 0;
		if (value != null)
			len = value.getBytes().length;
		return len;
	}

	/**
	 * 设置查询面板
	 */
	@Override
	protected JPanel createQueryComponent() {
		DateFormat df = getDateFormat();
		// 查询面板
		JPanel managerQuery = new JPanel();
		managerQuery.setLayout(new FlowLayout());
		managerQuery.add(new JLabel("人员名称："));
		userName = new JTextField(10);
		managerQuery.add(userName);
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

	@Override
	protected JComponent createCenterComponent() {
		/** render渲染控制 */
		final DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -1138422127695925811L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}
		};
		leaveTableModel = new LeaveTableModel();
		// 数据一览
		dataTable = new JTable(leaveTableModel) {
			/** 序列号 */
			private static final long serialVersionUID = -2471831198768136302L;
			// 请假类型下拉框
			private TableCellEditor comboxCellEditor = new DefaultCellEditor(
					new JComboBox(new String[] { "调休", "请假", "病假", "市内出差",
							"国内出差" }));

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
					JPopupMenu popupMenu = getAttencePopupMenu();
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

	/**
	 * 考勤面板右键菜单
	 * 
	 * @return
	 */
	private JPopupMenu getAttencePopupMenu() {
		if (null == tablePopupMenu) {
			tablePopupMenu = new JPopupMenu();
			popupInsertItem = new JMenuItem("追加");
			popupInsertItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<LeaveEntity> records = queryRecords();
					records.add(0, new LeaveEntity());
					leaveTableModel.setData(records);
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
					List<LeaveEntity> selects = getSelectedEntities();
					service.deleteRecords(selects);
					loadAttenceRecord();
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
					List<LeaveEntity> updateList = getSelectedEntities();
					List<LeaveEntity> insertList = new ArrayList<LeaveEntity>(
							updateList.size());

					// 需要新增的记录放入插入集合中
					for (LeaveEntity entity : updateList) {
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
					loadAttenceRecord();
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
	private List<LeaveEntity> getSelectedEntities() {
		// 当前选中行
		int[] rows = dataTable.getSelectedRows();
		if (null == rows || rows.length == 0) {
			return null;
		}
		List<LeaveEntity> entities = new ArrayList<LeaveEntity>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			LeaveEntity entity = new LeaveEntity();
			// 用户名
			entity.setName(String.valueOf(leaveTableModel
					.getValueAt(rows[i], 0)));
			String leaveType = String.valueOf(leaveTableModel.getValueAt(
					rows[i], 1));
			if ("请假".equals(leaveType)) {
				entity.setLeaveType("2");
			} else if ("病假".equals(leaveType)) {
				entity.setLeaveType("3");
			} else if ("市内出差".equals(leaveType)) {
				entity.setLeaveType("4");
			} else if ("国内出差".equals(leaveType)) {
				entity.setLeaveType("5");
			} else {
				entity.setLeaveType("1");
			}
			entity.setLeaveDate(String.valueOf(leaveTableModel.getValueAt(
					rows[i], 2)));
			entity.setStartTime(String.valueOf(leaveTableModel.getValueAt(
					rows[i], 3)));
			entity.setEndTime(String.valueOf(leaveTableModel.getValueAt(
					rows[i], 4)));
			entity.setRemark(String.valueOf(leaveTableModel.getValueAt(rows[i],
					5)));
			entity.setId(String.valueOf(leaveTableModel.getValueAt(rows[i], 6)));
			entities.add(entity);
		}
		return entities;
	}

	/***
	 * 加载考勤数据
	 */
	private void loadAttenceRecord() {
		leaveTableModel.setData(queryRecords());
	}

	/**
	 * 根据画面条件查询记录
	 * 
	 * @return
	 */
	private List<LeaveEntity> queryRecords() {
		List<LeaveEntity> entities = null;
		String startTimeStr = startTime.getText();
		Date start = null;
		if (null != startTimeStr && startTimeStr.length() > 0) {
			try {
				start = getDateFormat().parse(startTimeStr);
			} catch (ParseException e) {
			}
		}
		String endTimeStr = endTime.getText();
		Date end = null;
		if (null != endTimeStr && endTimeStr.length() > 0) {
			try {
				end = getDateFormat().parse(endTimeStr);
			} catch (ParseException e) {
			}
		}
		entities = service.getRecords(start, end, userName.getText());
		return entities;
	}

	private DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyyMMdd");
	}
}
