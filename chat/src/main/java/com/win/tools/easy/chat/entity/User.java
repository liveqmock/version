package com.win.tools.easy.chat.entity;

import java.io.Serializable;
import java.net.Socket;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * �û�����ÿ���ͻ������ӷ����������ڴ��д��һ���û�����
 * 
 * @author Ԭ����
 * 
 */
public class User implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5292150168794431921L;

	// ��ҵ�Ψһ��ʶ
	private String id;
	/** �ǳ� */
	private String nickName;

	// �������
	private String name;

	private String password;

	/** socket���� */
	@XStreamOmitField
	private Socket socket;

	public User() {

	}

	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public String getPassword() {
		return password;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
