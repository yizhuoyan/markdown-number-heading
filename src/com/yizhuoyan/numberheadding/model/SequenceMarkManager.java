package com.yizhuoyan.numberheadding.model;

public class SequenceMarkManager {
	// 〇一二三四五六七八九
	private static String h1(int n) {
		String nstr = String.valueOf(n);
		char[] result = new char[nstr.length()];
		for (int i = result.length; i-- > 0; result[i] = "〇一二三四五六七八九".charAt(nstr.charAt(i) - '0'))
			;
		return new String(result);
	}

	// (〇)(一)(二)(三)...
	private static String h2(int n) {
		return String.format("(%s)", h1(n));
	}

	// 1,2,3,4,5...
	private static String h3(int n) {
		String nstr = String.valueOf(n);
		char[] result = new char[nstr.length()];
		for (int i = result.length; i-- > 0; result[i] = "0123456789".charAt(nstr.charAt(i) - '0'));
		return new String(result);
	}

	// (1),(2),(3),(4),(5),(6),(7),(8),(9),(10)
	private static String h4(int n) {
		return String.format("(%s)", h3(n));
	}
	// a、b、c、d、e、f、g、
	private static String h5(int n) {
		n=n%26;
		return "abcdefghijklmnopqrstuvwxyz".substring(n,n+1);
	};
	// (a)、(b)、
	private static String h6(int n) {
		return String.format("(%s)", h5(n));
	}

	public static String getLevelMark(int level, int order) {
		switch (level) {
		case 1:
			return h1(order);	
		case 2:
			return h2(order);
		case 3:
			return h3(order);
		case 4:
			return h4(order);
		case 5:
			return h5(order);
		case 6:
			return h6(order);	
		default:
			return null;
		}
	}
}
