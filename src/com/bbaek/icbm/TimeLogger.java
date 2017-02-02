package com.bbaek.icbm;

public class TimeLogger {
	
	private long STANDARD_TIME = 0;
	private long SPEND_TIME = 0;
	
	public void start() {
		STANDARD_TIME = System.currentTimeMillis();
		SPEND_TIME = 0;
	}
	
	public void end() {
		STANDARD_TIME = 0;
		SPEND_TIME = 0;
	}
	
	private String strSpendTime() {
		long now = System.currentTimeMillis();
		long spendT = now - STANDARD_TIME;
		STANDARD_TIME = now;
		SPEND_TIME += spendT;
		return "time spend total: " + SPEND_TIME + "ms(" + convertMsToS(SPEND_TIME) + "sec) << " + spendT + "ms(" + convertMsToS(spendT) + "sec)";
	}
	
	public void logSpendTime(String title) {
		System.out.println(title + "] " + strSpendTime());
	}
	
	private float convertMsToS(long mills) {
		return (float)(mills)/1000;
	}
}
