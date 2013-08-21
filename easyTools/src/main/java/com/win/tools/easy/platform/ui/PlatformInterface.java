package com.win.tools.easy.platform.ui;

import java.awt.Component;
import java.awt.MenuComponent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDesktopPane;

import com.win.tools.easy.platform.entity.PluginManagerEntity;

/**
 * 平台接口
 * 
 * @author 袁晓冬
 * 
 */
public interface PlatformInterface {
	/**
	 * 添加菜单
	 * 
	 * @param menu
	 * @param listener
	 * @param path
	 * @return Component
	 */
	public Component addMenu(String menu, ActionListener listener, String path);

	/**
	 * 添加系统托盘菜单
	 * 
	 * @param menu
	 * @param listener
	 * @param path
	 * @return
	 */
	public MenuComponent addTrayMenu(String menu, ActionListener listener);

	/**
	 * 添加工具栏按钮
	 * 
	 * @param name
	 * @param listener
	 */
	public Component addToolbarBtn(String name, ActionListener listener);

	/**
	 * 获取系统插件列表
	 * 
	 * @return
	 */
	public List<PluginManagerEntity> getPluginManagerEntities();

	/**
	 * 获取主控件
	 * 
	 * @return
	 */
	public PlatFormFrame getPlatFormFrame();

	/**
	 * 获取桌面控件
	 * 
	 * @return
	 */
	public JDesktopPane getMainDesktopPane();

	/**
	 * 创建桌面窗口
	 * 
	 * @return
	 */
	public InternalFrame createInternalFrame();

	/**
	 * 设置日志窗口是否显示
	 * 
	 * @param visible
	 */
	public void setLogVisible(boolean visible);
}
