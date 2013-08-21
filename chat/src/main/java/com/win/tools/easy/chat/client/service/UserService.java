package com.win.tools.easy.chat.client.service;

import java.awt.Color;

import com.win.tools.easy.chat.client.action.CLoginAction;
import com.win.tools.easy.chat.client.action.CReceiveMessageAction;
import com.win.tools.easy.chat.common.Message;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.server.action.LoginAction;
import com.win.tools.easy.chat.server.action.SendMessageAction;

/**
 * 客户端用户相关服务
 * 
 * @author 袁晓冬
 * 
 */
public class UserService extends BaseClientService {
	/**
	 * 用户登录
	 * 
	 * @param userName
	 *            用户名
	 * @param pwd
	 *            密码
	 */
	public void login(String userName, String pwd) {
		// 无连接
		if (null == getSocket()) {
			return;
		}
		Request request = createRequest(LoginAction.class, CLoginAction.class);
		request.setParameter("uname", userName);
		request.setParameter("pwd", pwd);
		sendRequest(request);
	}

	/**
	 * 向其他人发送聊天信息
	 * 
	 * @param message
	 * @param userId
	 */
	public void sendMesage(String message, String userId) {
		// 构造请求
		Request request = new Request(SendMessageAction.class,
				CReceiveMessageAction.class);
		Message messageObj = new Message();
		messageObj.setContent(message);
		messageObj.setBold(false);
		messageObj.setColor(Color.BLACK.getRGB());
		messageObj.setFontSize(14);
		// 设置参数
		request.setParameter("receiverId", userId);
		request.setParameter("content", messageObj);
//		request.setUserId(ClientContext.getInstance().getUser().getId());
		sendRequest(request);
	}
}
