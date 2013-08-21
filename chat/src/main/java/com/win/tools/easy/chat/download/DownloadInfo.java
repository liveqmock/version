package com.win.tools.easy.chat.download;

/**
 * �����ļ���Ϣ��
 * 
 * @author Ԭ����
 * 
 */
public class DownloadInfo {
	// �����ļ�url
	private String url;
	// �����ļ�����
	private String fileName;
	// �����ļ�·��
	private String filePath;

	// �ֳɶ��ٶ����أ� ÿһ����һ���߳��������
	private int splitter;
	// �����ļ�Ĭ�ϱ���·��
	private final static String FILE_PATH = "C:/temp";
	// Ĭ�Ϸֿ������߳���
	private final static int SPLITTER_NUM = 5;

	public DownloadInfo() {
		super();
	}

	/** * @param url ���ص�ַ */
	public DownloadInfo(String url) {
		this(url, null, null, SPLITTER_NUM);
	}

	/** * @param url ���ص�ַurl * @param splitter �ֳɶ��ٶλ��Ƕ��ٸ��߳����� */
	public DownloadInfo(String url, int splitter) {
		this(url, null, null, splitter);
	}

	/***
	 * * @param url ���ص�ַ * @param fileName �ļ����� * @param filePath �ļ�����·�� * @param
	 * splitter �ֳɶ��ٶλ��Ƕ��ٸ��߳�����
	 */
	public DownloadInfo(String url, String fileName, String filePath,
			int splitter) {
		super();
		if (url == null || "".equals(url)) {
			throw new RuntimeException("url is not null!");
		}
		this.url = url;
		this.fileName = (fileName == null || "".equals(fileName)) ? getFileName(url)
				: fileName;
		this.filePath = (filePath == null || "".equals(filePath)) ? FILE_PATH
				: filePath;
		this.splitter = (splitter < 1) ? SPLITTER_NUM : splitter;
	}

	/**
	 * * <b>function:</b> ͨ��url����ļ����� * @author hoojo * @createDate 2011-9-30
	 * ����05:00:00 * @param url * @return
	 */
	private String getFileName(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.length());
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url == null || "".equals(url)) {
			throw new RuntimeException("url is not null!");
		}
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = (fileName == null || "".equals(fileName)) ? getFileName(url)
				: fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = (filePath == null || "".equals(filePath)) ? FILE_PATH
				: filePath;
	}

	public int getSplitter() {
		return splitter;
	}

	public void setSplitter(int splitter) {
		this.splitter = (splitter < 1) ? SPLITTER_NUM : splitter;
	}

	@Override
	public String toString() {
		return this.url + "#" + this.fileName + "#" + this.filePath + "#"
				+ this.splitter;
	}
}
