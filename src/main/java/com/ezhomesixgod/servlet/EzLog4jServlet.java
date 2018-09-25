package com.ezhomesixgod.servlet;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author renyang
 * @description
 * @createTime 2018-09-25 14:06
 */
public class EzLog4jServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void init() throws ServletException {
        String prefix = getServletContext().getRealPath("/");
        prefix = prefix.replace("//", "/");
        String file = getInitParameter("log4jContextLocation");
        System.out.println("log4jContextLocation"+file);
        // if the log4j-init-file is not set, then no point in trying
        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
    }
}
