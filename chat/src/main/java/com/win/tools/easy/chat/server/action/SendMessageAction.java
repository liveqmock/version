package com.win.tools.easy.chat.server.action;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.win.tools.easy.chat.common.BaseAction;
import com.win.tools.easy.chat.common.ChatUtils;
import com.win.tools.easy.chat.common.Message;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.common.Response;
import com.win.tools.easy.chat.entity.User;
import com.win.tools.easy.chat.server.ServerContext;

/**
 * 发送消息
 * 
 * @author 袁晓冬
 * 
 */
public class SendMessageAction extends BaseAction {

	@Override
	public int execute(Request request, Response response) {
		Message content = (Message) request.getParameter("content");
		String receiverId = (String) request.getParameter("receiverId");
		User receiver = ServerContext.getInstance().getUserById(receiverId);
		if (receiver != null) {
			// 向一个人发
			sendToOne(content, response);
			// 需要向发送人会写，所以response不设置已发送
			ChatUtils.sendResponseToUser(receiver, response);
		} else {
			List<Message> messages = new ArrayList<Message>();
			Message userInfo = new Message();
			userInfo.setBold(true);
			userInfo.setColor(Color.RED.getRGB());
			userInfo.setContent("用户已下线！");
			userInfo.setFontSize(14);
			messages.add(userInfo);
			// 接收者已下线
			response.setData("chatUserID", receiverId);
			response.setData("messages", messages);
		}
		// 发送给消息发送者，设置聊天用户为被发送人
		response.setData("chatUser", receiver);
		return 0;
	}

	/**
	 * 向具体的一个人发送
	 * 
	 * @param content
	 * @param receiver
	 * @param sender
	 * @param response
	 */
	private void sendToOne(Message content, Response response) {
		User sender = ServerContext.getInstance().getCurrentUser();
		List<Message> messages = new ArrayList<Message>();
		DateFormat df = new SimpleDateFormat("MM-dd hh:mm:ss");
		Message userInfo = new Message();
		userInfo.setBold(false);
		userInfo.setColor(Color.BLUE.getRGB());
		userInfo.setContent(sender.getName() + " " + df.format(new Date()));
		userInfo.setFontSize(14);
		messages.add(userInfo);
		messages.add(content);
		response.setData("messages", messages);
		response.setData("chatUser", sender);
	}
}
