package com.example;

import jakarta.servlet.*;

public class ContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("visitCount", 0);
        System.out.println("Ứng dụng đã khởi động.");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Ứng dụng đã dừng.");
    }
}
