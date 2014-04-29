package org.juitar.jetty;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.*;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author sha1n
 *         Date: 4/22/2014
 */
class Application {

    private final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final String SYS_PROPERTY_JETTY_LOGS = "jetty.logs";
    private static final String SYS_PROPERTY_JETTY_HOME = "jetty.home";
    private static final String SYS_PROPERTY_JETTY_PORT = "jetty.port";

    private final Arguments arguments;

    Application(Arguments arguments) {
        this.arguments = arguments;
    }

    void launch() throws Exception {
        initSystemProperties();
        configureLog4J();
        startJetty();
    }

    /**
     * This method logs to System.out/err since the Log4J system is not initialized yet
     * and depends on the "jetty.logs" system property which is set in this method.
     */
    private void initSystemProperties() throws IOException {
        String userDir = System.getProperty("user.dir");

        System.out.println("Paths are relative to \"" + userDir + "\"");

        // Set jetty.port system property
        System.setProperty(SYS_PROPERTY_JETTY_PORT, String.valueOf(arguments.getPort()));

        // Set jetty.home system property
        String jettyHome = System.getProperty(SYS_PROPERTY_JETTY_HOME, userDir);
        System.out.println(SYS_PROPERTY_JETTY_HOME + "=" + jettyHome);
        System.setProperty(SYS_PROPERTY_JETTY_HOME, jettyHome);

        // Set jetty.logs system property
        String jettyLogs = System.getProperty(SYS_PROPERTY_JETTY_LOGS, jettyHome + File.separator + "logs");

        // Create logs directory if doesn't exist
        Path path = Files.createDirectories(Paths.get(jettyLogs));
        File file = path.toFile();
        if (!(file.setReadable(true) && file.setWritable(true))) {
            System.err.println("Failed to set appropriate permissions on " + path.toString());
        }

        System.out.println(SYS_PROPERTY_JETTY_LOGS + "=" + path.toString());
        System.setProperty(SYS_PROPERTY_JETTY_LOGS, jettyLogs);
    }

    private void configureLog4J() throws IOException {
        File log4jConfigFile = new File(arguments.getLog4JConfigPath());
        System.out.println("Using Log4J configuration file: " + log4jConfigFile.getCanonicalPath());
        PropertyConfigurator.configure(log4jConfigFile.getAbsolutePath());
    }

    private void startJetty() throws Exception {

        Server server = new Server();

        HandlerCollection handlerCollection = new HandlerCollection();

        server.setHandler(handlerCollection);

        // Create and add web-application context handler
        handlerCollection.addHandler(createWebAppContext());

        // Configure the Jetty server object
        configureServer(server, arguments, "/etc/jetty-embedded.xml");

        // Add shutdown handler
        ShutdownHandler shutdownHandler = new ShutdownHandler(arguments.getShutdownToken(), true, false);
        shutdownHandler.setHandler(handlerCollection);

        server.setHandler(shutdownHandler);
        server.setStopTimeout(3000);
        server.setStopAtShutdown(true);

        long start = System.currentTimeMillis();

        server.start();

        long end = System.currentTimeMillis();
        LOGGER.info("Server is ready! (Boot time " + (end - start) / 1000 + " seconds)");

//        server.dump(System.err);
        server.join();
    }

    private void configureServer(Server server, Arguments arguments, String... jettyServerConfigXmls) throws Exception {
        for (String jettyXml : jettyServerConfigXmls) {
            final Path path = Paths.get(arguments.getConfigLocation(), jettyXml);
            final File file = path.toFile();
            if (file.exists()) {
                XmlConfiguration xmlConfiguration = new XmlConfiguration(new FileInputStream(file));
                xmlConfiguration.configure(server);
            } else {
                throw new IllegalArgumentException("Configuration file '" + jettyXml + "' does not exist.");
            }
        }
    }

    private WebAppContext createWebAppContext() {
        LOGGER.info("Configuring war from: " + arguments.getWarLocation());
        WebAppContext webAppContext = new WebAppContext(arguments.getWarLocation(), "/");

        List<String> staticResourcesPaths = arguments.getStaticResourcesPaths();
        if (!staticResourcesPaths.isEmpty()) {
            LOGGER.info("Using static data from locations:");
            for (String staticFolder : staticResourcesPaths) {
                LOGGER.info(staticFolder);
            }
        }

        staticResourcesPaths.add(arguments.getWarLocation());
        ResourceCollection resourceCollection = new ResourceCollection(
                staticResourcesPaths.toArray(new String[staticResourcesPaths.size()])
        );

        webAppContext.setBaseResource(resourceCollection);
        webAppContext.setDescriptor(arguments.getWarLocation() + "/WEB-INF/web.xml");
        webAppContext.setConfigurations(new Configuration[]{
                new AnnotationConfiguration(),
                new WebXmlConfiguration(),
                new WebInfConfiguration(),
                new PlusConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                new EnvConfiguration()});

        webAppContext.setContextPath("/");
        webAppContext.setParentLoaderPriority(true);

        // Set annotation scanning pattern
        webAppContext.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/classes/.*");

        return webAppContext;
    }

}
