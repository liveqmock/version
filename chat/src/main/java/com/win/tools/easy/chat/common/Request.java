package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;

import com.win.tools.easy.chat.client.action.BaseClientAction;

/**
 * �������, ��ʾ�ͻ������������һ������
 * 
 * @author Ԭ����
 * 
 */
public class Request {

	/** �û�ID */
	private String userId = null;

	/** ���� */
	private Map<String, Object> parameters;

	/** ��������������Action */
	private Class<? extends Action> serverActionClass;

	/** �����Ҫ����Action����ʱ���� */
	private Request nextRequest;

	public Request nextRequest() {
		return nextRequest;
	}

	/**
	 * ���ñ����������һ��������������������ڱ�������֮��ִ����������
	 * 
	 * @param nextRequest
	 */
	public void appendRequest(Request nextRequest) {
		Request old = this.nextRequest;
		this.nextRequest = nextRequest;
		nextRequest.nextRequest = old;
	}

	/** �ͻ��˴�����ӦAction */
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
