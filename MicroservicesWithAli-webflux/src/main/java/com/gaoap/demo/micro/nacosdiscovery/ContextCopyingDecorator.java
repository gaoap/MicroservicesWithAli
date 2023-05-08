package com.gaoap.demo.micro.nacosdiscovery;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        try {
            RequestAttributes context = RequestContextHolder.currentRequestAttributes();  //1
            Map<String, String> previous = MDC.getCopyOfContextMap();                      //2
            return () -> {
                try {
                    System.out.println("--------------------------------");
                    RequestContextHolder.setRequestAttributes(context);     //1
                    MDC.setContextMap(previous);                         //2
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();        // 1
                    MDC.clear();                                        // 2
                }
            };
        } catch (IllegalStateException e) {
            return runnable;
        }
    }
}
