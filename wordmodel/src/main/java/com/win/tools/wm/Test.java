package com.win.tools.wm;

import java.util.HashMap;
import java.util.Map;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char a = 0;
		char b = 1;
		StringBuffer sb = new StringBuffer();
		sb.append(a);
		sb.append(b);
		Map<Integer, String> hashMap = new HashMap<Integer, String>();
		for (int i = 0; i < 255; i++) {
			for (int j = 0; j < 255; j++) {
				sb.setLength(0);
				sb.append((char) i);
				sb.append((char) j);
				int hash = sb.toString().hashCode();
				if (hashMap.containsKey(hash)) {
					System.err.println(hashMap.get(hash) + "-" + sb.toString());
				} else {
					hashMap.put(hash, sb.toString());
				}
			}
		}
	}

}
