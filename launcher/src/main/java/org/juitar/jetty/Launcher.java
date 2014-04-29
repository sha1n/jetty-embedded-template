package org.juitar.jetty;

/**
 * @author sha1n
 *         Date: 12/15/13
 */
public class Launcher {

    public static void main(String[] args) throws Exception {
        try {
            Arguments arguments = Arguments.parse(args);
            arguments.validate();

            Application application = new Application(arguments);
            application.launch();

        } catch (Exception e) {
            handleException(e);
        }
    }

    private static void handleException(Exception e) {
        Throwable realException = e;
        String message = e.getMessage(); // Keep the original message
        if (e.getCause() != null) {
            realException = e.getCause();
        }

        System.err.println(message);
        realException.printStackTrace();

        usage();
    }

    private static void usage() {
        System.out.println();
        System.out.println("Usage: Launcher \r\n\t["
                + Arguments.ARG_WAR_LOCATION
                + " <war dir (default to ./war)]\r\n\t["
                + Arguments.ARG_CONFIG_LOCATION
                + " <config dir (default to ./config)>]\r\n\t["
                + Arguments.ARG_HTTP_PORT
                + " <port number (default to 8080)>]\r\n\t"
                + "[additional-static-folders...]");

        System.exit(1);
    }

}
