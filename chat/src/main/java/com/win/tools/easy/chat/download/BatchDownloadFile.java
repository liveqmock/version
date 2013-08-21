package com.win.tools.easy.chat.download;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * �����������ļ�
 * 
 * @author Ԭ����
 * 
 */
public class BatchDownloadFile implements Runnable {
	// �����ļ���Ϣ
	private DownloadInfo downloadInfo;
	// һ�鿪ʼ����λ��
	private long[] startPos;
	// һ���������λ��
	private long[] endPos;
	// ����ʱ��
	private static final int SLEEP_SECONDS = 500;
	// ���߳�����
	private DownloadFile[] fileItem;
	// �ļ�����
	private int length;
	// �Ƿ��һ���ļ�
	private boolean first = true;
	// �Ƿ�ֹͣ����
	private boolean stop = false;
	// ��ʱ�ļ���Ϣ
	private File tempFile;

	public BatchDownloadFile(DownloadInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
		String tempPath = this.downloadInfo.getFilePath() + File.separator
				+ downloadInfo.getFileName() + ".position";
		tempFile = new File(tempPath);
		// ������ڶ����λ�õ��ļ�
		if (tempFile.exists()) {
			first = false;
			// ��ֱ�Ӷ�ȡ����
			try {
				readPosInfo();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// ����ĳ��Ⱦ�Ҫ�ֳɶ��ٶε�����
			startPos = new long[downloadInfo.getSplitter()];
			endPos = new long[downloadInfo.getSplitter()];
		}
	}

	@Override
	public void run() {
		// �״����أ���ȡ�����ļ�����
		if (first) {
			length = this.getFileSize();
			// ��ȡ�ļ�����
			if (length == -1) {
				LogUtils.log("file length is know!");
				stop = true;
			} else if (length == -2) {
				LogUtils.log("read file length is error!");
				stop = true;
			} else if (length > 0) {
				/** * eg * start: 1, 3, 5, 7, 9 * end: 3, 5, 7, 9, length */
				for (int i = 0, len = startPos.length; i < len; i++) {
					int size = i * (length / len);
					startPos[i] = size;
					// �������һ���������λ��
					if (i == len - 1) {
						endPos[i] = length;
					} else {
						size = (i + 1) * (length / len);
						endPos[i] = size;
					}
					LogUtils.log("start-end Position[" + i + "]: "
							+ startPos[i] + "-" + endPos[i]);
				}
			} else {
				LogUtils.log("get file length is error, download is stop!");
				stop = true;
			}
		}
		// ���߳̿�ʼ����
		if (!stop) {
			// �������߳����ض�������
			fileItem = new DownloadFile[startPos.length];
			for (int i = 0; i < startPos.length; i++) {
				try {
					// ����ָ���������߳����ض���ÿ���̶߳������ָ�������ݵ�����
					fileItem[i] = new DownloadFile(downloadInfo.getUrl(),
							this.downloadInfo.getFilePath() + File.separator
									+ downloadInfo.getFileName(), startPos[i],
							endPos[i], i);
					fileItem[i].start();
					// �����̣߳���ʼ����
					LogUtils.log("Thread: " + i + ", startPos: " + startPos[i]
							+ ", endPos: " + endPos[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// ѭ��д�������ļ�������Ϣ
			while (!stop) {
				try {
					writePosInfo();
					LogUtils.log("downloading����");
					Thread.sleep(SLEEP_SECONDS);
					stop = true;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < startPos.length; i++) {
					if (!fileItem[i].isDownloadOver()) {
						stop = false;
						break;
					}
				}
			}
			LogUtils.info("Download task is finished!");
		}
	}

	/**
	 * ��д������ݱ�������ʱ�ļ���
	 * 
	 * @author hoojo
	 * @createDate 2011-9-23 ����05:25:37
	 * @throws IOException
	 */
	private void writePosInfo() throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(
				tempFile));
		dos.writeInt(startPos.length);
		for (int i = 0; i < startPos.length; i++) {
			dos.writeLong(fileItem[i].getStartPos());
			dos.writeLong(fileItem[i].getEndPos());
		}
		dos.close();
	}

	private void readPosInfo() throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
		int startPosLength = dis.readInt();
		startPos = new long[startPosLength];
		endPos = new long[startPosLength];
		for (int i = 0; i < startPosLength; i++) {
			startPos[i] = dis.readLong();
			endPos[i] = dis.readLong();
		}
		dis.close();
	}

	private int getFileSize() {
		int fileLength = -1;
		try {
			URL url = new URL(this.downloadInfo.getUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			DownloadFile.setHeader(conn);
			int stateCode = conn.getResponseCode();
			// �ж�http status�Ƿ�ΪHTTP/1.1 206 Partial Content����200 OK
			if (stateCode != HttpURLConnection.HTTP_OK
					&& stateCode != HttpURLConnection.HTTP_PARTIAL) {
				LogUtils.log("Error Code: " + stateCode);
				return -2;
			} else if (stateCode >= 400) {
				LogUtils.log("Error Code: " + stateCode);
				return -2;
			} else {
				// ��ȡ����
				fileLength = conn.getContentLength();
				LogUtils.log("FileLength: " + fileLength);
			}
			DownloadFile.printHeader(conn);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileLength;
	}
}
