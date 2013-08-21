package com.win.tools.easy.chat.entity;

import java.io.Serializable;
import java.net.Socket;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 用户对象，每个客户端连接服务器后在内存中存放一个用户对象
 * 
 * @author 袁晓冬
 * 
 */
public class User implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5292150168794431921L;

	// 玩家的唯一标识
	private String id;
	/** 昵称 */
	private String nickName;

	// 玩家名称
	private String name;

	private String password;

	/** socket链接 */
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
