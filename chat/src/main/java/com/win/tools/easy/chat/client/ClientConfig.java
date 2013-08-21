package com.win.tools.easy.chat.client;

import java.io.Serializable;

/**
 * 客户端配置
 * 
 * @author 袁晓冬
 * 
 */
public class ClientConfig implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3085207871232454814L;
	/** 服务器IP地址 */
	private String serverIp = "172.16.124.176";
	/** 服务器端口号 */
	private int port = 13071;

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
