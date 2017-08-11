/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.auth;

import dk.fambagge.recipes.web.views.AuthBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rolf
 */
public class AuthenticationFilter implements Filter {

    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpRequest.getSession().getAttribute(AuthBean.USER_KEY) == null) {
            //Save requested URI
            String url = httpRequest.getRequestURI();
            
            if(httpRequest.getQueryString() != null) {
                url += "?"+httpRequest.getQueryString();
            }
            
            httpRequest.getSession().setAttribute(AuthBean.REDIRECT_KEY, url);
            System.out.println("Saving request URL: ["+url+"]");
            
            //Redirect to login
            httpResponse.sendRedirect("/login.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        config = null;
    }

}
