package com.win.tools.easy.chat;

import com.win.tools.easy.chat.client.Client;
import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.server.ServerStart;

/**
 * 客户端及服务器启动入口
 * 
 * @author 袁晓冬
 * 
 */
public class Main {
	static void usage() {
		System.err.println("Usage: \n\t" + "Start service: server\n\t"
				+ "Start client: client or emperty\n\t");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String choice = "client";
		if (args.length == 1) {
			choice = args[0];
		}
		if ("client".equals(choice)) {
			Client client = ClientFactory.getClient();
			client.moveToMainPage();
		} else if ("service".equals(choice)) {
			new ServerStart().start();
		} else {
			usage();
		}
	}

}
