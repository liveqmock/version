package com.win.tools.easy.chat.common;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.win.tools.easy.chat.entity.User;

/**
 * 工具类
 * 
 * @author 袁晓冬
 * 
 */
public class ChatUtils {

	/** log */
	static final Logger LOGGER = Logger.getLogger(ChatUtils.class);
	private static final String KEY = "~!@HN$%^";
	/** 每次读取的字节长度 */
	public static final int INIT_BUFFER_SIZE = 10;
	/** 请求初始化长度 */
	public static final int INIT_REQUEST_SIZE = 10;
	/** 请求结束标识 */
	public static final byte[] REQUEST_ID = new byte[] { 0xe, 0xb, 0x9, 0x0,
			0xe, 0xb, 0x9, 0x0 };
	/** xstream */
	private static XStream xstream;
	static {
		xstream = new XStream();
		xstream.autodetectAnnotations(true);
	}

	/**
	 * 发送response给user
	 * 
	 * @param user
	 * @param message
	 * @return
	 */
	public static int sendResponseToUser(User user, Response response) {
		Socket socket = user.getSocket();
		if (null == socket) {
			// 用户可能已不在线
			return -1;
		}
		OutputStream os = null;
		try {
			os = socket.getOutputStream();
			byte[] responseBytes = encode(response);
			os.write(responseBytes);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 将字符串转换成一个Request对象
	 * 
	 * @param xml
	 * @return
	 */
	public static <T> T getObjectFromXml(String xml) {
		try {
			@SuppressWarnings("unchecked")
			T t = (T) xstream.fromXML(xml);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将字符串转换成一个Request对象
	 * 
	 * @param xml
	 * @return
	 */
	public static String getXmlFromObject(Object object) {
		try {
			String xml = xstream.toXML(object);
			return xml.replaceAll("\n", " ");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密字符串s
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] encrypt(byte[] data) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(KEY.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密字符串s
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] decrypt(byte[] data) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(KEY.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将对象object转化为通信传输字节数组
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] encode(Object object) {
		String objectStr = ChatUtils.getXmlFromObject(object);
		if (objectStr == null) {
			return null;
		}
		byte[] secretResponse = encrypt(objectStr.getBytes());
		byte[] secretResponseWithTrail = new byte[secretResponse.length
				+ ChatUtils.REQUEST_ID.length];
		System.arraycopy(secretResponse, 0, secretResponseWithTrail, 0,
				secretResponse.length);
		System.arraycopy(ChatUtils.REQUEST_ID, 0, secretResponseWithTrail,
				secretResponse.length, ChatUtils.REQUEST_ID.length);
		return secretResponseWithTrail;
	}
}
