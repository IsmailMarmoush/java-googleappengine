### Branches
Each branch has README File with different Technologies.

### Servers
1. Embedded Jetty

### Technologies
1. Platform: Java EE 1.7
2. Dependency Injection: Java CDI -> weld implementation
3. JAX-RS: Resteasy  
4. JSON data binding: JacksonFeature provided by jersey
5. XML data binding: N/A
6. Logging API: SLF4j API latest
7. Logging Impl: logback latest
8. Build System: gradle 1.8 

 
### Software Design
1. Presentation Tier: 
    1. Front controller (Jersey dispatcher servlet)
    2. Dispatcher View can be used by a CDI ConfigurationInjectionManager 
    3. Intercepting filters (Authorization)
    4. MVC on the backend (model(business layer services), views(JSP), controllers(jersey))
    5. MVC on the frontend if needed: (model (local storage or other), view (css, html,JS), controller(JS))
3. Business Logic Tier: 
    1. Business Delegate (If EJBs are used it will reduce EJB direct calls and group EJBs together as a service)
4. Database Tier: DAO


### Configurations

1. Gradle => build.gradle
2. Java CDI => com.marmoush.birj.config.MyApplicationWEB-INF/beans.xml
3. Java CDI for injectable configurations
    1.com.marmoush.birj.config.ConfigurationInjectionManager
    2.com.marmoush.birj.config.InjectedConfiguration
    3.src/main/resources/configuration.properties
3. Jersey => com.marmoush.birj.config.MyApplication
4. Jersey Controllers => @Path("...") annotated classes 
4. JSON serialization => com.marmoush.birj.config.MyApplication
5. JSP mapping => com.marmoush.birj.config.MyApplication
6. logback => src/main/resources/logback.groovy
7. jetty logging => src/main/resources/jetty-logging.properties

### Build

1. The gradle eclipse plugin uses gradle 1.5, 
I use gradle1.8 through commandline and it's actually better 
3. after adding gradle1.8/bin/gradle to PATH etc..
4. $gradle tasks --all  // to view all tasks
5. $gradle cleanEclipse eclipse // clean then add dependencies as list of folders 
6. In eclipse: project name > properties> facet > check dynamic web modules > 3.1 > runtimes tab> check glassfish > apply  
7. Repeat 5,6,7 everytime a new depedency is added
 

### Run

application path and any defined servlet => localhost:8080/<project name>/...
Jersey Application path => localhost:8080/<projectname>/restfuljersey/...
