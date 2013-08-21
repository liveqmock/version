package com.win.tools.easy.attence.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import com.win.tools.easy.attence.common.AttenceConfig;
import com.win.tools.easy.common.PersitentManager;
import com.win.tools.easy.platform.PlatformPlugin;
import com.win.tools.easy.platform.ui.FrameFactory;
import com.win.tools.easy.platform.ui.FrameStart;
import com.win.tools.easy.platform.ui.InternalFrame;
import com.win.tools.easy.platform.ui.PlatformInterface;

/**
 * 考勤插件
 * 
 * @author 袁晓冬
 * 
 */
public class AttencePlugin implements PlatformPlugin {
	private static final Logger LOGGER = Logger.getLogger(AttencePlugin.class);
	private PlatformInterface platformInterface;
	private ActionListener attenceShowListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			InternalFrame frame = platformInterface.createInternalFrame();
			frame.setTitle("考勤管理");
			AttenceFrame attenceFrame = new AttenceFrame(frame);
			attenceFrame.show();
		}
	};

	@Override
	public void start() {
		platformInterface = FrameFactory.getPlatformInterface();
		createMenu();
		createToolbar();
	}

	private void createToolbar() {
		platformInterface.addToolbarBtn("考勤主页", attenceShowListener);
	}

	@Override
	public void stop() {
		PersitentManager manager = PersitentManager.load();
		manager.remove(AttenceConfig.class.toString());
		manager.save();
		LOGGER.debug("plugin is stop");
	}

	/**
	 * 菜单设置
	 */
	private void createMenu() {
		platformInterface.addMenu("考勤", null, null);
		platformInterface.addMenu("主页面", attenceShowListener, "考勤");
	}

	public static void main(String[] args) {
		FrameStart.start();
	}
}
