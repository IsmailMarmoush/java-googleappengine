package com.marmoush.birj.test.unit.websocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marmoush.birj.config.ConfigurationInjectionManager;
import com.marmoush.birj.config.InjectedConfiguration;
import com.marmoush.birj.poc.TestServletAnnotation;
import com.marmoush.birj.poc.test.CustomEndPoint;

@RunWith(Arquillian.class)
public class WebsocketTest {
	final static Logger LOG = LoggerFactory.getLogger(WebsocketTest.class);
	private static final String WEBAPP_SRC = "src/main/webapp";
	private static Object waitLock = new Object();

	private static void wait4TerminateSignal() {
		synchronized (waitLock) {
			try {
				waitLock.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	@Deployment(testable = false)
	public static Archive<?> createTestArchive() {

		Archive<?> archive = ShrinkWrap.create(WebArchive.class, "test.war")
				.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"))
				.addClass(InjectedConfiguration.class)
				.addClass(ConfigurationInjectionManager.class)
				.addClass(CustomEndPoint.class)
				.addClass(TestServletAnnotation.class)
				.addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/beans.xml"));
		LOG.info(archive.toString(Formatters.VERBOSE));
		/*
		 * .addAsResource("configuration.properties") was removed because the
		 * ConfigurationInjectManager class already define the BUNDLE_FILE_NAME
		 */
		return archive;
	}

	@RunAsClient
	@Test
	public void shouldBeAbleToCallServlet(@ArquillianResource URL baseUrl)
			throws Exception {
		System.out.println("Hellooooooooooooooooo");
		System.out.println(baseUrl);
		System.out.println("Hellooooooooooooooooo");

		try {
			System.out.println("Connecting to Servlet");

			URL url = new URL(baseUrl + "testservlet");
//			URL url=new URL("http://localhost:9090/test/testservlet");
			
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

//			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
//					conn.getOutputStream()));
//			out.write("username=name\r\n");
//			out.flush();
//			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String response;
			while ((response = in.readLine()) != null) {
				System.out.println(response);
			}
			in.close();
		} catch (MalformedURLException ex) {
			System.out.println("########" + ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("@@@@@@@@@@@@@@" + ex);
			ex.printStackTrace();
		} catch (Exception e) {
			System.out.println("$$$$$$$$$$$$$$" + e);
			e.printStackTrace();
		}
		// String body = readAllAndClose(new URL(baseUrl,
		// "/Test").openStream());
		// Assert.assertEquals(
		// "Verify that the servlet was deployed and returns the expected result",
		// "hello", body);
	}

	@RunAsClient
	@Test
	public void testWebsocketConnection() {
		WebSocketContainer container = null;//
		Session session = null;
		try {
			// Tyrus is plugged via ServiceLoader API. See notes above
			container = ContainerProvider.getWebSocketContainer();

			// WS1 is the context-root of my web.app
			// ratesrv is the path given in the ServerEndPoint annotation on
			// server implementation
			session = container.connectToServer(WSClient.class,
					URI.create("ws://localhost:8080/ratesrv"));

			wait4TerminateSignal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// String k="";
		// assertNotNull(k);
		// assertEquals(MessageFormat.format(
		// ConfigurationInjectionManager.INVALID_KEY,
		// new Object[] { "invalidParam" }), k);
	}
}