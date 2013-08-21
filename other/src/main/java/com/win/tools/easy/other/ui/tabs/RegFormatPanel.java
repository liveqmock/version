package com.win.tools.easy.other.ui.tabs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 根据正则表达式提取文档
 * 
 * @author 袁晓冬
 * 
 */
public class RegFormatPanel extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -132242811842079588L;
	private JTextArea contentTA = null;
	private String hisSearch = null;

	public RegFormatPanel() {
		setLayout(new BorderLayout());
		add(createToolbar(), BorderLayout.NORTH);
		add(createContentPane(), BorderLayout.CENTER);
	}

	/**
	 * 创建工具栏
	 * 
	 * @return
	 */
	private JPanel createToolbar() {
		// 工具栏面板
		JPanel managerToolbar = new JPanel();
		managerToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton formatBtn = new JButton("查找");
		formatBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hisSearch = JOptionPane.showInputDialog("输入查找正则表达式", hisSearch);
				if (null == hisSearch || hisSearch.length() == 0) {
					return;
				}
				String content = contentTA.getText();
				if (null == content || content.trim().length() == 0) {
					return;
				}
				Pattern pattern = Pattern.compile(hisSearch);
				Matcher matcher = pattern.matcher(content);
				StringBuffer result = new StringBuffer();
				while (matcher.find()) {
					result.append(matcher.group());
					result.append("\r\n");
				}
				contentTA.setText(result.toString());
			}
		});

		JButton singleLineBtn = new JButton("去重行");
		singleLineBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringReader sr = new StringReader(contentTA.getText());
				BufferedReader br = new BufferedReader(sr);
				List<String> singleLineResult = new ArrayList<String>();
				try {
					String line = null;
					while ((line = br.readLine()) != null) {
						if (singleLineResult.contains(line)) {
							continue;
						}
						singleLineResult.add(line);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					if (null != br) {
						try {
							br.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				StringBuffer resultStr = new StringBuffer();
				for (String line : singleLineResult) {
					resultStr.append(line);
					resultStr.append("\r\n");
				}
				contentTA.setText(resultStr.toString());
			}
		});

		managerToolbar.add(formatBtn);
		managerToolbar.add(singleLineBtn);
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
		contentTA = new JTextArea();
		pane.add(new JScrollPane(contentTA), BorderLayout.CENTER);
		return pane;
	}
}
