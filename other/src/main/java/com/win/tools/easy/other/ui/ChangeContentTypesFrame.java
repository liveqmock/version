package com.win.tools.easy.other.ui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.springframework.util.StringUtils;

import com.win.tools.easy.platform.ui.FrameStart;
import com.win.tools.easy.platform.ui.InternalFrame;

/**
 * 格式化工具
 * 
 * @author 袁晓冬
 * 
 */
public class ChangeContentTypesFrame {
	/** 窗口对象 */
	private InternalFrame internalFrame;
	/** Spring布局管理器 */
	private SpringLayout springLayout;
	/** 文件路径 */
	private JTextField filePath;
	/** 原编码 */
	private JComboBox srcTypeComboBox;
	/** 目标编码 */
	private JComboBox destTypeComboBox;
	/** 转换字符集 */
	private String[] contentTyes = { "GBK", "UTF-8" };
	/** 执行按钮事件处理 */
	private final ActionListener executeActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String filePathStr = filePath.getText();
			if (!StringUtils.hasLength(filePathStr)) {
				JOptionPane.showMessageDialog(null, "请选择文件或目录！");
				return;
			}
			String src = (String) srcTypeComboBox.getSelectedItem();
			String dest = (String) destTypeComboBox.getSelectedItem();
			try {
				if (doChange(src, dest, filePathStr)) {
					JOptionPane.showMessageDialog(null, "转换完成！");
				} else {
					JOptionPane.showMessageDialog(null, "转换失败！");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "转换失败！");
			}
		}
	};

	/**
	 * 构造函数
	 * 
	 * @param internalFrame
	 */
	public ChangeContentTypesFrame(InternalFrame internalFrame) {
		springLayout = new SpringLayout();
		internalFrame.setLayout(springLayout);
		this.internalFrame = internalFrame;
	}

	/**
	 * 显示画面
	 */
	public void show() {
		try {
			internalFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		setContentPanel();
		internalFrame.setVisible(true);
	}

	/**
	 * 设置内容面板
	 */
	public void setContentPanel() {
		// 文件路径
		Container contentPane = internalFrame.getContentPane();
		JPanel filePathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel pathLabel = new JLabel("文件路径：");
		filePathPanel.add(pathLabel);
		filePath = new JTextField(25);
		filePathPanel.add(filePath);
		JButton filteChoseBtn = new JButton("选择...");
		filteChoseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					filePath.setText(file.getAbsolutePath());
				}
			}
		});
		filePathPanel.add(filteChoseBtn);
		contentPane.add(filePathPanel);
		springLayout.putConstraint(SpringLayout.NORTH, filePathPanel, 5,
				SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, filePathPanel, 5,
				SpringLayout.WEST, contentPane);
		springLayout.putConstraint(SpringLayout.EAST, filePathPanel, -5,
				SpringLayout.EAST, contentPane);
		// 原编码
		JPanel srcTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		srcTypeComboBox = new JComboBox(contentTyes);
		srcTypePanel.add(new JLabel("原编码："));
		srcTypePanel.add(srcTypeComboBox);
		contentPane.add(srcTypePanel);
		springLayout.putConstraint(SpringLayout.NORTH, srcTypePanel, 5,
				SpringLayout.SOUTH, filePathPanel);
		springLayout.putConstraint(SpringLayout.WEST, srcTypePanel, 0,
				SpringLayout.WEST, filePathPanel);
		springLayout.putConstraint(SpringLayout.EAST, srcTypePanel, 0,
				SpringLayout.EAST, filePathPanel);
		// 转换后编码

		JPanel destTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		destTypeComboBox = new JComboBox(contentTyes);
		destTypePanel.add(new JLabel("转换后编码："));
		destTypePanel.add(destTypeComboBox);
		contentPane.add(destTypePanel);
		springLayout.putConstraint(SpringLayout.NORTH, destTypePanel, 5,
				SpringLayout.SOUTH, srcTypePanel);
		springLayout.putConstraint(SpringLayout.WEST, destTypePanel, 0,
				SpringLayout.WEST, filePathPanel);
		springLayout.putConstraint(SpringLayout.EAST, destTypePanel, 0,
				SpringLayout.EAST, filePathPanel);
		// 执行转换按钮
		JButton execute = new JButton("执行转换");
		execute.addActionListener(executeActionListener);
		JPanel executePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		executePanel.add(execute);
		contentPane.add(executePanel);
		springLayout.putConstraint(SpringLayout.NORTH, executePanel, 5,
				SpringLayout.SOUTH, destTypePanel);
		springLayout.putConstraint(SpringLayout.WEST, executePanel, 0,
				SpringLayout.WEST, filePathPanel);
		springLayout.putConstraint(SpringLayout.EAST, executePanel, 0,
				SpringLayout.EAST, filePathPanel);
	}

	private boolean doChange(String srcType, String destType, String path)
			throws IOException {
		// 原与目标编码一致时无需转换
		if (srcType.equals(destType)) {
			return true;
		}
		File srcFile = new File(path);
		File parentFile = srcFile.getParentFile();
		File bakFile = new File(parentFile, srcFile.getName() + ".bak");
		while (bakFile.exists()) {
			bakFile = new File(parentFile, bakFile.getName() + ".bak");
		}
		srcFile.renameTo(bakFile);
		if (!srcFile.exists()) {
			srcFile.mkdir();
		}
		changeFile(srcType, destType, bakFile, srcFile);
		return true;
	}

	/**
	 * 递归执行文件编码转换
	 * 
	 * @param srcType
	 * @param destType
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	private void changeFile(String srcType, String destType, File srcFile,
			File destFile) throws IOException {
		if (!destFile.exists()) {
			if (srcFile.isDirectory()) {
				destFile.mkdir();
			} else {
				destFile.createNewFile();
			}
		}
		if (srcFile.isDirectory()) {
			File[] subFiles = srcFile.listFiles();
			for (File f : subFiles) {
				File destSubFile = new File(destFile, f.getName());
				changeFile(srcType, destType, f, destSubFile);
			}
		} else {
			PrintWriter pw = new PrintWriter(destFile, destType);
			BufferedReader br = new BufferedReader(new FileReader(srcFile));
			try {
				String line = null;
				while ((line = br.readLine()) != null) {
					pw.write(line + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (pw != null) {
					try {
						pw.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		FrameStart.start();
	}
}
