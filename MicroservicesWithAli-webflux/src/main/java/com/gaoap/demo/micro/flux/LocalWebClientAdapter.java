//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gaoap.demo.micro.flux;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.service.invoker.HttpClientAdapter;
import org.springframework.web.service.invoker.HttpRequestValues;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;

public final class LocalWebClientAdapter implements HttpClientAdapter {
    private final WebClient webClient;

    private LocalWebClientAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> requestToVoid(HttpRequestValues requestValues) {
        return this.newRequest(requestValues).retrieve().toBodilessEntity().then();
    }

    public Mono<HttpHeaders> requestToHeaders(HttpRequestValues requestValues) {
        return this.newRequest(requestValues).retrieve().toBodilessEntity().map(HttpEntity::getHeaders);
    }

    public <T> Mono<T> requestToBody(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            String version = attributes.getRequest().getHeader("version");
            System.out.println("requestToBodyversion:" + version);
            return this.newRequest(requestValues).retrieve().bodyToMono(bodyType).contextWrite(context -> context.put("version", version));
        } else {
            return this.newRequest(requestValues).retrieve().bodyToMono(bodyType);
        }
    }

    public <T> Flux<T> requestToBodyFlux(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return this.newRequest(requestValues).retrieve().bodyToFlux(bodyType);
    }

    public Mono<ResponseEntity<Void>> requestToBodilessEntity(HttpRequestValues requestValues) {
        return this.newRequest(requestValues).retrieve().toBodilessEntity();
    }

    public <T> Mono<ResponseEntity<T>> requestToEntity(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return this.newRequest(requestValues).retrieve().toEntity(bodyType);
    }

    public <T> Mono<ResponseEntity<Flux<T>>> requestToEntityFlux(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return this.newRequest(requestValues).retrieve().toEntityFlux(bodyType);
    }

    private RequestBodySpec newRequest(HttpRequestValues requestValues) {
        long start = System.currentTimeMillis();
        HttpMethod httpMethod = requestValues.getHttpMethod();
        Assert.notNull(httpMethod, "HttpMethod is required");
        RequestBodyUriSpec uriSpec = this.webClient.method(httpMethod);
        RequestBodySpec bodySpec = null;
        //參考SentinelInvocationHandler
  

        if (requestValues.getUri() != null) {
            bodySpec = (RequestBodySpec) uriSpec.uri(requestValues.getUri());
        } else {
            if (requestValues.getUriTemplate() == null) {
                throw new IllegalStateException("Neither full URL nor URI template");
            }

            bodySpec = (RequestBodySpec) uriSpec.uri(requestValues.getUriTemplate(), requestValues.getUriVariables());
        }

        bodySpec.headers((headers) -> {
            headers.putAll(requestValues.getHeaders());
        });
        bodySpec.cookies((cookies) -> {
            cookies.putAll(requestValues.getCookies());
        });
        bodySpec.attributes((attributes) -> {
            attributes.putAll(requestValues.getAttributes());
        });
        if (requestValues.getBodyValue() != null) {
            bodySpec.bodyValue(requestValues.getBodyValue());
        } else if (requestValues.getBody() != null) {
            Assert.notNull(requestValues.getBodyElementType(), "Publisher body element type is required");
            bodySpec.body(requestValues.getBody(), requestValues.getBodyElementType());
        }

        return bodySpec;
    }

    public static LocalWebClientAdapter forClient(WebClient webClient) {
        return new LocalWebClientAdapter(webClient);
    }
}
