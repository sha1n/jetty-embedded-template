package org.juitar.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sha1n
 * Date: 4/22/14
 */
@WebServlet(
        name = "test",
        asyncSupported = true,
        loadOnStartup = 1,
        urlPatterns = "/testServlet")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("HooHaa!!! I'm an old school stupid servlet...");
    }
}
