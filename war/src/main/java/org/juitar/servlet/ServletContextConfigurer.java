package org.juitar.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;

/**
 * @author sha1n
 *         Date: 4/22/2014
 */
@WebListener("Servlet Context Configurer")
public class ServletContextConfigurer implements ServletContextListener {

    public static final String SESSION_COOKIE_NAME = "__app_sess_";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));

        SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        sessionCookieConfig.setName(SESSION_COOKIE_NAME);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
