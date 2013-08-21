package com.win.tools.easy.chat.server;

import java.io.Serializable;

/**
 * 聊天配置对象
 * 
 * @author 袁晓冬
 * 
 */
public class ServiceConfig implements Serializable {
	/** 序列号 */
	private static final long serialVersionUID = 7484611814742265627L;
	/** 数据库连接url */
	private String connectionURL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
	/** 数据库连接用户名 */
	private String connectionUser = "sa";
	/** 数据库连接密码 */
	private String connectionPassword = "";
	/** 服务端口号 */
	private int serverPort = 23071;

	public String getConnectionPassword() {
		return connectionPassword;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public String getConnectionUser() {
		return connectionUser;
	}

	public int getServerPort() {
		return serverPort;
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

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public String toString() {
		return "ServiceConfig [connectionURL=" + connectionURL
				+ ", connectionUser=" + connectionUser
				+ ", connectionPassword=" + connectionPassword
				+ ", serverPort=" + serverPort + "]";
	}

}
