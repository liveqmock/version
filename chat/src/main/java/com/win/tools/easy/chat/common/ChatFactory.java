package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;


/**
 * 工厂类
 * 
 * @author 袁晓冬
 * 
 */
public class ChatFactory {

	// 保存被创建的Action对象
	private static Map<String, Action> actions = new HashMap<String, Action>();

	/**
	 * 获取当前过滤链
	 * 
	 * @return
	 */
	public static FilterChain getFilterChain() {
		return SimpleFilterChain.HEAD;
	}

	/**
	 * 从Map中得到Action对象, 如果拿不到, 则创建
	 * 
	 * @param request
	 * @return
	 */
	public static Action getAction(Request request) {
		if (null == request) {
			return null;
		}
		Class<? extends Action> className = request
				.getServerActionClass();
		if (null == className) {
			return null;
		}
		try {
			// 存放Action对象的Key
			String key = className.getName();
			if (actions.get(key) == null) {
				Action action = className.newInstance();
				actions.put(key, action);
			}
			return actions.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
