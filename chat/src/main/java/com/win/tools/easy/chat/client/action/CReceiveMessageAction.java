package com.win.tools.easy.chat.client.action;

import java.util.List;

import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.client.ui.ChatWindow;
import com.win.tools.easy.chat.common.Message;
import com.win.tools.easy.chat.common.Response;
import com.win.tools.easy.chat.entity.User;

/**
 * 聊天消息接收处理Action
 * 
 * @author 袁晓冬
 * 
 */
public class CReceiveMessageAction extends BaseClientAction {

	@Override
	public void doExecute(Response response) {
		User chatUser = (User) response.getData("chatUser");
		ChatWindow chatWindow = null;
		if (null != chatUser) {
			chatWindow = ClientFactory.openChatWindow(chatUser);
		} else {
			String chatUserId = (String) response.getData("chatUserID");
			if (chatUserId == null || chatUserId.length() == 0) {
				return;
			}
			chatWindow = ClientFactory.openChatWindow(chatUserId);
		}
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) response.getData("messages");
		for (Message msg : messages) {
			chatWindow.appendMessage(msg.getContent(), msg.getColor(),
					msg.isBold(), msg.getFontSize());
		}
	}
}
