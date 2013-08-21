package com.win.tools.easy.attence.ui.tabs;

import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.StringUtils;

import com.win.tools.easy.attence.common.AttenceConfig;
import com.win.tools.easy.attence.common.ExcelOperator;
import com.win.tools.easy.attence.entity.AttenceEntity;
import com.win.tools.easy.attence.model.AttenceTableModel;
import com.win.tools.easy.attence.service.AttenceService;
import com.win.tools.easy.attence.service.UserService;
import com.win.tools.easy.common.PersitentManager;

/**
 * 考勤面板
 * 
 * @author 袁晓冬
 * 
 */
public class AttencePanel extends BasePanel {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1507222210979198657L;
	/** 考勤列表 */
	private AttenceTableModel attencedataModel = null;
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
	/** 右键菜单-请假 */
	private JMenuItem popupLeaveItem = null;
	/** 查询面板-人员名称 */
	private JTextField userName = null;
	/** 查询面板-开始时间 */
	private JTextField startTime = null;
	/** 查询面板-结束时间 */
	private JTextField endTime = null;
	/** 考勤服务类 */
	private AttenceService service = null;
	/** 用户服务 */
	private UserService userService = null;

	public AttencePanel() {
		service = new AttenceService();
		userService = new UserService();
		initBorderLayout();
	}

	/**
	 * 工具栏面板
	 */
	@Override
	protected JPanel createToolbarComponent() {
		// 工具栏面板
		JPanel managerToolbar = new JPanel();
		JButton importBtn = new JButton("导入");
		importBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
					JOptionPane.showMessageDialog(AttencePanel.this,
							"开始时间和结束时间不可为空！");
					return;
				}
				PersitentManager manager = PersitentManager.load();
				AttenceConfig config = manager.get(AttenceConfig.class);
				String lastPath = config.getLastImportFile();
				JFileChooser chooser = null;
				if (StringUtils.hasLength(lastPath)) {
					chooser = new JFileChooser(lastPath);
				} else {
					chooser = new JFileChooser();
				}

