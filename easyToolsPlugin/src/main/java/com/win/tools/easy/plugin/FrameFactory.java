package com.win.tools.easy.plugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.win.tools.easy.platform.PluginInfo;
import com.win.tools.easy.platform.ui.InternalFrame;
import com.win.tools.easy.platform.ui.PlatformInterface;

/**
 * 插件相信信息窗口
 * 
 * @author 袁晓冬
 * 
 */
public class FrameFactory {
	private final static FrameFactory instence = createInstence();

	/**
	 * 创建单例对象
	 * 
	 * @return
	 */
	private static FrameFactory createInstence() {
		return new FrameFactory();
	}

	/**
	 * 禁止外部创建对象
	 */
	private FrameFactory() {

	}

	/**
	 * 获取单例对象
	 * 
	 * @return
	 */
	public static FrameFactory getInstence() {
		return instence;
	}

	/**
	 * 获取禁用按钮
	 * 
	 * @param table
	 * @return
	 */
	JButton getDisableBtn(final JTable table,
			final PlatformInterface platformInterface) {
		JButton jButton = new JButton("禁用");
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rowIndexs = table.getSelectedRows();
				int statusIndex = table.getColumn("状态").getModelIndex();
				int idIndex = table.getColumn("插件ID").getModelIndex();
				for (int index : rowIndexs) {
					String id = String.valueOf(table.getValueAt(index, idIndex));
					if("systemInit".equals(id)) {
						continue;
					}
					table.setValueAt("禁用", index, statusIndex);
				}

				/***
				 * 保存当前插件状态到配置文件
				 */
				Properties properties = platformInterface.getAppProperties();
				StringBuffer disablePlugins = new StringBuffer(";");
				for (int i = 0; i < table.getRowCount(); i++) {
					if ("禁用".equals(table.getValueAt(i, statusIndex))) {
						String id = String.valueOf(table.getValueAt(i, idIndex));
						if("systemInit".equals(id)) {
							continue;
						}
						disablePlugins.append(table.getValueAt(i, idIndex));
						disablePlugins.append(";");
					}
				}
				properties.setProperty("disablePlugins",
						disablePlugins.toString());
				platformInterface.saveAppProperties(properties);
			}
		});
		return jButton;
	}

	/**
	 * 获取启用按钮
	 * 
	 * @param table
	 * @return
	 */
	JButton getEnableBtn(final JTable table,
			final PlatformInterface platformInterface) {
		JButton jButton = new JButton("启用");
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rowIndexs = table.getSelectedRows();
				int statusIndex = table.getColumn("状态").getModelIndex();
				int idIndex = table.getColumn("插件ID").getModelIndex();
				for (int index : rowIndexs) {
					table.setValueAt("启用", index, statusIndex);
				}
				/***
				 * 保存当前插件状态到配置文件
				 */
				Properties properties = platformInterface.getAppProperties();
				StringBuffer disablePlugins = new StringBuffer(";");
				for (int i = 0; i < table.getRowCount(); i++) {
					if ("禁用".equals(table.getValueAt(i, statusIndex))) {
						disablePlugins.append(table.getValueAt(i, idIndex));
						disablePlugins.append(";");
					}
				}
				properties.setProperty("disablePlugins",
						disablePlugins.toString());
				platformInterface.saveAppProperties(properties);
			}
		});
		return jButton;
	}

	/**
	 * 构造插件详细信息窗口
	 * 
	 * @param pluginInfo
	 * @param frame
	 */
	public void setPluginDetailFrame(List<PluginInfo> pluginInfo,
			InternalFrame frame, PlatformInterface platformInterface) {
		// 显示插件信息
		String[] columnNames = new String[] { "插件ID", "插件名称", "作者", "状态" };
		String[][] tableValues = new String[pluginInfo.size()][columnNames.length];
		for (int i = 0; i < pluginInfo.size(); i++) {
			tableValues[i][0] = pluginInfo.get(i).getId();
			tableValues[i][1] = pluginInfo.get(i).getName();
			tableValues[i][2] = pluginInfo.get(i).getAuthor();
			tableValues[i][3] = pluginInfo.get(i).getStatus() > 0 ? "启用" : "禁用";
		}
		JTable table = new JTable(tableValues, columnNames) {
			private static final long serialVersionUID = 1L;

			/**
			 * 设置单元格只读
			 */
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			/**
			 * 居中显示
			 */
			@Override
			public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
				DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super
						.getDefaultRenderer(columnClass);
				cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return cr;
			}
		};
		JScrollPane pane = new JScrollPane(table);
		frame.getContentPane().add(pane, BorderLayout.CENTER);
		// 构造工具栏
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(getDisableBtn(table, platformInterface));
		panel.add(getEnableBtn(table, platformInterface));
		JLabel label = new JLabel("状态改变后重启程序生效，systemInit插件不可禁用!");
		label.setForeground(Color.red);
		panel.add(label);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
	}
}
