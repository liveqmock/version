package com.win.tools.easy.chat.common;

/**
 * ��Ϣ����
 * 
 * @author Ԭ����
 * 
 */
public class Message {
	/** ������ɫ */
	int color;
	/** �Ƿ�Ӵ� */
	boolean bold;
	/** �����С */
	int fontSize;
	/** ��Ϣ���� */
	String content;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
