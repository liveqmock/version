package com.win.tools.easy.platform;

import java.awt.Frame;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

/**
 * 框架配置信息
 * 
 * @author 袁晓冬
 * 
 */
public class PlatFormConfig implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5191317282418655577L;
	/**
	 * 窗口大小位置
	 */
	private Rectangle frameBounds = null;

	/**
	 * 插件禁用列表
	 */
	private List<String> disablePlugins = null;

	/**
	 * 窗口状态（最大化、最小化、正常等）
	 */
	private int state = Frame.NORMAL;

	public List<String> getDisablePlugins() {
		return disablePlugins;
	}

	public Rectangle getFrameBounds() {
		return frameBounds;
	}

	public int getState() {
		return state;
	}

	public void setDisablePlugins(List<String> disablePlugins) {
		this.disablePlugins = disablePlugins;
	}

	public void setFrameBounds(Rectangle frameBounds) {
		this.frameBounds = frameBounds;
	}

	public void setState(int state) {
		this.state = state;
	}
}
