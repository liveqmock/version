package com.win.tools.easy.chat.common;

import org.apache.log4j.Logger;


/**
 * ����Action����
 * 
 * @author Ԭ����
 * 
 */
public class BaseAction implements Action {
	/** ��־��¼ */
	protected Logger logger = Logger.getLogger(getClass());

	@Override
	public int execute(Request request, Response response) {
		return 0;
	}
}
