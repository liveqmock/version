package com.win.tools.easy.db;

import java.util.List;

import javax.swing.table.DefaultTableModel;

/**
 * 数据库表列表
 * 
 * @author 袁晓冬
 * 
 */
public class ColumnListModel extends DefaultTableModel {
	/**
	 * serialVersion
	 */
	private static final long serialVersionUID = 9165759194560468333L;
	/** 显示插件信息 */
	private static final String[] COLUMN_NAMES = new String[] { "Column",
			"Remarks", "PK", "Type", "Size", "Nullable" };

	public ColumnListModel() {
		this.columnIdentifiers = convertToVector(COLUMN_NAMES);
	}

	/**
	 * 设置一览数据
	 * 
	 * @param infos
	 */
	public void setData(List<ColumnInfo> infos) {
		String[][] datas = new String[infos.size()][];
		for (int i = 0; i < infos.size(); i++) {
			datas[i] = convertInfoToArray(infos.get(i));
		}
		this.dataVector = convertToVector(datas);
		fireTableStructureChanged();
	}

	/**
	 * 添加数据
	 * 
	 * @param info
	 */
	public void addRow(ColumnInfo info) {
		insertRow(getRowCount(), info);
	}

	/**
	 * 插入数据
	 * 
	 * @param row
	 * @param info
	 */
	public void insertRow(int row, ColumnInfo info) {
		insertRow(row, convertInfoToArray(info));
	}

	/**
	 * 转换info对象为String数组
	 * 
	 * @param info
	 * @return
	 */
	public static String[] convertInfoToArray(ColumnInfo info) {
		String[] rowData = new String[COLUMN_NAMES.length];
		rowData[0] = info.getColumnName();
		rowData[1] = info.getRemarks();
		rowData[2] = String.valueOf(info.getPk());
		rowData[3] = info.getTypeName();
		rowData[4] = String.valueOf(info.getColumnSize());
		rowData[5] = String.valueOf(info.getNullable());
		return rowData;
	}

	/**
	 * 清空列表
	 */
	public void clear() {
		int rowCount = getRowCount();
		dataVector.removeAllElements();
		fireTableRowsDeleted(0, rowCount);
	}
}
