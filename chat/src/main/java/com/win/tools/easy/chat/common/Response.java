package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.win.tools.easy.chat.client.action.BaseClientAction;

/**
 * 服务器响应对象
 * 
 * @author 袁晓冬
 * 
 */
public class Response {

	// 服务器返回的各个数值
	private Map<String, Object> datas;
	/** 响应是否已发送 */
	@XStreamOmitField
	private boolean send = false;

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	// 错误代码
	private int status;
	// 客户端处理类, 该值保存在Request的请求参数的Map中
	// 服务器处理类
	private Class<? extends BaseClientAction> clientActionClass;

	/**
	 * 根据客户端Action构造响应
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
