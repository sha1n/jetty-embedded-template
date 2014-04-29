package org.juitar.web.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author sha1n
 * Date: 4/23/14
 */
@Path("/queued/testResource")
public class QueuedTestResource {

    private static final Executor asyncProcessingExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static final String MESSAGE = "HooHaa!!! I'm a trendy REST resource";

    /**
     * HTTP GET - responds to requests with 'Accept' header set to 'text/plain' asynchronously using a shared thread-pool.
     * Returns a JSON representation specified by the {@link TestJson}
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void getTest(@Suspended final AsyncResponse asyncResponse) {
        execute(MESSAGE, asyncResponse);
    }

    /**
     * HTTP GET - responds to requests with 'Accept' header set to 'application/json' asynchronously using a shared thread-pool.
     * Returns a JSON representation specified by the {@link TestJson}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getTestJson(@Suspended final AsyncResponse asyncResponse) {
        execute(new TestJson(MESSAGE), asyncResponse);
    }

    private void execute(final Object entity, final AsyncResponse asyncResponse) {
        asyncProcessingExecutor.execute(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(entity);
            }
        });
    }

}
