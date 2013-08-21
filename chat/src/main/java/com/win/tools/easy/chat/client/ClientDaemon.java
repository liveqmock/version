package com.win.tools.easy.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.win.tools.easy.chat.client.action.BaseClientAction;
import com.win.tools.easy.chat.common.ChatUtils;
import com.win.tools.easy.chat.common.Response;

/**
 * �������Ӧ�����߳�
 * 
 * @author Ԭ����
 * 
 */
public class ClientDaemon implements Runnable {
	/** ��־��¼ */
	private static final Logger LOGGER = Logger.getLogger(ClientDaemon.class);

	/** �������� */
	private Socket socket = null;

	/** �ͻ��˴�����ʵ������ */
	private Map<String, BaseClientAction> actions = new HashMap<String, BaseClientAction>();

	// �õ���������Ӧ�еĿͻ��˴�����
	private BaseClientAction getClientAction(Response response) {
		Class<? extends BaseClientAction> action = response
				.getClientActionClass();
		if (null == action) {
			return null;
		}
		try {
			String key = action.getName();
			if (actions.get(key) == null) {
				BaseClientAction actionInst = action.newInstance();
				actions.put(key, actionInst);
			}
			return actions.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Socket getSocket() {
		return socket;
	}

	@Override
	public void run() {
		InputStream is = null;
		try {
			is = socket.getInputStream();
			byte[] buffer = new byte[ChatUtils.INIT_BUFFER_SIZE];
			byte[] requestBuffer = new byte[ChatUtils.INIT_REQUEST_SIZE];
			int index = 0;
			while (true) {
				is.read(buffer);
				// ����buffer����
				System.arraycopy(buffer, 0, requestBuffer, index, buffer.length);
				index += buffer.length;
				if (index + buffer.length > requestBuffer.length) {
					byte[] newRequest = new byte[requestBuffer.length
							+ buffer.length];
					// ����request����
					System.arraycopy(requestBuffer, 0, newRequest, 0,
							requestBuffer.length);
					requestBuffer = newRequest;
				}
				// �����ѽ���
				for (int i = requestBuffer.length; i > 0; i--) {
					if (requestBuffer[i - 1] != 0) {
						byte[] trail = new byte[8];
						System.arraycopy(requestBuffer, i - trail.length + 1,
								trail, 0, trail.length);
						if (Arrays.equals(trail, ChatUtils.REQUEST_ID)) {
							byte[] recevied = new byte[i + 1
									- ChatUtils.REQUEST_ID.length];
							System.arraycopy(requestBuffer, 0, recevied, 0,
									recevied.length);
							recevied = ChatUtils.decrypt(recevied);
							Response response = (Response) ChatUtils
									.getObjectFromXml(new String(recevied));
							// �õ��ͻ��˵Ĵ�����
							BaseClientAction action = getClientAction(response);
							if (action != null) {
								// ִ�пͻ��˴�����
								action.execute(response);
							} else {
								LOGGER.error("get a null client action from server!");
							}
							Arrays.fill(requestBuffer, (byte) 0);
							index = 0;
						}
						break;
					}
				}
				Arrays.fill(buffer, (byte) 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOGGER.debug("client daemo thread end!");
			if (null != this.socket) {
				try {
					this.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
