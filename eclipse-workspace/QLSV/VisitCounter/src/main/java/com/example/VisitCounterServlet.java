package com.example;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class VisitCounterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // KHÔNG tăng visitCount ở đây nữa (đã xử lý trong Filter)

        // Chuyển tiếp sang JSP để hiển thị
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
