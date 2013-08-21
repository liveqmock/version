package com.win.tools.easy.attence.entity;

/**
 * 用户对象
 * 
 * @author 袁晓冬
 * 
 */
public class UserEntity {
	/** ID */
	private String id = "";
	/** 姓名 */
	private String name = "";
	/** 记录状态 */
	private String status = "";
	/** 邮箱 */
	private String email = "";

	/** 备注 */
	private String remark = "";

	public String getEmail() {
		return email;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public String getStatus() {
		return status;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
