package com.win.tools.easy.platform.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import org.springframework.util.StringUtils;

import com.win.tools.easy.platform.entity.PluginManagerEntity;

/**
 * 平台接口实现
 * 
 * @author 袁晓冬
 * 
 */
public class PlatformImpl implements PlatformInterface {

	@Override
	public Component addMenu(String menuName, ActionListener listener,
			String path) {
		// 父组件设置为菜单栏
		Component parentComp = FrameFactory.getFrameMenuBar();
		Component ret = null;
		// 如果上级名称存在，设置父组件为菜单栏下此控件
		if (StringUtils.hasLength(path)) {
			parentComp = getMenuComponentByName(path, parentComp);
		}
		// 当上级控件为菜单栏时
		if (parentComp instanceof JMenuBar) {
			JMenuBar menuBar = (JMenuBar) parentComp;
			JMenu menuComp = new JMenu(menuName);
			if (null != listener)
				menuComp.addActionListener(listener);
			menuBar.add(menuComp);
			ret = menuComp;
		} else if (parentComp instanceof JMenu) {
			// 当上级控件为菜单时
			JMenu pMenu = (JMenu) parentComp;
			// 事件存在时创建为MenuItem
			if (listener != null) {
				JMenuItem item = new JMenuItem(menuName);
				item.addActionListener(listener);
				pMenu.add(item);
				ret = item;
			} else {
				JMenu menu = new JMenu(menuName);
				pMenu.add(menu);
				ret = menu;
			}
		} else {
		}
		// 解决添加菜单后菜单不可见问题
		FrameFactory.getPlatFormFrame().setVisible(true);
		ret.setName(menuName);
		return ret;
	}

	private Component getMenuComponentByName(String text, Component comp) {
		if (text.equals(comp.getName())) {
			return comp;
		}
		if (comp instanceof Container) {
			Container ct = (Container) comp;
			for (Component subComponent : ct.getComponents()) {
				Component result = getMenuComponentByName(text, subComponent);
				if (null != result) {
					return getMenuComponentByName(text, subComponent);
				}
			}
		}
		return null;
	}

	@Override
	public List<PluginManagerEntity> getPluginManagerEntities() {
		return FrameFactory.getPlugins();
	}

	@Override
	public PlatFormFrame getPlatFormFrame() {
		return FrameFactory.getPlatFormFrame();
	}

	@Override
	public JDesktopPane getMainDesktopPane() {
		return FrameFactory.getMainDesktopPane();
	}

	@Override
	public InternalFrame createInternalFrame() {
		InternalFrame window = new InternalFrame() {
			/**
			 * serialVersionUID
			 */
			private static final long serialVersionUID = 8205910852888859525L;

			@Override
			public void doDefaultCloseAction() {
				this.setVisible(false);
			}
		};

		JInternalFrame[] allFrames = FrameFactory.getMainDesktopPane()
				.getAllFrames();
		int titleBarHight = 30 * allFrames.length;
		int pos = 10 + titleBarHight;
		window.setBounds(pos, pos, InternalFrame.DEFAULT_WITH,
				InternalFrame.DEFAULT_HEIGHT);
		window.setVisible(false);
		FrameFactory.getMainDesktopPane().add(window);
		try {
			window.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return window;
	}

	@Override
	public MenuComponent addTrayMenu(String menu, ActionListener listener) {
		// 父组件设置为菜单栏
		PopupMenu popupMenu = FrameFactory.getTrayMenu();
		MenuItem item = new MenuItem(menu);
		if (null != listener)
			item.addActionListener(listener);
		popupMenu.add(item);
		item.setName(menu);
		return item;
	}

	@Override
	public void setLogVisible(boolean visible) {
		FrameFactory.getLogWindow().setVisible(visible);
	}

	@Override
	public Component addToolbarBtn(String name, ActionListener listener) {
		JToolBar toolBar = FrameFactory.getFrameToolBar();
		JButton btn = new JButton(name);
		if (null != listener) {
			btn.addActionListener(listener);
		}
		toolBar.add(btn);
		return btn;
	}

}
