package com.timeapp;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FormatFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String format = req.getParameter("format");

        if (format != null && !format.isEmpty()) {
            try {
                new SimpleDateFormat(format); // kiểm tra định dạng
                chain.doFilter(request, response); // hợp lệ → tiếp tục
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Định dạng thời gian không hợp lệ!");
                request.getRequestDispatcher("time.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Vui lòng nhập định dạng thời gian.");
            request.getRequestDispatcher("time.jsp").forward(request, response);
        }
    }
}
