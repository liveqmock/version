package com.win.tools.easy.attence.model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.win.tools.easy.attence.entity.AttenceEntity;

/**
 * 考勤汇总表
 * 
 * @author 袁晓冬
 * 
 */
public class AttenceTableModel extends DefaultTableModel {

	/**
	 * serialVersion
	 */
	private static final long serialVersionUID = -3453103861070796437L;
	/** 显示插件信息 */
	private static final String[] COLUMN_NAMES = new String[] { "姓名", "日期",
			"上班时间", "下班时间", "备注" };

	public AttenceTableModel() {
		this.columnIdentifiers = convertToVector(COLUMN_NAMES);
	}

	/**
	 * 设置一览数据
	 * 
	 * @param infos
	 */
	public void setData(List<AttenceEntity> entities) {
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
	public void addRow(AttenceEntity entity) {
		insertRow(getRowCount(), entity);
	}

	/**
	 * 插入数据
	 * 
	 * @param row
	 * @param info
	 */
	public void insertRow(int row, AttenceEntity entity) {
		Vector<?> rowVector = convertToVector(convertInfoToArray(entity));
		super.insertRow(row, rowVector);
	}

	public Object[][] getExcelData() {
		Object[][] data = new Object[dataVector.size()][COLUMN_NAMES.length + 2];
		for (int i = 0; i < dataVector.size(); i++) {
			@SuppressWarnings("rawtypes")
			Vector rowVector = (Vector) dataVector.elementAt(i);
			for (int j = 0; j < rowVector.size(); j++) {
				data[i][j] = rowVector.elementAt(j);
			}
		}
		return data;
	}

	/**
	 * 转换info对象为String数组
	 * 
	 * @param info
	 * @return
	 */
	public static String[] convertInfoToArray(AttenceEntity entity) {
		String[] rowData = new String[COLUMN_NAMES.length + 2];
		rowData[0] = entity.getUserName();
		rowData[1] = entity.getDay();
		rowData[2] = entity.getStartTime();
		rowData[3] = entity.getEndTime();
		rowData[4] = entity.getRemark();
		rowData[5] = entity.getStatus();
		rowData[6] = entity.getId();
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
