package com.win.tools.easy.chat.client;

import java.io.Serializable;

/**
 * �ͻ�������
 * 
 * @author Ԭ����
 * 
 */
public class ClientConfig implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3085207871232454814L;
	/** ������IP��ַ */
	private String serverIp = "172.16.124.176";
	/** �������˿ں� */
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
