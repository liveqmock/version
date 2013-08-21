package com.win.tools.easy.platform.plugin;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.win.tools.easy.common.PersitentManager;
import com.win.tools.easy.platform.PlatFormConfig;
import com.win.tools.easy.platform.entity.PluginManagerEntity;
import com.win.tools.easy.platform.model.PluginManagerTableModel;
import com.win.tools.easy.platform.ui.BasePanel;
import com.win.tools.easy.platform.ui.FrameStart;

/**
 * 插件管理面板
 * 
 * @author 袁晓冬
 * 
 */
public class PluginManagerPanel extends BasePanel<PluginManagerEntity> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1834392029731284266L;

	public PluginManagerPanel() {
		initBorderLayout();
	}

	@Override
	protected JComponent createToolbarComponent() {
		// 构造工具栏
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(getDisableBtn());
		panel.add(getEnableBtn());
		JLabel label = new JLabel("状态改变后重启程序生效，system插件不可禁用!");
		label.setForeground(Color.red);
		panel.add(label);
		return panel;
	}

	/**
	 * 获取禁用按钮
	 * 
	 * @param table
	 * @return
	 */
	JButton getDisableBtn() {
		JButton jButton = new JButton("禁用");
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rowIndexs = dataTable.getSelectedRows();
				int statusIndex = dataTable.getColumn("状态").getModelIndex();
				int idIndex = dataTable.getColumn("插件ID").getModelIndex();
				for (int index : rowIndexs) {
					String id = String.valueOf(dataTable.getValueAt(index,
							idIndex));
					if (id.startsWith("system")) {
						continue;
					}
					dataTable.setValueAt("禁用", index, statusIndex);
				}
				List<String> disablePlugins = new ArrayList<String>();
				for (int i = 0; i < dataTable.getRowCount(); i++) {
					if ("禁用".equals(dataTable.getValueAt(i, statusIndex))) {
						String id = String.valueOf(dataTable.getValueAt(i,
								idIndex));
						if ("system".equals(id)) {
							continue;
						}
						disablePlugins.add(String.valueOf(dataTable.getValueAt(
								i, idIndex)));
						FrameStart.stopPlugin(id);
					}
				}
				PersitentManager manager = PersitentManager.load();
				PlatFormConfig config = manager.get(PlatFormConfig.class);
				config.setDisablePlugins(disablePlugins);
				manager.save();
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
	JButton getEnableBtn() {
		JButton jButton = new JButton("启用");
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rowIndexs = dataTable.getSelectedRows();
				int statusIndex = dataTable.getColumn("状态").getModelIndex();
				int idIndex = dataTable.getColumn("插件ID").getModelIndex();
				for (int index : rowIndexs) {
					dataTable.setValueAt("启用", index, statusIndex);
				}
				List<String> disablePlugins = new ArrayList<String>();
				for (int i = 0; i < dataTable.getRowCount(); i++) {
					if ("禁用".equals(dataTable.getValueAt(i, statusIndex))) {
						String id = String.valueOf(dataTable.getValueAt(i,
								idIndex));
						if ("system".equals(id)) {
							continue;
						}
						disablePlugins.add(String.valueOf(dataTable.getValueAt(
								i, idIndex)));
					}
				}
				PersitentManager manager = PersitentManager.load();
				PlatFormConfig config = manager.get(PlatFormConfig.class);
				config.setDisablePlugins(disablePlugins);
				manager.save();
			}
		});
		return jButton;
	}

	@Override
	protected JComponent createCenterComponent() {
		baseTableModel = new PluginManagerTableModel();
		// 数据一览
		dataTable = new JTable(baseTableModel) {
			/** 序列号 */
			private static final long serialVersionUID = -2471831198768136302L;

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
		dataTable.setRowHeight(22);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(dataTable);
		return scrollPane;
	}

}
