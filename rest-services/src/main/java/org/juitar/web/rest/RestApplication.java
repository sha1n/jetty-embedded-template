package org.juitar.web.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;

import javax.ws.rs.ApplicationPath;

/**
 * @author sha1n
 * Date: 4/23/14
 */
@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {

    public RestApplication() {
        super();

        // Create a recursive package scanner
        PackageNamesScanner resourceFinder = new PackageNamesScanner(new String[]{"org.juitar.web.rest"}, true);
        // Register the scanner with this Application
        registerFinder(resourceFinder);
        register(JacksonFeature.class);
    }
}
