package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.win.tools.easy.chat.client.action.BaseClientAction;

/**
 * ��������Ӧ����
 * 
 * @author Ԭ����
 * 
 */
public class Response {

	// ���������صĸ�����ֵ
	private Map<String, Object> datas;
	/** ��Ӧ�Ƿ��ѷ��� */
	@XStreamOmitField
	private boolean send = false;

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	// �������
	private int status;
	// �ͻ��˴�����, ��ֵ������Request�����������Map��
	// ������������
	private Class<? extends BaseClientAction> clientActionClass;

	/**
	 * ���ݿͻ���Action������Ӧ
	 * 
	 * @param clientActionClass
	 */
	public Response(Class<? extends BaseClientAction> clientActionClass) {
		this.datas = new HashMap<String, Object>();
		this.clientActionClass = clientActionClass;
	}

	public Response(Request request) {
		this.datas = new HashMap<String, Object>();
		this.clientActionClass = request.getClientActionClass();
		Map<String, Object> parameters = request.getParameters();
		for (String key : parameters.keySet()) {
			setData(key, parameters.get(key));
		}
	}

	public void setData(String key, Object value) {
		this.datas.put(key, value);
	}

	public Object getData(String key) {
		return this.datas.get(key);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Class<? extends BaseClientAction> getClientActionClass() {
		return clientActionClass;
	}

	public void setClientActionClass(
			Class<? extends BaseClientAction> clientActionClass) {
		this.clientActionClass = clientActionClass;
	}
}
