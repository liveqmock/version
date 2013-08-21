package com.win.tools.easy.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 列信息
 * 
 * @author 袁晓冬
 * 
 */
public class ColumnInfo {
	public static ColumnInfo getColumnInfoFromResultSet(ResultSet rs)
			throws SQLException {
		ColumnInfo info = new ColumnInfo();
		// 序号
		info.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
		// column
		info.setColumnName(rs.getString("COLUMN_NAME"));
		// remarks
		info.setRemarks(rs.getString("REMARKS"));
		// type(int)
		info.setDataType(rs.getInt("DATA_TYPE"));
		// type name
		info.setTypeName(rs.getString("TYPE_NAME"));
		// column size
		info.setColumnSize(rs.getInt("COLUMN_SIZE"));
		// null?
		info.setNullable(rs.getInt("NULLABLE"));
		return info;
	}

	/**
	 * 根据RS获取列信息列表
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static List<ColumnInfo> getColumnInfosFromResultSet(ResultSet rs)
			throws SQLException {
		rs.beforeFirst();
		List<ColumnInfo> infoList = new ArrayList<ColumnInfo>();
		while (rs.next()) {
			infoList.add(getColumnInfoFromResultSet(rs));
		}
		return infoList;
	}

	/**
	 * 根据RS中检索的主键信息更新infos列信息
	 * 
	 * @param infos
	 * @param rs
	 * @throws SQLException
	 */
	public static void addPK(List<ColumnInfo> infos, ResultSet rs)
			throws SQLException {
		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			short keySeq = rs.getShort("KEY_SEQ");
			for (ColumnInfo info : infos) {
				if (info.getColumnName().equals(columnName)) {
					info.setPk(keySeq);
					break;
				}
			}
		}
	}

	private int ordinalPosition;
	private String columnName;
	private String remarks;
	private int pk;

	private int dataType;

	private String typeName;

	private int columnSize;
	private int nullable;

	public String getColumnName() {
		return columnName;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public int getDataType() {
		return dataType;
	}

	public int getNullable() {
		return nullable;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}

	public int getPk() {
		return pk;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	public void setOrdinalPosition(int ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
