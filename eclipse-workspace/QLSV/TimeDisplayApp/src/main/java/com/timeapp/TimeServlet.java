package com.timeapp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class TimeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String format = request.getParameter("format");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(new Date());
        System.out.println("Servlet called with format: " + format);



        request.setAttribute("time", time);
        request.setAttribute("format", format);
        RequestDispatcher rd = request.getRequestDispatcher("time.jsp");
        rd.forward(request, response);
    }
}
