package org.juitar.jetty;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author sha1n
 * Date: 4/24/14
 */
public class Stop {

    public static void main(String... args) throws IOException {
        URL url = new URL("http://localhost:8080/shutdown?token=secretToken");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.getResponseCode();
    }
}
