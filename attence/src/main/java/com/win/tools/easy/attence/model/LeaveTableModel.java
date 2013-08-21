package com.win.tools.easy.attence.model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.win.tools.easy.attence.entity.LeaveEntity;

/**
 * 考勤汇总表
 * 
 * @author 袁晓冬
 * 
 */
public class LeaveTableModel extends DefaultTableModel {

	/**
	 * serialVersion
	 */
	private static final long serialVersionUID = -3453103861070796437L;
	/** 一览列名 */
	private static final String[] COLUMN_NAMES = new String[] { "姓名", "请假类型",
			"请假日期", "开始时间", "结束时间", "备注" };

	public LeaveTableModel() {
		this.columnIdentifiers = convertToVector(COLUMN_NAMES);
	}

	/**
	 * 导出Excel格式数据
	 * @return
	 */
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
	 * 设置一览数据
	 * 
	 * @param infos
	 */
	public void setData(List<LeaveEntity> entities) {
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
	public void addRow(LeaveEntity entity) {
		insertRow(getRowCount(), entity);
	}

	/**
	 * 插入数据
	 * 
	 * @param row
	 * @param info
	 */
	public void insertRow(int row, LeaveEntity entity) {
		Vector<?> rowVector = convertToVector(convertInfoToArray(entity));
		super.insertRow(row, rowVector);
	}

	/**
	 * 转换info对象为String数组
	 * 
	 * @param info
	 * @return
	 */
	public static String[] convertInfoToArray(LeaveEntity entity) {
		String[] rowData = new String[COLUMN_NAMES.length + 2];
		int index = 0;
		rowData[index++] = entity.getName();
		String leaveType = entity.getLeaveType();
		if ("2".equals(leaveType)) {
			rowData[index++] = "请假";
		} else if ("3".equals(leaveType)) {
			rowData[index++] = "病假";
		} else if ("4".equals(leaveType)) {
			rowData[index++] = "市内出差";
		} else if ("5".equals(leaveType)) {
			rowData[index++] = "国内出差";
		} else {
			rowData[index++] = "调休";
		}
		rowData[index++] = entity.getLeaveDate();
		rowData[index++] = entity.getStartTime();
		rowData[index++] = entity.getEndTime();
		rowData[index++] = entity.getRemark();
		rowData[index++] = entity.getId();
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
