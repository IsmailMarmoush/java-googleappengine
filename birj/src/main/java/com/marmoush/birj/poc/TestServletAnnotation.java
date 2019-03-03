package com.marmoush.birj.poc;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.marmoush.birj.config.InjectedConfiguration;

@WebServlet("/testservlet")
public class TestServletAnnotation extends HttpServlet {

	private static final long serialVersionUID = -972255682675189329L;
	@Context
	private HttpHeaders headers;
	@Inject
	@InjectedConfiguration(key = "exception.jsp.path", defaultValue = "/defaultvalue.jsp")
	private String exceptionJspPath = "/exception23.jsp";

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().write("Hello World. Ismail\n");
		// resp.getWriter().write(exceptionJspPath);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().write("Hello World. Ismail\n");
		// resp.getWriter().write(exceptionJspPath);
	}
}
