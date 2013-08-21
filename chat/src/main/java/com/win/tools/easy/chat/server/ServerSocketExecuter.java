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
 * 处理服务器接受到得socket
 * 
 * @author 袁晓冬
 * 
 */
public class ServerSocketExecuter implements Runnable {
	/** 日志记录 */
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
		// 请求对象输出流
		InputStream is = null;
		// socket输入流
		OutputStream os = null;
		try {
			is = this.socket.getInputStream();
			os = this.socket.getOutputStream();
			byte[] buffer = new byte[ChatUtils.INIT_BUFFER_SIZE];
			byte[] requestBuffer = new byte[ChatUtils.INIT_REQUEST_SIZE];
			int index = 0;
			while (true) {
				is.read(buffer);
				// 拷贝buffer数组
				System.arraycopy(buffer, 0, requestBuffer, index, buffer.length);
				index += buffer.length;
				if (index + buffer.length > requestBuffer.length) {
					byte[] newRequest = new byte[requestBuffer.length
							+ buffer.length];
					// 拷贝request数组
					System.arraycopy(requestBuffer, 0, newRequest, 0,
							requestBuffer.length);
					requestBuffer = newRequest;
				}
				// 请求已结束
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
			// 清除内存中的用户
			ServerContext.getInstance().logout();
			if (null != this.socket) {
				try {
					this.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			LOGGER.debug("一个客户端断开链接！");
		}
	}

	private void doRequest(Request request, OutputStream os) {
		while (request != null) {
			// 从request中得到客户端处理类, 并且构造Response对象
			Response response = new Response(request);
			ChatFactory.getFilterChain().doFilter(request, response);
			// 退出，无需响应
			if (ChatCode.SERVER_EXIST == response.getStatus()) {
				LOGGER.debug("exit:"
						+ ChatCode.getMessageByCode(ChatCode.SERVER_EXIST));
				throw new RuntimeException("exit");
			}
			// 发送未发送的响应
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
