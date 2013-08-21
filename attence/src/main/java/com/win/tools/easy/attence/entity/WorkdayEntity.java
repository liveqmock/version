package com.win.tools.easy.attence.entity;

/**
 * 用户对象
 * 
 * @author 袁晓冬
 * 
 */
public class WorkdayEntity {
	/** ID */
	private String id = "";
	/** 姓名 */
	private String wdate = null;
	/** 是否工作日1:是,0:否 */
	private String iswork = "";

	/** 备注 */
	private String remark = "";

	public String getId() {
		return id;
	}

	public String getIswork() {
		return iswork;
	}

	public String getRemark() {
		return remark;
	}

	public String getWdate() {
		return wdate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIswork(String iswork) {
		this.iswork = iswork;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setWdate(String wdate) {
		this.wdate = wdate;
	}

}
