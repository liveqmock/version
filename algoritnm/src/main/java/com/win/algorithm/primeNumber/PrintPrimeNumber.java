package com.win.algorithm.primeNumber;

/**
 * 素数判断与打印
 * 
 * @author 袁晓冬
 * 
 */
public class PrintPrimeNumber {

	public static boolean isPrime(int num) {
		if (num <= 0 || num == 1) {
			return false;
		}
		if (num == 2) {
			return true;
		}
		for (int i = 2; i < Math.sqrt(num); i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}

	public static void println(int num) {
		boolean[] numbers = new boolean[num + 1];
		// 提出索引为0的位，索引为1的位为false非素数，索引为2的位 为素数
		for (int i = 2; i <= num; i++) {
			// 如果为素数，则剔除能整除他的数
			numbers[i] = true;
		}
		for (int i = 2; i <= num; i++) {
			// 如果为素数，则剔除能整除他的数
			if (numbers[i]) {
				int j = 2;
				int index = 0;
				while ((index = (i * j++)) <= num) {
					numbers[index] = false;
				}
			}
		}
		for (int i = 2; i <= num; i++) {
			// 如果为素数，则剔除能整除他的数
			if (numbers[i]) {
				System.err.print(i + "\t");
			}
		}
	}
}
