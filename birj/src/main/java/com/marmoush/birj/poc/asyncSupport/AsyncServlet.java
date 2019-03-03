package com.marmoush.birj.poc.asyncSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "async", value = { "/async" }, asyncSupported = true)
public class AsyncServlet extends HttpServlet {
	private static final long serialVersionUID = 4450000170131823967L;
	public static final long TIME_OUT = 500 * 1000;
	public static final long SLEEP = 500;
	public static final List<Long> ids = new ArrayList<Long>(500);

	/**
	 * Simply spawn a new thread (from the app server's pool) for every new
	 * async request. Will consume a lot more threads for many concurrent
	 * requests.
	 */
	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {

		// create the async context, otherwise getAsyncContext() will be null
		final AsyncContext ctx = req.startAsync();
		ctx.setTimeout(TIME_OUT);

		// attach listener to respond to lifecycle events of this AsyncContext
		ctx.addListener(new AsyncListener() {
			public void onComplete(AsyncEvent event) throws IOException {
				// log("onComplete called");
				ctx.getResponse().getWriter().println("Complete");
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
					// System.out.println("hello world");
					ids.add(Thread.currentThread().getId());
					Thread.sleep(SLEEP);
					// System.out.println("hello world after sleep");
					// System.out.println("---------------");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ctx.complete();
			}
		});
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Long max = Collections.max(ids);
		Long min = Collections.min(ids);
		resp.getWriter().println("The Max is:" + max);
		resp.getWriter().println("The Min is:" + min);
		resp.getWriter().println("The size is:" + ids.size());

		for (Long id : ids) {
			resp.getWriter().println(id);

		}
	}
}