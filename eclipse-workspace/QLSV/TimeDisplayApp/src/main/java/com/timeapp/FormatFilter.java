package com.timeapp;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FormatFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String format = request.getParameter("format");
        if (format != null && isValidFormat(format)) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute("error", "Định dạng không hợp lệ!");
            request.getRequestDispatcher("time.jsp").forward(request, response);
        }
    }

    private boolean isValidFormat(String format) {
        try {
            new SimpleDateFormat(format).format(new java.util.Date());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    
    public void init(FilterConfig fConfig) {}
    public void destroy() {}
}
