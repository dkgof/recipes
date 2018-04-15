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
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.omnifaces.filter.HttpFilter;
import org.omnifaces.util.Servlets;

/**
 *
 * @author rolf
 */
@WebFilter("/restricted/*")
public class AuthenticationFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, HttpSession session, FilterChain chain) throws ServletException, IOException {
        if (session.getAttribute(AuthBean.USER_KEY) == null) {
            //Save requested URI
            String url = request.getRequestURI();
            
            if(request.getQueryString() != null) {
                url += "?"+request.getQueryString();
            }
            
            session.setAttribute(AuthBean.REDIRECT_KEY, url);
            System.out.println("Saving request URL: ["+url+"]");
            
            //Redirect to login
            Servlets.facesRedirect(request, response, "login.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }
}
