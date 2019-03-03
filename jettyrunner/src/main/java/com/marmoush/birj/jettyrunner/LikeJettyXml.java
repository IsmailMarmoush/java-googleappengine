package com.marmoush.birj.jettyrunner;

import java.lang.management.ManagementFactory;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.PropertiesConfigurationManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.LowResourceMonitor;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

/**
 * A Reference copy from @see <a href=
 * "http://www.eclipse.org/jetty/documentation/current/embedding-jetty.html"
 * >Embedded jetty configurations</a>
 * 
 * not tested yet
 */
public class LikeJettyXml {
	private static final String KEY_STORE_PATH = "./../keystore";

	public static void main(String[] args) throws Exception {
		run1();
	}

	public static void run1() throws Exception {
		String jetty_home = System.getProperty("jetty.home",
				"../../jetty-distribution/target/distribution");
		String jetty_base = System.getProperty("jetty.home",
				"../../jetty-distribution/target/distribution/demo-base");
		System.setProperty("jetty.home", jetty_home);
		System.setProperty("jetty.base", jetty_base);
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(500);
		Server server = new Server(threadPool);
		server.addBean(new ScheduledExecutorScheduler());
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);
		http_config.setRequestHeaderSize(8192);
		http_config.setResponseHeaderSize(8192);
		http_config.setSendServerVersion(true);
		http_config.setSendDateHeader(false);
		HandlerCollection handlers = new HandlerCollection();
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });
		server.setHandler(handlers);
		server.setDumpAfterStart(false);
		server.setDumpBeforeStop(false);
		server.setStopAtShutdown(true);
		MBeanContainer mbContainer = new MBeanContainer(
				ManagementFactory.getPlatformMBeanServer());
		server.addBean(mbContainer);
		ServerConnector http = new ServerConnector(server,
				new HttpConnectionFactory(http_config));
		http.setPort(8080);
		http.setIdleTimeout(30000);
		server.addConnector(http);
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(jetty_home + "/etc/keystore");
		sslContextFactory
				.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
		sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
		sslContextFactory.setTrustStorePath(jetty_home + "/etc/keystore");
		sslContextFactory
				.setTrustStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
		sslContextFactory.setExcludeCipherSuites("SSL_RSA_WITH_DES_CBC_SHA",
				"SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA",
				"SSL_RSA_EXPORT_WITH_RC4_40_MD5",
				"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
				"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
				"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");
		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());
		ServerConnector sslConnector = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, "http/1.1"),
				new HttpConnectionFactory(https_config));
		sslConnector.setPort(8443);
		server.addConnector(sslConnector);
		DeploymentManager deployer = new DeploymentManager();
		deployer.setContexts(contexts);
		deployer.setContextAttribute(
				"org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*/servlet-api-[^/]*\\.jar$");
		WebAppProvider webapp_provider = new WebAppProvider();
		webapp_provider.setMonitoredDirName(jetty_base + "/webapps");
		webapp_provider.setDefaultsDescriptor(jetty_home
				+ "/etc/webdefault.xml");
		webapp_provider.setScanInterval(1);
		webapp_provider.setExtractWars(true);
		webapp_provider
				.setConfigurationManager(new PropertiesConfigurationManager());
		deployer.addAppProvider(webapp_provider);
		server.addBean(deployer);
		StatisticsHandler stats = new StatisticsHandler();
		stats.setHandler(server.getHandler());
		server.setHandler(stats);
		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename(jetty_home + "/logs/yyyy_mm_dd.request.log");
		requestLog.setFilenameDateFormat("yyyy_MM_dd");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone("GMT");
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		requestLogHandler.setRequestLog(requestLog);
		handlers.addHandler(requestLogHandler);
		LowResourceMonitor lowResourcesMonitor = new LowResourceMonitor(server);
		lowResourcesMonitor.setPeriod(1000);
		lowResourcesMonitor.setLowResourcesIdleTimeout(200);
		lowResourcesMonitor.setMonitorThreads(true);
		lowResourcesMonitor.setMaxConnections(0);
		lowResourcesMonitor.setMaxMemory(0);
		lowResourcesMonitor.setMaxLowResourcesTime(5000);
		server.addBean(lowResourcesMonitor);
		HashLoginService login = new HashLoginService();
		login.setName("Test Realm");
		login.setConfig(jetty_base + "/etc/realm.properties");
		login.setRefreshInterval(0);
		server.addBean(login);
		server.start();
		server.join();
	}

	public static void run2() throws Exception {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(500);
		Server server = new Server(threadPool);
		server.addBean(new ScheduledExecutorScheduler());

		ContextHandlerCollection contexts = new ContextHandlerCollection();

		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });
		server.setHandler(handlers);

		server.setDumpAfterStart(false);
		server.setDumpBeforeStop(false);
		server.setStopAtShutdown(true);
		// server.addBean(getMBContainer());
		server.addConnector(getServerConnector(server));
		// server.addConnector(getSecurityConnector(http_config,server));
		server.addBean(getDeployer(contexts));
		// setStatisticsHandler(server);
		// handlers.addHandler(getLoggingHandler());
		// server.addBean(getLowResourcesMonitorBean());
		// server.addBean(getLoginBean());
		server.start();
		server.join();
	}

	public static ServerConnector getServerConnector(Server server) {

		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);
		http_config.setRequestHeaderSize(8192);
		http_config.setResponseHeaderSize(8192);
		http_config.setSendServerVersion(true);
		http_config.setSendDateHeader(false);
		HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(
				http_config);
		ServerConnector http = new ServerConnector(server,
				httpConnectionFactory);
		http.setPort(8080);
		http.setIdleTimeout(30000);
		return http;
	}

	public static DeploymentManager getDeployer(
			ContextHandlerCollection contexts) {
		DeploymentManager deployer = new DeploymentManager();
		deployer.setContexts(contexts);
		deployer.setContextAttribute(
				"org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*/servlet-api-[^/]*\\.jar$");
		WebAppProvider webapp_provider = new WebAppProvider();
		webapp_provider.setMonitoredDirName("./../webapps");
		// webapp_provider.setDefaultsDescriptor(jetty_home
		// + "/etc/webdefault.xml");
		webapp_provider.setScanInterval(1);
		webapp_provider.setExtractWars(true);
		webapp_provider
				.setConfigurationManager(new PropertiesConfigurationManager());
		deployer.addAppProvider(webapp_provider);
		return deployer;
	}

	public static MBeanContainer getMBContainer() {
		MBeanContainer mbContainer = new MBeanContainer(
				ManagementFactory.getPlatformMBeanServer());
		return mbContainer;
	}

	public static LowResourceMonitor getLowResourcesMonitorBean(Server server) {
		LowResourceMonitor lowResourcesMonitor = new LowResourceMonitor(server);
		lowResourcesMonitor.setPeriod(1000);
		lowResourcesMonitor.setLowResourcesIdleTimeout(200);
		lowResourcesMonitor.setMonitorThreads(true);
		lowResourcesMonitor.setMaxConnections(0);
		lowResourcesMonitor.setMaxMemory(0);
		lowResourcesMonitor.setMaxLowResourcesTime(5000);
		return lowResourcesMonitor;
	}

	public static HashLoginService getLoginBean() {
		HashLoginService login = new HashLoginService();
		login.setName("Test Realm");
		login.setConfig("./../etc/realm.properties");
		login.setRefreshInterval(0);
		return login;
	}

	public static ServerConnector getSecurityConnector(
			HttpConfiguration http_config, Server server) {
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(KEY_STORE_PATH);
		sslContextFactory
				.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
		sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
		sslContextFactory.setTrustStorePath(KEY_STORE_PATH);
		sslContextFactory
				.setTrustStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
		sslContextFactory.setExcludeCipherSuites("SSL_RSA_WITH_DES_CBC_SHA",
				"SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA",
				"SSL_RSA_EXPORT_WITH_RC4_40_MD5",
				"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
				"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
				"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");
		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());
		ServerConnector sslConnector = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, "http/1.1"),
				new HttpConnectionFactory(https_config));
		sslConnector.setPort(8443);
		return sslConnector;
	}

	public static void setStatisticsHandler(Server server) {
		StatisticsHandler stats = new StatisticsHandler();
		stats.setHandler(server.getHandler());
		server.setHandler(stats);
	}

	public static RequestLogHandler getLoggingHandler() {
		RequestLogHandler requestLogHandler = new RequestLogHandler();

		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename("./logs/yyyy_mm_dd.request.log");
		requestLog.setFilenameDateFormat("yyyy_MM_dd");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone("GMT");
		requestLogHandler.setRequestLog(requestLog);
		return requestLogHandler;
	}
}