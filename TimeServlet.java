package com.timeapp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String format = request.getParameter("format");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String currentTime = sdf.format(new Date());

        request.setAttribute("time", currentTime);
        request.setAttribute("format", format); // Gửi lại format để hiển thị
        request.getRequestDispatcher("time.jsp").forward(request, response);

    }
}
