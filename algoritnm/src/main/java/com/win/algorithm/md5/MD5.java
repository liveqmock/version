package com.win.algorithm.md5;

import java.security.MessageDigest;

/**
 * MD5压缩处理<br>
 * MD5 以512 位分组来处理输入的信息，且每一分组又 被划分为16 个32 位子分组，经过一系列的处理后，算法的输出由4 个32 位分组组成， 将这4
 * 个32 位分组级联后将生成一个128 位的散列值
 * 
 * @author 袁晓冬
 * 
 */
public class MD5 {
	public static String getMD5(byte[] source) {
		String s = null;
		// 用来将字节转换成十六进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			// MD5 的计算结果是一个128 位的长整数，
			byte tmp[] = md.digest();
			// 用字节表示就是16 个字节
			// 每个字节用十六进制表示的话，使用两个字符，
			char str[] = new char[16 * 2];
			// 所以表示成十六进制需要32 个字符
			// 表示转换结果中对应的字符位置
			int k = 0;
			// 从第一个字节开始，将MD5 的每一个字节转换成十六进制字符
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i]; // 取第i 个字节
				// 取字节中高4 位的数字转换，>>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 取字节中低4 位的数字转换
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str); // 将换后的结果转换为字符串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
}