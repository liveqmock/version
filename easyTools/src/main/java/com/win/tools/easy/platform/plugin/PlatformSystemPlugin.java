package com.win.tools.easy.platform.plugin;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import com.win.tools.easy.platform.PlatformPlugin;
import com.win.tools.easy.platform.entity.PluginManagerEntity;
import com.win.tools.easy.platform.ui.FrameFactory;
import com.win.tools.easy.platform.ui.InternalFrame;
import com.win.tools.easy.platform.ui.PlatformInterface;

/**
 * 平台初始化插件
 * 
 * @author 袁晓冬
 * 
 */
public class PlatformSystemPlugin implements PlatformPlugin {

	private PlatformInterface platformInterface;
	/** 插件管理列表画面 */
	private InternalFrame puginFrame = null;

	@Override
	public void start() {
		platformInterface = FrameFactory.getPlatformInterface();
		createMenu();
		createTrayMenu();
	}

	private void createTrayMenu() {
		platformInterface.addTrayMenu("open", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				platformInterface.getPlatFormFrame().setVisible(true);
				platformInterface.getPlatFormFrame().setExtendedState(
						Frame.NORMAL);
			}
		});
		platformInterface.addTrayMenu("exit", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				platformInterface.getPlatFormFrame().saveStatus();
				System.exit(0);
			}
		});
	}

	/**
	 * 菜单设置
	 */
	private void createMenu() {
		platformInterface.addMenu("帮助", null, null);
		/** 菜单-文件 */
		platformInterface.addMenu("关于", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showInternalMessageDialog(
						platformInterface.getMainDesktopPane(),
						"作者：袁晓冬\nEmail:yuanxd@sy.liandisys.com.cn", "关于",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}, "帮助");
		platformInterface.addMenu("插件管理", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (null == puginFrame) {
					puginFrame = platformInterface.createInternalFrame();
					puginFrame.setTitle("插件信息");
					PluginManagerPanel pluginManagerPanel = new PluginManagerPanel();
					puginFrame.getContentPane().add(pluginManagerPanel,
							BorderLayout.CENTER);
					List<PluginManagerEntity> pluginManagerEntities = platformInterface
							.getPluginManagerEntities();
					pluginManagerPanel.loadRecords(pluginManagerEntities);
				}
				puginFrame.setVisible(true);
			}
		}, "帮助");
		platformInterface.addMenu("日志", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				platformInterface.setLogVisible(true);
			}
		}, "帮助");
	}

	@Override
	public void stop() {

	}
}
