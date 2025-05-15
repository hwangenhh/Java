package com.example;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class VisitFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        ServletContext context = request.getServletContext();
        synchronized (context) {	
            if (session.getAttribute("visited") == null) {
                Integer count = (Integer) context.getAttribute("visitCount");
                if (count == null) count = 0;
                context.setAttribute("visitCount", count + 1);
                session.setAttribute("visited", true);
            }
        }

        chain.doFilter(request, response);
    }
}
