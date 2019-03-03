package com.marmoush.birj.poc.asyncSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadTester {

	public static final int numberOfBrowsers = 1000;

	public static void main(String[] args) throws InterruptedException {
		LoadTester loadTester = new LoadTester();
		loadTester.servletLoadTester("http://localhost:8080/async2");
		loadTester.servletLoadTester("http://localhost:8080/async");
		loadTester.servletLoadTester("http://localhost:8080/sync");
	}

	public void servletLoadTester(String path) throws InterruptedException {
		long start = System.currentTimeMillis();
		System.out.println("Processing..");
		ExecutorService service = Executors.newCachedThreadPool();
		
		for (int i = 0; i < numberOfBrowsers; i++) {
			service.submit(new UrlReaderTask(path));
		}
		service.shutdown();
		service.awaitTermination(1, TimeUnit.HOURS); // or longer.
		double time = System.currentTimeMillis() - start;
		System.out.println("....Time spent was... " + time + " milliseconds");
		System.out.println("....Time spent was... " + time / 1000 + " second");
	}

	public class UrlReaderTask implements Runnable {
		private String endpoint;

		public UrlReaderTask(String s) {
			endpoint = s;
		}

		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						new URL(endpoint).openStream()));
				in.close();
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
	
}