package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;

import com.win.tools.easy.chat.client.action.BaseClientAction;

/**
 * 请求对象, 表示客户端向服务器的一次请求
 * 
 * @author 袁晓冬
 * 
 */
public class Request {

	/** 用户ID */
	private String userId = null;

	/** 参数 */
	private Map<String, Object> parameters;

	/** 服务器处理请求Action */
	private Class<? extends Action> serverActionClass;

	/** 如果需要后续Action处理时设置 */
	private Request nextRequest;

	public Request nextRequest() {
		return nextRequest;
	}

	/**
	 * 设置本次请求的下一个请求，如果其他请求，则将在本次请求之后执行其他请求
	 * 
	 * @param nextRequest
	 */
	public void appendRequest(Request nextRequest) {
		Request old = this.nextRequest;
		this.nextRequest = nextRequest;
		nextRequest.nextRequest = old;
	}

	/** 客户端处理响应Action */
	private Class<? extends BaseClientAction> clientActionClass;

	public Request(Class<? extends Action> serverActionClass) {
		this.serverActionClass = serverActionClass;
		this.parameters = new HashMap<String, Object>();
		this.clientActionClass = null;
	}

	public Request(Class<? extends Action> serverActionClass,
			Class<? extends BaseClientAction> clientAction) {
		this.serverActionClass = serverActionClass;
		this.parameters = new HashMap<String, Object>();
		this.clientActionClass = clientAction;
	}

	public Class<? extends BaseClientAction> getClientActionClass() {
		return clientActionClass;
	}

	public Object getParameter(String key) {
		return this.parameters.get(key);
	}

	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public Class<? extends Action> getServerActionClass() {
		return serverActionClass;
	}

	public String getUserId() {
		return userId;
	}

	public void setClientActionClass(
			Class<? extends BaseClientAction> clientActionClass) {
		this.clientActionClass = clientActionClass;
	}

	public void setParameter(String key, Object value) {
		this.parameters.put(key, value);
	}

	public void setServerActionClass(
			Class<? extends Action> serverActionClass) {
		this.serverActionClass = serverActionClass;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
