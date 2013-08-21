package com.win.tools.easy.chat.test;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.win.tools.easy.chat.common.ChatUtils;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.server.action.LoginAction;

public class ChatUtilsTest {
	private static final Logger LOGGER = Logger.getLogger(ChatUtilsTest.class);

	@Test
	public void getXmlFromObject() {
		Request request = new Request(LoginAction.class);
		LOGGER.debug("getXmlFromObject result:"
				+ ChatUtils.getXmlFromObject(request));
	}
}
