package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;

/**
 * �������<br>
 * <p>
 * 1xx:��Ϣ
 * <p>
 * 4xx:�����쳣
 * <p>
 * 5xx:�������쳣
 * 
 * @author Ԭ����
 * 
 */
public class ChatCode {

	/** �Ҳ������� */
	public final static int COMMAND_NOT_FOUND = 401;
	/** ��Ҫ���е�¼ */
	public final static int USER_UNLOGIN = 402;
	/** �û������������ */
	public final static int USER_INVALID = 403;
	/** �û��ѵ�¼ */
	public final static int USER_LOGONED = 404;
	/** Actionִ��ʧ�� */
	public final static int ACTION_ERROR = 501;
	/** �û��ǳ����� */
	public final static int SERVER_EXIST = 101;
	private static Map<Integer, String> errorMap = new HashMap<Integer, String>();
	static {
		errorMap.put(COMMAND_NOT_FOUND, "�Ҳ�������");
		errorMap.put(USER_UNLOGIN, "���ȵ�¼ ");
		errorMap.put(USER_INVALID, "�û������������");
		errorMap.put(USER_LOGONED, "�û��ѵ�¼");
		errorMap.put(ACTION_ERROR, "ִ��ʧ��");
		errorMap.put(SERVER_EXIST, "�˳��ɹ�");
	}

	public static String getMessageByCode(int code) {
		return errorMap.get(code);
	}
}
