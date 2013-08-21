package com.win.tools.easy.attence.common;

import java.awt.Color;
import java.io.Serializable;

/**
 * 考勤配置对象
 * 
 * @author 袁晓冬
 * 
 */
public class AttenceConfig implements Serializable {
	/** 序列号 */
	private static final long serialVersionUID = -7427834890028843684L;
	/** 数据库连接url */
	private String connectionURL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
	/** 数据库连接用户名 */
	private String connectionUser = "sa";
	/** 数据库连接密码 */
	private String connectionPassword = "";
	/** 上班时间 */
	private String startTime = "0830";
	/** 下班时间 */
	private String endTime = "1730";
	/** 调休开始时间 */
	private String leaveStartTime = "0830";
	/** 调休结束时间 */
	private String leaveEndTime = "1800";
	/** 异常考勤背景色 */
	private Color attenceExColor = Color.CYAN;
	/** 上次导入文件路径 */
	private String lastImportFile = "";

	public Color getAttenceExColor() {
		return attenceExColor;
	}

	public String getConnectionPassword() {
		return connectionPassword;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public String getConnectionUser() {
		return connectionUser;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getLastImportFile() {
		System.err.println("getLastImportFile:"+lastImportFile);
		return lastImportFile;
	}

	public String getLeaveEndTime() {
		return leaveEndTime;
	}

	public String getLeaveStartTime() {
		return leaveStartTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setAttenceExColor(Color attenceExColor) {
		this.attenceExColor = attenceExColor;
	}

	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public void setConnectionUser(String connectionUser) {
		this.connectionUser = connectionUser;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setLastImportFile(String lastImportFile) {
		System.err.println("setLastImportFile:"+lastImportFile);
		this.lastImportFile = lastImportFile;
	}

	public void setLeaveEndTime(String leaveEndTime) {
		this.leaveEndTime = leaveEndTime;
	}

	public void setLeaveStartTime(String leaveStartTime) {
		this.leaveStartTime = leaveStartTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "AttenceConfig [connectionURL=" + connectionURL
				+ ", connectionUser=" + connectionUser
				+ ", connectionPassword=" + connectionPassword + ", startTime="
				+ startTime + ", endTime=" + endTime + ", leaveStartTime="
				+ leaveStartTime + ", leaveEndTime=" + leaveEndTime + "]";
	}
}
