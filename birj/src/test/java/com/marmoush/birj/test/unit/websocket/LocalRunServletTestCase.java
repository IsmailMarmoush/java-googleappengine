package com.marmoush.birj.test.unit.websocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.annotation.WebServlet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.marmoush.birj.config.ConfigurationInjectionManager;
import com.marmoush.birj.config.InjectedConfiguration;
import com.marmoush.birj.poc.TestServletAnnotation;
import com.marmoush.birj.poc.websocket.MyListener;

@RunWith(Arquillian.class)
public class LocalRunServletTestCase {
  private final String USER_AGENT = "Mozilla/5.0";
  private static final String WEBAPP_SRC = "src/main/webapp";

  @Deployment(testable = false)
  public static WebArchive createDeployment() {
    WebArchive archive =
        ShrinkWrap.create(WebArchive.class, "birj-test.war").addPackage("com.marmoush.birj.poc")
            .addClass(MyListener.class).addClass(ConfigurationInjectionManager.class)
            .addClass(InjectedConfiguration.class);
    archive.addAsResource("configuration.properties");
    archive.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml")).addAsWebInfResource(
        EmptyAsset.INSTANCE, "beans.xml");

    return archive;

  }

  @Test
  public void testGet(@ArquillianResource URL baseUrl) throws Exception {
    URL url = new URL(baseUrl.toExternalForm() + "testservlet");
    System.out.println(url);
    sendGet(url);
  }

  @Test
  public void testPost(@ArquillianResource URL baseUrl) throws Exception {
    sendPost(baseUrl);
  }

  private void sendGet(URL url) throws Exception {
    HttpURLConnection con = (HttpURLConnection) url.openConnection();

    // optional default is GET
    con.setRequestMethod("GET");

    // add request header
    con.setRequestProperty("User-Agent", USER_AGENT);

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    // print result
    System.out.println(response.toString());

  }

  // HTTP POST request
  private void sendPost(URL url) throws Exception {

    HttpURLConnection con = (HttpURLConnection) url.openConnection();

    // add reuqest header
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(urlParameters);
    wr.flush();
    wr.close();

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + urlParameters);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    // print result
    System.out.println(response.toString());

  }

}
