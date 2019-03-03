package com.marmoush.birj.jettyrunner;

import java.io.File;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyRunner {
	public static final String WAR_PATH = "./../birj/build/libs/birj-1.0.war";

	public static void main(String[] args) throws Exception {
		new JettyRunner().run1();
	}

	public void run1() throws Exception {
		Server server = new Server(8080);
		ClassList clist = ClassList.setServerDefault(server);
		// clist.addAfter(FragmentConfiguration.class.getName(),
		// EnvConfiguration.class.getName(),
		// PlusConfiguration.class.getName());
		clist.addBefore(JettyWebXmlConfiguration.class.getName(),
				AnnotationConfiguration.class.getName());

		WebAppContext context = new WebAppContext();
		context.setWar(WAR_PATH);

		context.setParentLoaderPriority(true);
		context.setTempDirectory(new File("/tmp/jettywar"));
		server.setHandler(context);

		server.start();
		server.dump(System.out);
		server.join();
	}
}
