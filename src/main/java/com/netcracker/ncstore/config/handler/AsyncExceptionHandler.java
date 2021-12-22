package com.netcracker.ncstore.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Uncaught exception in async task. ");
        stringBuilder.append("Exception message - ").append(ex.getMessage()).append(". ");
        stringBuilder.append("Method name - ").append(method.getName()).append(". ");
        for (Object param : params) {
            stringBuilder.append("Parameter value - ").append(param).append(". ");
        }

        log.warn(stringBuilder.toString());
    }
}
