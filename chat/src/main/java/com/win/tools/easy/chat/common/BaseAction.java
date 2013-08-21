package com.win.tools.easy.chat.common;

import org.apache.log4j.Logger;


/**
 * 服务Action基类
 * 
 * @author 袁晓冬
 * 
 */
public class BaseAction implements Action {
	/** 日志记录 */
	protected Logger logger = Logger.getLogger(getClass());

	@Override
	public int execute(Request request, Response response) {
		return 0;
	}
}
