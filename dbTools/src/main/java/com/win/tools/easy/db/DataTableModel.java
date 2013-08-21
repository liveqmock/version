package com.win.tools.easy.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel {
	private ResultSet resultSet = null;
	List<String[]> dataCache = new ArrayList<String[]>();
	private static final int INIT_SIZE = 30;
	private static final int PAGE_SIZE = 10;
	// 总行数
	private int rowCount = 0;
	// 列数
	private int columnCount = 0;
	// 列名
	private String[] columnName = null;

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public String getColumnName(int column) {
		if (null == columnName) {
			return null;
		}
		return columnName[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			while (rowIndex >= dataCache.size() && rowIndex < getRowCount()) {
				if (resultSet.next()) {
					int count = getColumnCount();
					String[] data = new String[count];
					for (int i = 0; i < count; i++) {
						data[i] = resultSet.getString(i + 1);
					}
					dataCache.add(data);
				} else {
					try {
						resultSet.close();
					} catch (Exception e) {
					}
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataCache.get(rowIndex)[columnIndex];
	}

	/**
	 * 关闭老的ResultSet，并使用新的ResultSet刷新数据
	 * 
	 * @param resultSet
	 */
	public void setResultSet(ResultSet resultSet) {
		dataCache.clear();
		if (null != this.resultSet) {
			try {
				this.resultSet.close();
			} catch (SQLException e) {
			}
		}
		this.resultSet = resultSet;
		try {
			this.resultSet.last();
			rowCount = this.resultSet.getRow();
			columnCount = resultSet.getMetaData().getColumnCount();
			columnName = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columnName[i] = resultSet.getMetaData().getColumnName(i + 1);
			}
			this.resultSet.beforeFirst();
			int count = getColumnCount();
			while (INIT_SIZE > dataCache.size() && resultSet.next()) {
				String[] data = new String[count];
				for (int i = 0; i < count; i++) {
					data[i] = resultSet.getString(i + 1);
				}
				dataCache.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		fireTableStructureChanged();
	}
}
