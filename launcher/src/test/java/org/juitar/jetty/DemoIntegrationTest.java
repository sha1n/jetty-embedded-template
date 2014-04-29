package org.juitar.jetty;

import com.eclipsesource.restfuse.*;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.Header;
import com.eclipsesource.restfuse.annotation.HttpTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.eclipsesource.restfuse.Assert.assertOk;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author sha1n
 * Date: 4/23/14
 */
@RunWith(HttpJUnitRunner.class)
@Category(Integration.class)
public class DemoIntegrationTest {

    @Rule
    public Destination destination = new Destination(this, "http://localhost:8080");

    @Context
    private Response response;

    /**
     * Tests that the welcome page is returned index.html
     */
    @HttpTest(method = Method.GET, path = "/")
    public void testIndexHtmlGet() throws InterruptedException {
        assertOk(response);

        assertTrue("No way... even an error page has body...", response.hasBody());
    }

    /**
     * Tests the testServlet
     */
    @HttpTest(
            method = Method.GET,
            type = MediaType.TEXT_HTML,
            path = "/testServlet")
    public void testTestServletGet() {
        assertOk(response);

        assertTrue("No way... even an error page has body...", response.hasBody());
        final String text = response.getBody();
        assertEquals(text, "HooHaa!!! I'm an old school stupid servlet...");
    }

    /**
     * Tests the plain text format version of the REST testResource
     */
    @HttpTest(
            method = Method.GET,
            headers = {
                    @Header(name = "Accept", value = "text/plain")
            },
            path = "/api/testResource")
    public void testTestResourceGetPlainText() {
        doPlainTextGetTest();
    }

    /**
     * Tests the plain text format version of the REST testResource
     */
    @HttpTest(
            method = Method.GET,
            headers = {
                    @Header(name = "Accept", value = "text/plain")
            },
            path = "/api/queued/testResource")
    public void testTestResourceGetPlainTextQueued() {
        doPlainTextGetTest();
    }

    /**
     * Tests the JSON format version of the REST testResource
     */
    @HttpTest(
            method = Method.GET,
            headers = {
                    @Header(name = "Accept", value = "application/json")
            },
            path = "/api/testResource")
    public void testTestResourceGetJson() {
        doJsonGetTest();
    }

    /**
     * Tests the JSON format "queued" version of the REST testResource
     */
    @HttpTest(
            method = Method.GET,
            headers = {
                    @Header(name = "Accept", value = "application/json")
            },
            path = "/api/queued/testResource")
    public void testTestResourceGetJsonQueued() {
        doJsonGetTest();
    }

    private void doPlainTextGetTest() {
        assertOk(response);

        final String text = response.getBody();
        assertEquals(text, "HooHaa!!! I'm a trendy REST resource", text);
    }

    private void doJsonGetTest() {
        // Verify that the response code is 200
        assertOk(response);

        // Getting the body as a string (it's a JSON string)
        final String json = response.getBody();

        // Using GSON to parse the JSON string into a standard HashMap (stupid but enough for a demo purpose)
        final Gson gson = new GsonBuilder().create();
        @SuppressWarnings("unchecked")
        final Map<String, String> jsonProperties = (Map<String, String>) gson.fromJson(json, HashMap.class);

        // Making assertions on the JSON structure and data.
        assertTrue("'timestamp' property is missing - someone screwed up. Please consult the PM", jsonProperties.containsKey("timestamp"));
        assertEquals("HooHaa!!! I'm a trendy REST resource", jsonProperties.get("message"));
    }


}
