package com.win.tools.easy.chat.common;

/**
 * 消息对象
 * 
 * @author 袁晓冬
 * 
 */
public class Message {
	/** 字体颜色 */
	int color;
	/** 是否加粗 */
	boolean bold;
	/** 字体大小 */
	int fontSize;
	/** 消息内容 */
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
