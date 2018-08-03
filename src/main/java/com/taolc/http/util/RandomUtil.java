package com.taolc.http.util;

import java.util.Random;

/**
 * 生成随机数
 * @author taolc
 *
 */
public class RandomUtil {
	/**
	 * 获取随机数（包含min和max的值）
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static int getRandom(int min,int max){
		int temp = max - min;
		return Math.round(new Random().nextFloat()*temp)+min;
	}

	/**
	 * 获取length长度的随机数字
	 * @param length
	 * @return
	 */
	public static String getRandomNumber(int length){
		StringBuilder stringBuilder = new StringBuilder();
		Random random = new Random();
		for(int i=0;i<length;i++){
			stringBuilder.append(random.nextInt(10));
		}
		return stringBuilder.toString();
	}
}
