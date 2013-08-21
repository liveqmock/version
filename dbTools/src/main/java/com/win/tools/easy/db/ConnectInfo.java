package com.win.tools.easy.db;

/**
 * 数据库连接信息
 * 
 * @author 袁晓冬
 * 
 */
public class ConnectInfo {
	/** 驱动 */
	String driver;
	/** 连接URL */
	String url;
	/** 用户名 */
	String user;
	/** 密码 */
	String password;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
