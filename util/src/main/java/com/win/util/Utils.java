package com.win.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 工具类
 * 
 * @author 袁晓冬
 * 
 */
public class Utils {

	/**
	 * 获取异常详细信息
	 * 
	 * @param t
	 * @return
	 */
	public static String getExceptionDetail(Throwable t) {
		StringWriter sWriter = new StringWriter();
		t.printStackTrace(new PrintWriter(sWriter));
		return sWriter.toString();
	}

	/**
	 * 获取字节长度
	 * 
	 * @param value
	 * @return
	 */
	public static int getStringLength(String value) {
		int len = 0;
		if (value != null)
			len = value.getBytes().length;
		return len;
	}
}
