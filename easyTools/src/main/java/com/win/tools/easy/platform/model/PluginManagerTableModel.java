package com.win.tools.easy.platform.model;

import com.win.tools.easy.platform.entity.PluginManagerEntity;

/**
 * 插件管理列表数据模型
 * 
 * @author 袁晓冬
 * 
 */
public class PluginManagerTableModel extends
		BaseTableModel<PluginManagerEntity> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6490321614428173863L;

	/** 一览列名 */
	private static final String[] COLUMN_NAMES = new String[] { "插件ID", "插件名称",
			"作者", "邮箱", "状态", "备注" };

	@Override
	protected String[] getColumnNames() {
		return COLUMN_NAMES;
	}

	@Override
	public String[] convertInfoToArray(PluginManagerEntity entity) {
		String[] rowData = new String[getColumnNames().length + 2];
		int index = 0;
		rowData[index++] = entity.getId();
		rowData[index++] = entity.getName();
		rowData[index++] = entity.getAuthor();
		rowData[index++] = entity.getEmail();
		int status = entity.getStatus();
		if (status > 0) {
			rowData[index++] = "启用";
		} else {
			rowData[index++] = "禁用";
		}
		rowData[index++] = entity.getRemark();
		return rowData;
	}

}
