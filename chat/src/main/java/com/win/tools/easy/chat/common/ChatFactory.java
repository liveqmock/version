package com.win.tools.easy.chat.common;

import java.util.HashMap;
import java.util.Map;


/**
 * ������
 * 
 * @author Ԭ����
 * 
 */
public class ChatFactory {

	// ���汻������Action����
	private static Map<String, Action> actions = new HashMap<String, Action>();

	/**
	 * ��ȡ��ǰ������
	 * 
	 * @return
	 */
	public static FilterChain getFilterChain() {
		return SimpleFilterChain.HEAD;
	}

	/**
	 * ��Map�еõ�Action����, ����ò���, �򴴽�
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
			// ���Action�����Key
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
