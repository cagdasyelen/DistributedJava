package edu.utexas.util;

public class Timer {

	private String name;
	private long startTime;

	public Timer(String name) {
		this.name = name;
	}

	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	public void stop() {
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		if (elapsedTime > 0) {
			System.out.println(name + ":" + elapsedTime);
		}
	}

}
