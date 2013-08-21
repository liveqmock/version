package com.win.tools.easy.chat.client;



public class ClientStart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = ClientFactory.getClient();
		client.moveToMainPage();
	}

}

