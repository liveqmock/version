package com.win.tools.easy.chat.download;

public class TestDownloadMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DownloadUtils
				.download(
						"http://172.16.85.50/vrveis/download/DeviceRegist.exe",
						"DeviceRegist.exe", "c:/temp", 2);
	}

}
