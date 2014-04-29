package org.juitar.jetty;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author sha1n
 *         Date: 12/15/13
 */
class Arguments {

    private static final class PathValidator {
        private final String logicalName;
        private final String path;
        private final boolean required;

        public PathValidator(String logicalName, String path, boolean required) {
            this.logicalName = logicalName;
            this.path = path;
            this.required = required;
        }

        public final boolean isValid() {
            boolean valid = true;

            if (path == null) {
                if (required) {
                    printValidationError(logicalName + " is required but has not been specified!");
                    valid = false;
                } else {
                    printValidationInfo(logicalName + " path has not been specified.");
                }
            } else {
                File pathFile = new File(path);
                if (!pathFile.exists()) {
                    valid = !required;

                    try {
                        String message = logicalName + " directory is required, but the specified path \"" + pathFile.getCanonicalPath() + "\" does not exist!\r\n";
                        if (!valid) {
                            printValidationError(message);
                        } else {
                            printValidationInfo(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return valid;
        }

    }

    /* Supported Program arguments */
    static final String ARG_HTTP_PORT = "--port";
    static final String ARG_WAR_LOCATION = "--war";
    static final String ARG_CONFIG_LOCATION = "--config";

    private static final int DEFAULT_HTTP_PORT;
    private static final String DEFAULT_WAR_LOCATION;
    private static final String DEFAULT_CONFIG_LOCATION;
    private static final String DEFAULT_SHUTDOWN_TOKEN;

    static final Properties LAUNCHER_PROPERTIES;

    static final String PROPERTY_NAME_SHUTDOWN_TOKEN = "shutdown.token";
    static final String PROPERTY_NAME_HTTP_CONNECTOR_PORT = "http.connector.port";
    static final String CONFIGURATION_NAME;

    static {

        CONFIGURATION_NAME = System.getProperty("configuration", "dev");

        try {
            LAUNCHER_PROPERTIES = new Properties();
            LAUNCHER_PROPERTIES.load(Arguments.class.getResourceAsStream("/launcher-" + CONFIGURATION_NAME + ".properties"));

            DEFAULT_WAR_LOCATION = LAUNCHER_PROPERTIES.getProperty("war.location");
            DEFAULT_HTTP_PORT = Integer.parseInt(LAUNCHER_PROPERTIES.getProperty(PROPERTY_NAME_HTTP_CONNECTOR_PORT));
            DEFAULT_CONFIG_LOCATION = LAUNCHER_PROPERTIES.getProperty("config.location");
            DEFAULT_SHUTDOWN_TOKEN = LAUNCHER_PROPERTIES.getProperty(PROPERTY_NAME_SHUTDOWN_TOKEN);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load defaultArguments.properties file.", e);
        }
    }

    public static Arguments parse(String... commandLineArgs) {

        Arguments args = new Arguments();

        for (int i = 0; i < commandLineArgs.length; i++) {
            String arg = commandLineArgs[i];
            switch (arg) {
                case ARG_CONFIG_LOCATION:
                    args.setConfigLocation(commandLineArgs[++i]);
                    break;
                case ARG_WAR_LOCATION:
                    args.setWarLocation(commandLineArgs[++i]);
                    break;
                case ARG_HTTP_PORT:
                    args.setPort(Integer.parseInt(commandLineArgs[++i]));
                    break;
                default:
                    args.addStaticFolderLocation(arg);
                    break;
            }
        }

        return args;
    }

    private String shutdownToken = DEFAULT_SHUTDOWN_TOKEN;
    private int port = DEFAULT_HTTP_PORT;
    private String configLocation = DEFAULT_CONFIG_LOCATION;
    private String warLocation = DEFAULT_WAR_LOCATION;
    private final List<String> staticResourcesPaths = new ArrayList<>();

    public String getLog4JConfigPath() {
        return getConfigLocation() + "/log4j-" + CONFIGURATION_NAME + ".properties";
    }

    public String getShutdownToken() {
        return shutdownToken;
    }

    public void addStaticFolderLocation(String resourcePath) {
        staticResourcesPaths.add(resourcePath);
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public String getWarLocation() {
        return warLocation;
    }

    private void setWarLocation(String warLocation) {
        this.warLocation = warLocation;
    }

    public List<String> getStaticResourcesPaths() {
        return staticResourcesPaths;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    public void validate() {

        printValidationInfo("Validating input arguments...");

        boolean valid = new PathValidator("war", getWarLocation(), true).isValid();
        valid &= new PathValidator("config", getConfigLocation(), true).isValid();
        for (String staticsPath : getStaticResourcesPaths()) {
            valid &= new PathValidator("static resource", staticsPath, false).isValid();
        }

        // The default connector used by Jetty (ServerConnector) succeeds to start even if the port
        // is being used by another process. Therefore we do the check explicitly.
        try {
            new ServerSocket(port).close();
        } catch (Exception e) {
            printValidationError("Failed to bind local server socket with port " + port + ". Could be that you already run another instance of Jetty on the same port.");
            e.printStackTrace();
            valid = false;
        }

        if (!valid) {
            printValidationError("Arguments validation failed!");
            throw new IllegalArgumentException("Please review the messages above.");
        }

        printValidationInfo("Arguments validated successfully.");
    }

    private static void printValidationError(String message) {
        System.err.println("VALIDATION:\t" + message);
        System.err.flush();
    }

    private static void printValidationInfo(String message) {
        System.out.println("VALIDATION:\t" + message);
        System.out.flush();
    }
}