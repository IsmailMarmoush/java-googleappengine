package com.marmoush.birj.poc.asyncSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/sync", asyncSupported = false)
public class SyncServlet extends HttpServlet {
	private static final long serialVersionUID = -665341751802536093L;
	public static final long TIME_OUT = 500 * 1000;
	public static final long SLEEP = 500;
	public static final List<Long> ids = new ArrayList<Long>(500);
	static int k = 0;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * I'm using thread here even that I don't need to, because I wanted to
		 * make both servlets run a different thread other than what they're
		 * currently in, just to make sure situation is matched as mush as
		 * possible
		 */
		// Thread d = new Thread(new Runnable() {
		// public void run() {
//		System.out.println("---------------  " + k++);
//		System.out.println("hello world");
		resp.getWriter().println("welcome to sync method, processing...");
		try {
			ids.add(Thread.currentThread().getId());
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("hello world after sleep");
		//
		// }
		// });
		// d.run();
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