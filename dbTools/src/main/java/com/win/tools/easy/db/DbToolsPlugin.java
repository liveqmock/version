package com.win.tools.easy.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import com.win.tools.easy.platform.PlatformPlugin;
import com.win.tools.easy.platform.PluginInfo;
import com.win.tools.easy.platform.ui.Manager;
import com.win.tools.easy.platform.ui.PlatformInterface;

public class DbToolsPlugin implements PlatformPlugin {

	private PlatformInterface platformInterface;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PluginInfo info = new PluginInfo();
		info.setAuthor("author1");
		info.setId("dbTools");
		info.setLoadIndex(10);
		info.setName("DbToolsPlugin");
		info.setStatus(1);
		List<Class<?>> classList = new ArrayList<Class<?>>();
		classList.add(DbToolsPlugin.class);
		info.setPluginClassList(classList);
		Manager.getInstance().startToolsWithPlugin(info);
	}

	@Override
	public void start(PlatformInterface platformInterface) {
		this.platformInterface = platformInterface;
		createMenu();
		createToolbar();
		createTraymenu();
	}

	@Override
	public void stop(PlatformInterface platformInterface) {

	}

	/**
	 * 菜单设置
	 */
	private void createMenu() {
		final JMenuBar menuBar = platformInterface.getPlatformMenuBar();
		/** 新建-菜单 */
		JMenu menu = new JMenu("文件");
		JMenuItem item = new JMenuItem("打开连接");
		final JDialog dialog = ComponentFactory.getInstance()
				.createOpenConnectionDialog(platformInterface);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(true);
			}
		});
		menu.add(item);
		item = new JMenuItem("关闭连接");
		menu.add(item);
		item = new JMenuItem("退出");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(item);
		menuBar.add(menu);

		JMenu viewMenu = new JMenu("视图");
		JMenuItem options = new JMenuItem("选项");
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComponentFactory.getInstance()
						.createOptionDialog(platformInterface).setVisible(true);
			}
		});
		viewMenu.add(options);
		menuBar.add(viewMenu);
	}

	/**
	 * 工具栏设置
	 */
	private void createToolbar() {
		JToolBar toolBar = platformInterface.getPlatformToolbar();
		toolBar.setFloatable(true);
		JButton createBtn = new JButton("打开连接");
		createBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComponentFactory.getInstance()
						.createOpenConnectionDialog(platformInterface)
						.setVisible(true);
			}
		});
		toolBar.add(createBtn);
	}

	/**
	 * 系统托盘图标设置
	 */
	private void createTraymenu() {
	}
}
