package com.marmoush.birj.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.marmoush.birj.config.InjectedConfiguration;
import com.marmoush.birj.model.InjectableClass;
import com.marmoush.birj.model.profile.Person;
//import org.apache.logging.log4j.Logger;

@Singleton
@Path("/root")
public class RootController {
	@Inject
	@InjectedConfiguration(key = "exception.jsp.path", defaultValue = "/exception.jsp")
	private String exceptionJspPath;
	private static int n = 0;

	public RootController() {
		n++;
		System.out.println("root controller initialized " + n);
	}

	@GET
	@Produces("text/plain")
	public String getHelloworld() {

		if (exceptionJspPath == null) {
			return "Hello World+ injection is not working";
		}
		return "Hello World+ injection is working" + exceptionJspPath;

	}

	@Inject
	InjectableClass c;

	// @Inject
	// Person p;

	@GET
	@Path("testinjectionfactory")
	@Produces("text/plain")
	public String testHk2InjectionFactory() {
		if (c != null)
			return "injection is working " + c.getAge();
		else
			return "injection isn't working";
	}

	@GET
	@Produces("text/plain")
	@Path("session")
	public String hello(@Context HttpServletRequest req) {

		HttpSession session = req.getSession(true);
		Object foo = session.getAttribute("foo");
		if (foo != null) {
			System.out.println(foo.toString());
		} else {
			foo = "bar";
			session.setAttribute("foo", "bar");
		}
		return foo.toString();
	}

	@Inject
	Person d;

	@GET
	@Path("testinjectionbinding")
	@Produces("text/plain")
	public String testInjection() {
		if (d != null)
			return "injection is working";
		else
			return "injection isn't working";
	}

	@GET
	@Path("testheaders")
	@Produces("text/plain")
	public String testHeaders(@Context HttpHeaders headers) {
		List<MediaType> acceptableMediaTypes = headers
				.getAcceptableMediaTypes();
		String k = "Header injection working \n";
		for (MediaType mediaType : acceptableMediaTypes) {
			k += mediaType + "\n";
		}
		return k;
	}
}