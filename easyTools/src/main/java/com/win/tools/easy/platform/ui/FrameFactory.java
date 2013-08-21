package com.win.tools.easy.platform.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import com.win.tools.easy.platform.entity.PluginManagerEntity;

/**
 * 框架工厂
 * 
 * @author 袁晓冬
 * 
 */
public class FrameFactory {
	private static final Logger LOGGER = Logger.getLogger(FrameFactory.class);
	/** 主画面 */
	private static PlatFormFrame platFormFrame = null;
	/** 日志窗口 */
	private static TextInternalFrame logWindow = null;
	/** 桌面面板 */
	private static JDesktopPane desktopPane = null;
	/** 主窗口菜单栏面板 */
	private static JMenuBar menuBar = null;
	/** 主窗口工具栏面板 */
	private static JToolBar toolBar = null;
	/** 状态栏菜单 */
	private static PopupMenu trayMenu = null;
	/** 插件列表 */
	private static List<PluginManagerEntity> plugins = null;
	/** 平台接口 */
	private static PlatformInterface instance = new PlatformImpl();

	/**
	 * 获取框架菜单栏
	 * 
	 * @return
	 */
	protected static JMenuBar getFrameMenuBar() {
		if (null == menuBar) {
			menuBar = new JMenuBar();
		}
		return menuBar;
	}

	/**
	 * 获取框架工具栏
	 * 
	 * @return
	 */
	protected static JToolBar getFrameToolBar() {
		if (null == toolBar)
			toolBar = new JToolBar();
		return toolBar;
	}

	/**
	 * 创建日志窗口
	 * 
	 * @return
	 */
	protected static TextInternalFrame getLogWindow() {
		if (logWindow == null) {
			logWindow = new TextInternalFrame("log", false);
			JButton button = new JButton("清空");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					logWindow.content.setText("");
				}
			});
			/** 日志窗口工具栏 */
			JPanel toolbarPanel = new JPanel();
			toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			toolbarPanel.add(button);
			logWindow.add(toolbarPanel, BorderLayout.NORTH);
			logWindow.setBounds(5, 5, InternalFrame.DEFAULT_WITH,
					InternalFrame.DEFAULT_HEIGHT);
			logWindow.setClosable(true);
			getMainDesktopPane().add(logWindow);
		}
		return logWindow;
	}

	/**
	 * 获取桌面面板
	 * 
	 * @return
	 */
	protected static JDesktopPane getMainDesktopPane() {
		if (desktopPane == null) {
			desktopPane = new JDesktopPane();
			desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		}
		return desktopPane;
	}

	/**
	 * 创建主画面
	 * 
	 * @return
	 */
	protected static PlatFormFrame getPlatFormFrame() {
		if (platFormFrame == null) {
			LOGGER.debug("create new PlatFormFrame!");
			platFormFrame = new PlatFormFrame();
		}
		return platFormFrame;
	}

	public static List<PluginManagerEntity> getPlugins() {
		return plugins;
	}

	/**
	 * 创建系统托盘菜单
	 * 
	 * @return
	 */
	protected static PopupMenu getTrayMenu() {
		if (null == trayMenu)
			trayMenu = new PopupMenu();
		return trayMenu;
	}

	protected static void setPlugins(List<PluginManagerEntity> plugins) {
		FrameFactory.plugins = plugins;
	}

	/**
	 * 获取单例对象
	 * 
	 * @return
	 */
	public static PlatformInterface getPlatformInterface() {
		return instance;
	}
}
