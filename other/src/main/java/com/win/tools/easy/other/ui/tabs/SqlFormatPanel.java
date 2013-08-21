package com.win.tools.easy.other.ui.tabs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.win.tools.easy.other.formatter.SqlFormatter;

/**
 * SQL格式化面板
 * 
 * @author 袁晓冬
 * 
 */
public class SqlFormatPanel extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -132242811842079588L;
	private JTextPane sqlTA = null;
	private SqlFormatter formatter = null;

	public SqlFormatPanel() {
		setLayout(new BorderLayout());
		add(createToolbar(), BorderLayout.NORTH);
		add(createContentPane(), BorderLayout.CENTER);
		formatter = new SqlFormatter();
	}

	/**
	 * 创建工具栏
	 * 
	 * @return
	 */
	private JPanel createToolbar() {
		final JCheckBox rs = new JCheckBox();
		// 工具栏面板
		JPanel managerToolbar = new JPanel();
		managerToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton formatBtn = new JButton("格式化");
		formatBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = sqlTA.getText();
				String afterFormatText = formatter.format(text, rs.isSelected());
				sqlTA.setText(afterFormatText);
			}
		});
		managerToolbar.add(formatBtn);
		managerToolbar.add(new JLabel("去空白"));
		managerToolbar.add(rs);
		return managerToolbar;
	}

	/**
	 * 创建内容面板
	 * 
	 * @return
	 */
	private JPanel createContentPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		sqlTA = new JTextPane();
		pane.add(new JScrollPane(sqlTA), BorderLayout.CENTER);
		return pane;
	}
}
