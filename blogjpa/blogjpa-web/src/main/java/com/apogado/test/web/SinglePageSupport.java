/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apogado.test.web;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * support for single page app
 
    <servlet>
        <servlet-name>SinglePageSupport</servlet-name>
        <servlet-class>com.apogado.test.web.SinglePageSupport</servlet-class>
        <init-param>
            <param-name>singlePage</param-name>
            <param-value>/index.jsp</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>SinglePageSupport</servlet-name>
        <url-pattern>/detail/*</url-pattern>
    </servlet-mapping>
    
 * init parameter: singlePage
 *
 * @author Gabriel
 */
public class SinglePageSupport extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SinglePageSupport.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = this.getInitParameter("singlePage");
        if (path == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.warning("singlePage parameter is not set");
            return;
        }

        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher(path);
        dispatcher.forward(req, resp);

    }
}
