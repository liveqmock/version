package com.win.algorithm;

import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;

import org.junit.Test;

import com.win.algorithm.md5.MD5;

public class MD5Test {
	public static MD5Test instance = new MD5Test();

	public static int a;
	public static int b = 0;
	public static char c ;
	public int d;
	public int e=0;
	public MD5Test() {
		a++;
		b++;
		d++;
		e++;
	}

	public static void main(String[] args) {
		MD5Test instance = MD5Test.instance;
		System.err.println(MD5Test.a);
		System.err.println(MD5Test.b);
		System.err.println(MD5Test.c);
		System.err.println(instance.d);
		System.err.println(instance.e);
	}

	@Test
	public void testGetMD5() {
		String source = "http://www.google.com";
		String dest = MD5.getMD5(source.getBytes());
		System.err.println(dest);
	}

	@Test
	public void testYh() {
		byte a = 0x1;
		byte b = 0x0;
		// 异或操作，对应位相等时为0，不相等时为1
		System.err.println(a ^ b);
	}

	@Test
	public void printProviders() {
		for (Provider provider : Security.getProviders()) {
			for (Service service : provider.getServices()) {
				System.err.println(provider.getName() + ":"
						+ service.getAlgorithm());
			}
		}
	}

	@Test
	public void testSwitch() {
		int i = 17;
		switch (i) {
		default:
			System.err.println("default");
		case 1:
			System.err.println("i=1");
			break;
		case 2:
			System.err.println("i=2");
			break;
		case 3:
			System.err.println("i=3");
			break;
		case 4:
			System.err.println("i=4");
			break;
		case 5:
			System.err.println("i=5");
		case 6:
			System.err.println("i=6");
			break;
		case 7:
			System.err.println("i=7");
		}
	}
}
