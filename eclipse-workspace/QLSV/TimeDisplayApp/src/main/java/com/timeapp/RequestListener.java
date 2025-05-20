package com.timeapp;

import jakarta.servlet.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestListener implements ServletRequestListener {
    private static AtomicInteger requestCount = new AtomicInteger(0);

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        int count = requestCount.incrementAndGet();
        System.out.println("Số request đã xử lý: " + count);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {}
}
