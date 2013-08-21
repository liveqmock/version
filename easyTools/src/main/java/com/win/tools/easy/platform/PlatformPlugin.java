package com.win.tools.easy.platform;

/**
 * 平台插件接口
 * 
 * @author 袁晓冬
 * 
 */
public interface PlatformPlugin {

	/**
	 * 平台调用，平台加载插件处理
	 * 
	 */
	void start();

	/**
	 * 平台调用，卸载插件时处理
	 * 
	 */
	void stop();
}
