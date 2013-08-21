package com.win.tools.easy.chat.client.service;

import java.awt.Color;

import com.win.tools.easy.chat.client.action.CLoginAction;
import com.win.tools.easy.chat.client.action.CReceiveMessageAction;
import com.win.tools.easy.chat.common.Message;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.server.action.LoginAction;
import com.win.tools.easy.chat.server.action.SendMessageAction;

/**
 * �ͻ����û���ط���
 * 
 * @author Ԭ����
 * 
 */
public class UserService extends BaseClientService {
	/**
	 * �û���¼
	 * 
	 * @param userName
	 *            �û���
	 * @param pwd
	 *            ����
	 */
	public void login(String userName, String pwd) {
		// ������
		if (null == getSocket()) {
			return;
		}
		Request request = createRequest(LoginAction.class, CLoginAction.class);
		request.setParameter("uname", userName);
		request.setParameter("pwd", pwd);
		sendRequest(request);
	}

	/**
	 * �������˷���������Ϣ
	 * 
	 * @param message
	 * @param userId
	 */
	public void sendMesage(String message, String userId) {
		// ��������
		Request request = new Request(SendMessageAction.class,
				CReceiveMessageAction.class);
		Message messageObj = new Message();
		messageObj.setContent(message);
		messageObj.setBold(false);
		messageObj.setColor(Color.BLACK.getRGB());
		messageObj.setFontSize(14);
		// ���ò���
		request.setParameter("receiverId", userId);
		request.setParameter("content", messageObj);
//		request.setUserId(ClientContext.getInstance().getUser().getId());
		sendRequest(request);
	}
}
