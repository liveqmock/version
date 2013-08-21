package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误代码<br>
 * <p>
 * 1xx:消息
 * <p>
 * 4xx:请求异常
 * <p>
 * 5xx:服务器异常
 * 
 * @author 袁晓冬
 * 
 */
public class ChatCode {

	/** 找不到命令 */
	public final static int COMMAND_NOT_FOUND = 401;
	/** 需要进行登录 */
	public final static int USER_UNLOGIN = 402;
	/** 用户名或密码错误 */
	public final static int USER_INVALID = 403;
	/** 用户已登录 */
	public final static int USER_LOGONED = 404;
	/** Action执行失败 */
	public final static int ACTION_ERROR = 501;
	/** 用户登出代码 */
	public final static int SERVER_EXIST = 101;
	private static Map<Integer, String> errorMap = new HashMap<Integer, String>();
	static {
		errorMap.put(COMMAND_NOT_FOUND, "找不到命令");
		errorMap.put(USER_UNLOGIN, "请先登录 ");
		errorMap.put(USER_INVALID, "用户名或密码错误");
		errorMap.put(USER_LOGONED, "用户已登录");
		errorMap.put(ACTION_ERROR, "执行失败");
		errorMap.put(SERVER_EXIST, "退出成功");
	}

	public static String getMessageByCode(int code) {
		return errorMap.get(code);
	}
}
