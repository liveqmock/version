package com.win.tools.easy.attence.model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.win.tools.easy.attence.entity.UserEntity;

/**
 * 考勤汇总表
 * 
 * @author 袁晓冬
 * 
 */
public class UserTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -3453103861070796437L;
	/** 显示插件信息 */
	private static final String[] COLUMN_NAMES = new String[] { "姓名", "邮箱",
			"是否有效", "备注" };

	public UserTableModel() {
		this.columnIdentifiers = convertToVector(COLUMN_NAMES);
	}

	/**
	 * 设置一览数据
	 * 
	 * @param infos
	 */
	public void setData(List<UserEntity> entities) {
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
	public void addRow(UserEntity entity) {
		insertRow(getRowCount(), entity);
	}

	/**
	 * 插入数据
	 * 
	 * @param row
	 * @param info
	 */
	public void insertRow(int row, UserEntity entity) {
		Vector<?> rowVector = convertToVector(convertInfoToArray(entity));
		super.insertRow(row, rowVector);
	}

	/**
	 * 转换info对象为String数组
	 * 
	 * @param info
	 * @return
	 */
	public static String[] convertInfoToArray(UserEntity entity) {
		String[] rowData = new String[COLUMN_NAMES.length + 2];
		rowData[0] = entity.getName();
		rowData[1] = entity.getEmail();
		String status = entity.getStatus();
		if ("0".equals(status)) {
			rowData[2] = "否";
		} else {
			rowData[2] = "是";
		}
		rowData[3] = entity.getRemark();
		rowData[4] = entity.getId();
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
