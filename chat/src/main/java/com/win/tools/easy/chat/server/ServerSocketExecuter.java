package com.win.tools.easy.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.win.tools.easy.chat.common.ChatCode;
import com.win.tools.easy.chat.common.ChatFactory;
import com.win.tools.easy.chat.common.ChatUtils;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.common.Response;

/**
 * ������������ܵ���socket
 * 
 * @author Ԭ����
 * 
 */
public class ServerSocketExecuter implements Runnable {
	/** ��־��¼ */
	private static final Logger LOGGER = Logger
			.getLogger(ServerSocketExecuter.class);

	/** Socket */
	private Socket socket;

	public ServerSocketExecuter(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		ServerContext.getInstance().setCurrentSocket(socket);
		// ������������
		InputStream is = null;
		// socket������
		OutputStream os = null;
		try {
			is = this.socket.getInputStream();
			os = this.socket.getOutputStream();
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
							doRequest(
									(Request) ChatUtils.getObjectFromXml(new String(
											recevied)), os);
							Arrays.fill(requestBuffer, (byte) 0);
							index = 0;
						}
						break;
					}
				}
				Arrays.fill(buffer, (byte) 0);
			}
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		} finally {
			// ����ڴ��е��û�
			ServerContext.getInstance().logout();
			if (null != this.socket) {
				try {
					this.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			LOGGER.debug("һ���ͻ��˶Ͽ����ӣ�");
		}
	}

	private void doRequest(Request request, OutputStream os) {
		while (request != null) {
			// ��request�еõ��ͻ��˴�����, ���ҹ���Response����
			Response response = new Response(request);
			ChatFactory.getFilterChain().doFilter(request, response);
			// �˳���������Ӧ
			if (ChatCode.SERVER_EXIST == response.getStatus()) {
				LOGGER.debug("exit:"
						+ ChatCode.getMessageByCode(ChatCode.SERVER_EXIST));
				throw new RuntimeException("exit");
			}
			// ����δ���͵���Ӧ
			if (!response.isSend()) {
				byte[] secretResponseWithTrail = ChatUtils.encode(response);
				try {
					os.write(secretResponseWithTrail);
					os.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				response.setSend(true);
			}
			request = request.nextRequest();
		}
	}
}
