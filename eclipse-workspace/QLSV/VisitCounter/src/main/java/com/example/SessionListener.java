package com.example;

import jakarta.servlet.http.*;

public class SessionListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Phiên mới được tạo: " + se.getSession().getId());
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Phiên bị hủy: " + se.getSession().getId());
    }
}
