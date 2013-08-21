package com.win.tools.easy.chat.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * ���߳������ļ�
 * 
 * @author Ԭ����
 * 
 */
public class DownloadFile extends Thread {
	// �����ļ�url
	private String url;
	// �����ļ���ʼλ��
	private long startPos;
	// �����ļ�����λ��
	private long endPos;
	// �߳�id
	private int threadId;
	// �����Ƿ����
	private boolean isDownloadOver = false;
	private SaveItemFile itemFile;
	private static final int BUFF_LENGTH = 1024 * 8;

	/**
	 * @param url
	 *            �����ļ�url
	 * @param name
	 *            �ļ�����
	 * @param startPos
	 *            �����ļ����
	 * @param endPos
	 *            �����ļ�������
	 * @param threadId
	 *            �߳�id
	 * @throws IOException
	 */
	public DownloadFile(String url, String name, long startPos, long endPos,
			int threadId) throws IOException {
		super();
		this.url = url;
		this.startPos = startPos;
		this.endPos = endPos;
		this.threadId = threadId; // �ֿ�����д���ļ�����
		this.itemFile = new SaveItemFile(name, startPos);
	}

	@Override
	public void run() {
		while (endPos > startPos && !isDownloadOver) {
			try {
				URL url = new URL(this.url);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// �������ӳ�ʱʱ��Ϊ10000ms
				conn.setConnectTimeout(10000);
				// ���ö�ȡ���ݳ�ʱʱ��Ϊ10000ms
				conn.setReadTimeout(10000);
				setHeader(conn);
				String property = "bytes=" + startPos + "-";
				conn.setRequestProperty("RANGE", property);
				// ���log��Ϣ
				LogUtils.log("��ʼ " + threadId + "��" + property + endPos);
				// printHeader(conn);
				// ��ȡ�ļ�����������ȡ�ļ�����
				InputStream is = conn.getInputStream();
				byte[] buff = new byte[BUFF_LENGTH];
				int length = -1;
				LogUtils.log("#start#Thread: " + threadId + ", startPos: "
						+ startPos + ", endPos: " + endPos);
				while ((length = is.read(buff)) > 0 && startPos < endPos
						&& !isDownloadOver) {
					// д���ļ����ݣ��������д��ĳ���
					startPos += itemFile.write(buff, 0, length);
				}
				LogUtils.log("#over#Thread: " + threadId + ", startPos: "
						+ startPos + ", endPos: " + endPos);
				LogUtils.log("Thread " + threadId + " is execute over!");
				this.isDownloadOver = true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (itemFile != null) {
						itemFile.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (endPos < startPos && !isDownloadOver) {
			LogUtils.log("Thread " + threadId
					+ " startPos > endPos, not need download file !");
			this.isDownloadOver = true;
		}
		if (endPos == startPos && !isDownloadOver) {
			LogUtils.log("Thread " + threadId
					+ " startPos = endPos, not need download file !");
			this.isDownloadOver = true;
		}
	}

	/**
	 * <b>function:</b> ��ӡ�����ļ�ͷ����Ϣ
	 * 
	 * @author hoojo
	 * @createDate 2011-9-22 ����05:44:35
	 * @param conn
	 *            HttpURLConnection
	 */
	public static void printHeader(URLConnection conn) {
		int i = 1;
		while (true) {
			String header = conn.getHeaderFieldKey(i);
			i++;
			if (header != null) {
				LogUtils.info(header + ":" + conn.getHeaderField(i));
			} else {
				break;
			}
		}
	}

	/**
	 * <b>function:</b> ����URLConnection��ͷ����Ϣ��αװ������Ϣ
	 * 
	 * @author hoojo
	 * @createDate 2011-9-28 ����05:29:43
	 * @param con
	 */
	public static void setHeader(URLConnection conn) {
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "utf-8");
		conn.setRequestProperty("Accept-Charset",
				"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("connnection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since",
				"Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-conntrol", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
	}

	public boolean isDownloadOver() {
		return isDownloadOver;
	}

	public long getStartPos() {
		return startPos;
	}

	public long getEndPos() {
		return endPos;
	}
}
