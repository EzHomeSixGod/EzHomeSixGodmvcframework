package com.ezhomesixgod.filter;

import com.ezhomesixgod.entity.EzWrapperResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.ServiceRegistry;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author renyang
 * @description
 * @createTime 2018-09-26 10:10
 */
public class JspFilter implements Filter {
    private static final Logger logger =LoggerFactory.getLogger(JspFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();

        filterChain.doFilter(req,resp);
    }

    @Override
    public void destroy() {

    }
}
