package com.marmoush.birj.poc.asyncSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "async2", value = { "/async2" }, asyncSupported = true)
public class AsyncServlet2 extends HttpServlet {
	private static final long serialVersionUID = 4450000170131823967L;
	public static final long TIME_OUT = 500 * 1000;
	public static final long SLEEP = 500;
	public static final List<Long> ids = new ArrayList<Long>(500);

	/**
	 * Simply spawn a new thread (from the app server's pool) for every new
	 * async request. Will consume a lot more threads for many concurrent
	 * requests.
	 */
	public void service(ServletRequest req, final ServletResponse res)
			throws ServletException, IOException {

		// create the async context, otherwise getAsyncContext() will be null
		final AsyncContext ctx = req.startAsync();
		ctx.setTimeout(TIME_OUT);

		// attach listener to respond to lifecycle events of this AsyncContext
		ctx.addListener(new AsyncListener() {
			public void onComplete(AsyncEvent event) throws IOException {
				// log("onComplete called");
			}

			public void onTimeout(AsyncEvent event) throws IOException {
				log("onTimeout called");
			}

			public void onError(AsyncEvent event) throws IOException {
				log("onError called");
			}

			public void onStartAsync(AsyncEvent event) throws IOException {
				log("onStartAsync called");
			}
		});

		// spawn some task in a background thread
		ctx.start(new Runnable() {
			public void run() {
				try {
					ids.add(Thread.currentThread().getId());
					Thread.sleep(SLEEP);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ctx.complete();
			}
		});
	}
}