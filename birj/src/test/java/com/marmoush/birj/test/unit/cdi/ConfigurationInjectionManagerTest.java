package com.marmoush.birj.test.unit.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.MessageFormat;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.marmoush.birj.config.ConfigurationInjectionManager;
import com.marmoush.birj.config.InjectedConfiguration;

@RunWith(Arquillian.class)
public class ConfigurationInjectionManagerTest {
	private static final String WEBAPP_SRC = "src/main/webapp";

	@Deployment
	public static Archive<?> createTestArchive() {

		Archive<?> archive = ShrinkWrap.create(WebArchive.class, "test.war")
				.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"))
				.addClass(InjectedConfiguration.class)
				.addClass(ConfigurationInjectionManager.class)
				.addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/beans.xml"));
		/*
		 * .addAsResource("configuration.properties") was removed because the
		 * ConfigurationInjectManager class already define the BUNDLE_FILE_NAME
		 */
		return archive;
	}

	@Inject
	@InjectedConfiguration(key = "test.host.name")
	String hostName;

	@Inject
	@InjectedConfiguration(key = "invalidParam")
	String invalidParam;

	@Test
	public void test_property_injection() throws Exception {
		assertNotNull(hostName);
		assertEquals("marmoush.com", hostName);
	}

	@Test
	public void test_invalid_key() throws Exception {
		assertNotNull(invalidParam);
		assertEquals(MessageFormat.format(
				ConfigurationInjectionManager.INVALID_KEY,
				new Object[] { "invalidParam" }), invalidParam);
	}
}