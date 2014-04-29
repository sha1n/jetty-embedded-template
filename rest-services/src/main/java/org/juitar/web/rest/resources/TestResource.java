package org.juitar.web.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author sha1n
 * Date: 4/23/14
 */
@Path("/testResource")
public class TestResource {

    public static final String MESSAGE = "HooHaa!!! I'm a trendy REST resource";

    /**
     * HTTP GET - responds to requests with 'Accept' header set to 'text/plain'
     *
     * @return a plain String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTest() {
        return MESSAGE;
    }

    /**
     * HTTP GET - responds to requests with 'Accept' header set to 'application/json'
     *
     * @return a JSON representation specified by the {@link TestJson}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TestJson getTestJson() {
        return new TestJson(MESSAGE);
    }

}
