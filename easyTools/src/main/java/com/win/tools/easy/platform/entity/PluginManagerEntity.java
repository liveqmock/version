package com.win.tools.easy.platform.entity;


/**
 * 插件管理对象
 * 
 * @author 袁晓冬
 * 
 */
public class PluginManagerEntity extends BaseEntity {
	/** 插件ID */
	private String id;
	/** 加载顺序 */
	private int loadIndex;
	/** 插件作者 */
	private String author;
	/** 插件名称 */
	private String name;
	/** 插件说明 */
	private String remark;
	/** 邮箱 */
	private String email;

	/** 插件类 */
	private String[] pluginClassList;

	/** 插件状态（启用、禁用） */
	private int status = 1;

	public String getAuthor() {
		return author;
	}

	public String getEmail() {
		return email;
	}

	public String getId() {
		return id;
	}

	public int getLoadIndex() {
		return loadIndex;
	}

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public int getStatus() {
		return status;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLoadIndex(int loadIndex) {
		this.loadIndex = loadIndex;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getPluginClassList() {
		return pluginClassList;
	}

	public void setPluginClassList(String[] pluginClassList) {
		this.pluginClassList = pluginClassList;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
