package com.win.tools.easy.chat.common;


/**
 * ��������������Ľӿ�
 * 
 * @author Ԭ����
 * 
 */
public interface Action {

	/**
	 * Actionִ��
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	int execute(Request request, Response response);
}
