package com.win.tools.easy.attence.entity;

/**
 * 请假对象
 * 
 * @author 袁晓冬
 * 
 */
public class LeaveEntity {
	/** ID */
	private String id = "";
	/** 员工名 */
	private String name = "";
	/** 请假类型 */
	private String leaveType = "";
	/** 请假日期 */
	private String leaveDate = "";
	/** 开始时间 */
	private String startTime = "";
	/** 结束时间 */
	private String endTime = "";
	/** 备注 */
	private String remark = "";

	public String getEndTime() {
		return endTime;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getId() {
		return id;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

}
