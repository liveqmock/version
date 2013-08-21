package com.win.tools.easy.plugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.springframework.util.StringUtils;

import com.win.tools.easy.formatter.DDLFormatter;
import com.win.tools.easy.formatter.SqlFormatter;
import com.win.tools.easy.platform.PlatformPlugin;
import com.win.tools.easy.platform.PluginInfo;
import com.win.tools.easy.platform.ui.InternalFrame;
import com.win.tools.easy.platform.ui.PlatformInterface;
import com.win.tools.easy.platform.ui.TextInternalFrame;

/**
 * 平台初始化插件
 * 
 * @author 袁晓冬
 * 
 */
public class PlatformInitPlugin implements PlatformPlugin {
	private PlatformInterface platformInterface;

	@Override
	public void start(PlatformInterface platformInterface) {
		this.platformInterface = platformInterface;
		createMenu();
		createToolbar();
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
		JMenuItem item = new JMenuItem("打开窗口");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		item.addActionListener(getCreateActionListener());
		menu.add(item);
		item = new JMenuItem("关闭所有窗口");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				platformInterface.closeAllFrame();
			}
		});
		menu.add(item);
		item = new JMenuItem("退出");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(item);
		menuBar.add(menu);
		/** 格式化-菜单 */
		menu = new JMenu("格式化");
		item = new JMenuItem("SQL");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SqlFormatter format = new SqlFormatter();
				String sql = platformInterface.getActiveFrame().getData()
						.toString();
				sql = format.format(sql);
				platformInterface.getActiveFrame().setData(sql);
			}
		});
		menu.add(item);
		item = new JMenuItem("DLL");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DDLFormatter format = new DDLFormatter();
				String sql = platformInterface.getActiveFrame().getData()
						.toString();
				sql = format.format(sql);
				platformInterface.getActiveFrame().setData(sql);
			}
		});
		menu.add(item);
		menuBar.add(menu);
		/** 帮助-菜单 */
		menu = new JMenu("帮助");
		item = new JMenuItem("关于");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showInternalMessageDialog(menuBar,
						"作者：袁晓冬\nEmail:yuanxd@sy.liandisys.com.cn", "关于",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(item);
		item = new JMenuItem("插件管理");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<PluginInfo> pluginInfo = platformInterface.getPluginInfo();
				InternalFrame frame = platformInterface.createInternalFrame();
				FrameFactory.getInstence().setPluginDetailFrame(pluginInfo,
						frame, platformInterface);
				frame.setTitle("插件信息");
				frame.setVisible(true);
			}
		});
		menu.add(item);
		menuBar.add(menu);
	}

	/**
	 * 工具栏设置
	 */
	private void createToolbar() {
		JToolBar toolBar = platformInterface.getPlatformToolbar();
		toolBar.setFloatable(true);
		JButton createBtn = new JButton("打开窗口");
		createBtn.addActionListener(getCreateActionListener());
		toolBar.add(createBtn);
	}

	/**
	 * 创建窗口
	 * 
	 * @return
	 */
	private ActionListener getCreateActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TextInternalFrame frame = platformInterface
						.createTextInternalFrame();
				String title = JOptionPane.showInputDialog(
						platformInterface.getApplicationFrame(), "窗口名称", "窗口-"
								+ frame.getId());
				if (!StringUtils.hasLength(title)) {
					title = "窗口-" + frame.getId();
				}
				frame.setTitle(title);
				frame.setVisible(true);
				try {
					frame.setSelected(true);
				} catch (PropertyVetoException e1) {
				}
			}
		};
	}
}
