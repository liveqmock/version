package com.win.tools.easy.platform.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

import org.apache.log4j.Logger;

import com.win.tools.easy.common.PersitentManager;
import com.win.tools.easy.platform.PlatFormConfig;

/**
 * main frame
 * 
 * @author 袁晓冬
 * 
 */
public class PlatFormFrame extends JFrame {
	/** serial */
	private static final long serialVersionUID = -2531797098986719700L;
	/** 日志记录 */
	private static final Logger LOGGER = Logger.getLogger(PlatFormFrame.class);
	/** application title */
	private static String TITLE = "EasyTools";
	/** 主窗口标题 */
	public final static String MAIN_WIN_TITLE = "日志";
	/** 系统托盘 */
	private TrayIcon trayIcon = null;

	/**
	 * 构造函数
	 */
	protected PlatFormFrame() {
		PersitentManager manager = PersitentManager.load();
		PlatFormConfig config = manager.get(PlatFormConfig.class);
		if (null == config) {
			config = new PlatFormConfig();
		}
		/** 窗口图标 */
		URL resource = getClass().getResource("trayIcon.gif");
		ImageIcon icon = new ImageIcon(resource);
		setIconImage(icon.getImage());
		// 设置窗口标题
		setTitle(TITLE);
		if (null == config.getFrameBounds()) {
			// 初始化大小
			setSize(800, 600);
		} else {
			this.setBounds(config.getFrameBounds());
		}
		this.setExtendedState(config.getState());
		// 初始化菜单栏
		setJMenuBar(FrameFactory.getFrameMenuBar());
		// 初始化工具栏
		getContentPane()
				.add(FrameFactory.getFrameToolBar(), BorderLayout.NORTH);
		// 设置内部窗体拖动模式
		JDesktopPane desktopPane = FrameFactory.getMainDesktopPane();
		this.getContentPane().add(desktopPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// 窗口事件处理
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				setVisible(false);
			}

			@Override
			public void windowClosing(WindowEvent e) {
				saveStatus();
			}
		});
		/**
		 * 记录窗口状态
		 */
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				PersitentManager manager = PersitentManager.load();
				PlatFormConfig config = manager.get(PlatFormConfig.class);
				config.setFrameBounds(e.getComponent().getBounds());
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				PersitentManager manager = PersitentManager.load();
				PlatFormConfig config = manager.get(PlatFormConfig.class);
				config.setFrameBounds(e.getComponent().getBounds());
			}
		});
		/** 添加系统托盘图标 */
		if (SystemTray.isSupported()) {
			if (trayIcon == null) {
				trayIcon = new TrayIcon(icon.getImage(), "EasyTools",
						FrameFactory.getTrayMenu());
				trayIcon.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						int count = e.getClickCount();
						// 双击打开处理
						if (count > 1 && e.getButton() == MouseEvent.BUTTON1) {
							setVisible(true);
							setExtendedState(Frame.NORMAL);
						}
					};
				});
			}
			try {
				SystemTray.getSystemTray().add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
		/** 创建日志窗口 */
		TextInternalFrame logWindow = FrameFactory.getLogWindow();
		try {
			logWindow.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		/**
		 * 设置字体
		 */
		Font font = new Font("微软雅黑", Font.PLAIN, 12);
		initGlobalFontSetting(font);
	}

	/**
	 * 保存窗口状态
	 */
	public void saveStatus() {
		PersitentManager manager = PersitentManager.load();
		PlatFormConfig config = manager.get(PlatFormConfig.class);
		config.setState(this.getExtendedState());
		manager.put(config);
		manager.save();
	}

	/**
	 * 设置全局字体
	 * 
	 * @param font
	 */
	private void initGlobalFontSetting(Font font) {
		FontUIResource fontUIResource = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys
				.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontUIResource);
			}
		}
	}
}
