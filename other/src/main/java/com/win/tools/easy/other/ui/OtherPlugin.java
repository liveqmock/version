package com.win.tools.easy.other.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.win.tools.easy.platform.PlatformPlugin;
import com.win.tools.easy.platform.ui.FrameFactory;
import com.win.tools.easy.platform.ui.InternalFrame;
import com.win.tools.easy.platform.ui.PlatformInterface;

public class OtherPlugin implements PlatformPlugin {
	private PlatformInterface platformInterface;
	private ActionListener formatShowListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if ("格式化".equals(command)) {
				InternalFrame frame = platformInterface.createInternalFrame();
				frame.setTitle("格式化");
				FormatFrame otherFrame = new FormatFrame(frame);
				otherFrame.show();
			} else if ("编码转换".equals(command)) {
				InternalFrame frame = platformInterface.createInternalFrame();
				frame.setTitle("编码转换");
				ChangeContentTypesFrame otherFrame = new ChangeContentTypesFrame(
						frame);
				otherFrame.show();

			}

		}
	};

	@Override
	public void start() {
		platformInterface = FrameFactory.getPlatformInterface();
		createMenu();
	}

	@Override
	public void stop() {

	}

	/**
	 * 菜单设置
	 */
	private void createMenu() {
		platformInterface.addMenu("其他", null, null);
		platformInterface.addMenu("格式化", formatShowListener, "其他");
		platformInterface.addMenu("编码转换", formatShowListener, "其他");
	}

}
