package com.gaoap.demo.micro.demos.nacosdiscoveryconsumer;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.io.IOException;
import java.util.Enumeration;

@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {

            HttpServletRequest httpServletRequest = attributes.getRequest();
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = httpServletRequest.getHeader(name);
                    // 跳过 content-length,防止报错Feign报错feign.RetryableException: too many bytes written executing
                    if (name.equals("content-length")) {
                        continue;
                    }
                    request.getHeaders().add(name, values);
                }
            }
        }
        return execution.execute(request, body);
    }

}


