package com.win.tools.easy.attence.entity;

/**
 * 每日考勤对象
 * 
 * @author 袁晓冬
 * 
 */
public class AttenceEntity {
	// "姓名", "日期", "上班时间", "下班时间", "备注"
	private String id = "";
	private String userName = "";
	private String day = "";
	private String startTime = "";
	private String endTime = "";
	private String status = "";
	private String iswork = "";
	/** 记录状态-正常 */
	public final static String STATUS_ZC = "1";
	/** 节假日上班 */
	public final static String STATUS_JJR = "2";

	/** 记录状态-旷工 */
	public final static String STATUS_KG = "5";

	/** 记录状态-迟到 */
	public final static String STATUS_CD = "6";
	/** 记录状态-早退 */
	public final static String STATUS_ZT = "7";
	// 备注
	private String remark = "";
	public boolean isJJRWork() {
		return STATUS_JJR.equals(status);
	}
	public String getDay() {
		return day;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getId() {
		return id;
	}

	public String getIswork() {
		return iswork;
	}

	public String getRemark() {
		return remark;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStatus() {
		return status;
	}

	public String getUserName() {
		return userName;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
