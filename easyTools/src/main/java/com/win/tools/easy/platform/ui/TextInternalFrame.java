package com.win.tools.easy.platform.ui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 文本式窗口
 * 
 * @author 袁晓冬
 * 
 */
public class TextInternalFrame extends InternalFrame {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 409646623758106263L;
	final JTextArea content = new JTextArea();

	public TextInternalFrame(String title) {
		this(title, true);
	}

	public TextInternalFrame(String title, boolean editable) {
		super(title);
		content.setEditable(editable);
		getContentPane().add(new JScrollPane(content));
		setVisible(false);
	}

	/**
	 * 获取窗口内容
	 * 
	 * @return
	 */
	public String getText() {
		return content.getText();
	}

	/**
	 * 设置窗口内容
	 * 
	 * @param text
	 */
	public void setText(String text) {
		content.setText(text);
	}

	@Override
	public Object getData() {
		return content.getText();
	}

	@Override
	public boolean setData(Object data) {
		content.setText(String.valueOf(data));
		return true;
	}
}