				int returnVal = chooser.showOpenDialog(AttencePanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					config.setLastImportFile(file.getAbsolutePath());
					manager.save();
					List<AttenceEntity> entities = service
							.getAttenceEntitiesFromFile(file);
					List<String> unSavedNames = service
							.getUnSavedUser(entities);
					if (null != unSavedNames && unSavedNames.size() > 0) {
						// 自动保存用户
						if (!userService.fastSaveUser(unSavedNames)) {
							JOptionPane.showMessageDialog(AttencePanel.this,
									"保存用户失败！");
							return;
						}
					}
					service.calcAttence(entities, startDate, endDate);
					service.insertRecords(entities);
					loadRecords();
				}
			}
		});
		JButton exportBtn = new JButton("导出");
		exportBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exportData();
			}
		});
		JButton setEndTimeBtn = new JButton("调整下班时间");
		setEndTimeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
				service.setEndTime(startDate, endDate, userName.getText());
				loadRecords();
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
		managerToolbar.add(exportBtn);
		managerToolbar.add(setEndTimeBtn);
		managerToolbar.add(refreshBtn);

		JButton sendByEmail = new JButton("邮件确认");
		sendByEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String pre = JOptionPane.showInputDialog("确认信息：");

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
				service.mailConfirm(startDate, endDate, userName.getText(), pre);
				loadRecords();
			}
		});
		managerToolbar.add(sendByEmail);
		return managerToolbar;
	}

	/**
	 * 设置查询面板
	 */
	@Override
	protected JPanel createQueryComponent() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
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
				AttenceConfig config = PersitentManager.load().get(
						AttenceConfig.class);
				String rowStatus = String.valueOf(attencedataModel.getValueAt(
						row, 5));
				if (AttenceEntity.STATUS_KG.equals(rowStatus)) {
					setBackground(config.getAttenceExColor());
				} else if (AttenceEntity.STATUS_CD.equals(rowStatus)) {
					setBackground(config.getAttenceExColor());
				} else if (AttenceEntity.STATUS_ZT.equals(rowStatus)) {
					setBackground(config.getAttenceExColor());
				} else if (AttenceEntity.STATUS_JJR.equals(rowStatus)) {
					setBackground(Color.YELLOW);
				} else {
					setBackground(Color.WHITE);
				}
				setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}
		};
		attencedataModel = new AttenceTableModel();
		// 数据一览
		dataTable = new JTable(attencedataModel) {
			/** 序列号 */
			private static final long serialVersionUID = -2471831198768136302L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				return render;
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
						popupMenu.add(popupLeaveItem);
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
	private JPopupMenu getTablePopupMenu() {
		if (null == tablePopupMenu) {
			tablePopupMenu = new JPopupMenu();
			popupInsertItem = new JMenuItem("追加");
			popupInsertItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<AttenceEntity> records = queryRecords();
					records.add(0, new AttenceEntity());
					attencedataModel.setData(records);
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
					List<AttenceEntity> selects = getSelectedEntities();
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
					List<AttenceEntity> updateList = getSelectedEntities();
					List<AttenceEntity> insertList = new ArrayList<AttenceEntity>(
							updateList.size());

					// 需要新增的记录放入插入集合中
					for (AttenceEntity entity : updateList) {
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
			popupLeaveItem = new JMenuItem("请假");
			popupLeaveItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// 当前选中的entity
					List<AttenceEntity> selects = getSelectedEntities();
					service.leaveRecords(selects, "1");
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
	private List<AttenceEntity> getSelectedEntities() {
		// 当前选中行
		int[] rows = dataTable.getSelectedRows();
		if (null == rows || rows.length == 0) {
			return null;
		}
		List<AttenceEntity> entities = new ArrayList<AttenceEntity>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			AttenceEntity entity = new AttenceEntity();
			// 用户名
			entity.setUserName(String.valueOf(attencedataModel.getValueAt(
					rows[i], 0)));
			entity.setDay(String.valueOf(attencedataModel
					.getValueAt(rows[i], 1)));
			entity.setStartTime(String.valueOf(attencedataModel.getValueAt(
					rows[i], 2)));
			entity.setEndTime(String.valueOf(attencedataModel.getValueAt(
					rows[i], 3)));
			entity.setRemark(String.valueOf(attencedataModel.getValueAt(
					rows[i], 4)));
			entity.setStatus(String.valueOf(attencedataModel.getValueAt(
					rows[i], 5)));
			entity.setId(String.valueOf(attencedataModel.getValueAt(rows[i], 6)));
			entities.add(entity);
		}
		return entities;
	}

	/***
	 * 加载考勤数据
	 */
	private void loadRecords() {
		attencedataModel.setData(queryRecords());
	}

	/**
	 * 根据画面条件查询记录
	 * 
	 * @return
	 */
	private List<AttenceEntity> queryRecords() {
		String start = startTime.getText();
		String end = endTime.getText();
		DateFormat df = getDateFormat();
		List<AttenceEntity> entities = null;
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
		entities = service.getRecords(startDate, endDate, userName.getText());
		return entities;
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
	 * 设置Excel导出内容
	 * 
	 * @param wb
	 * @param ws
	 * @param data
	 */
	private void fillWorkSheet(HSSFWorkbook wb, HSSFSheet ws, Object data[][]) {
		ws.setDefaultColumnWidth(12);
		// 列名
		String[] columns = { "姓名", "日期", "上班时间", "下班时间", "备注" };
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
				String status = data[rowIndex][columns.length].toString();
				if (AttenceEntity.STATUS_JJR.equals(status)) {
					cell.setCellStyle(cellYellowStyle);
				} else {
					cell.setCellStyle(cellStyle);
				}
				String value = data[rowIndex][i] != null ? data[rowIndex][i]
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

	/**
	 * 将Excel写入文件
	 * 
	 * @param file
	 */
	private void toExcel(File file) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet ws = wb.createSheet("Sheet1");
		Object data[][] = this.attencedataModel.getExcelData();
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

	protected static int getStringLength(String value) {
		int len = 0;
		if (value != null)
			len = value.getBytes().length;
		return len;
	}

	/**
	 * 日期格式化
	 * 
	 * @return
	 */
	private DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyyMMdd");
	}
}
