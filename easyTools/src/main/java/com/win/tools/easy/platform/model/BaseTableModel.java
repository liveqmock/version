package com.win.tools.easy.platform.model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.win.tools.easy.platform.entity.BaseEntity;

/**
 * Model基类
 * 
 * @author 袁晓冬
 * 
 */
public abstract class BaseTableModel<T extends BaseEntity> extends
		DefaultTableModel {

	/**
	 * serialVersion
	 */
	private static final long serialVersionUID = -3453103861070796437L;

	public BaseTableModel() {
		this.columnIdentifiers = convertToVector(getColumnNames());
	}

	protected abstract String[] getColumnNames();

	/**
	 * 设置一览数据
	 * 
	 * @param infos
	 */
	public void setData(List<T> entities) {
		if (null == entities) {
			return;
		}
		String[][] datas = new String[entities.size()][];
		for (int i = 0; i < entities.size(); i++) {
			datas[i] = convertInfoToArray(entities.get(i));
		}
		this.dataVector = convertToVector(datas);
		fireTableStructureChanged();
	}

	/**
	 * 添加数据
	 * 
	 * @param info
	 */
	public void addRow(T entity) {
		insertRow(getRowCount(), entity);
	}

	/**
	 * 插入数据
	 * 
	 * @param row
	 * @param info
	 */
	public void insertRow(int row, T entity) {
		Vector<?> rowVector = convertToVector(convertInfoToArray(entity));
		super.insertRow(row, rowVector);
	}

	/**
	 * 转换info对象为String数组
	 * 
	 * @param info
	 * @return
	 */
	public abstract String[] convertInfoToArray(T entity);

	/**
	 * 清空列表
	 */
	public void clear() {
		int rowCount = getRowCount();
		dataVector.removeAllElements();
		fireTableRowsDeleted(0, rowCount);
	}
}
