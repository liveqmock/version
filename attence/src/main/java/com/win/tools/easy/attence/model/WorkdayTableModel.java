package com.win.tools.easy.attence.model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.win.tools.easy.attence.entity.WorkdayEntity;

/**
 * 工作日模型
 * 
 * @author 袁晓冬
 * 
 */
public class WorkdayTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -3453103861070796437L;
	/** 显示插件信息 */
	private static final String[] COLUMN_NAMES = new String[] { "日期", "是否工作日",
			"备注" };

	public WorkdayTableModel() {
		this.columnIdentifiers = convertToVector(COLUMN_NAMES);
	}

	/**
	 * 设置一览数据
	 * 
	 * @param infos
	 */
	public void setData(List<WorkdayEntity> entities) {
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
	public void addRow(WorkdayEntity entity) {
		insertRow(getRowCount(), entity);
	}

	/**
	 * 插入数据
	 * 
	 * @param row
	 * @param info
	 */
	public void insertRow(int row, WorkdayEntity entity) {
		Vector<?> rowVector = convertToVector(convertInfoToArray(entity));
		super.insertRow(row, rowVector);
	}

	/**
	 * 转换info对象为String数组
	 * 
	 * @param info
	 * @return
	 */
	public static String[] convertInfoToArray(WorkdayEntity entity) {
		String[] rowData = new String[COLUMN_NAMES.length + 2];
		rowData[0] = entity.getWdate();
		String isWork = entity.getIswork();
		if ("0".equals(isWork)) {
			rowData[1] = "否";
		} else {
			rowData[1] = "是";
		}
		rowData[2] = entity.getRemark();
		rowData[3] = entity.getId();
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
