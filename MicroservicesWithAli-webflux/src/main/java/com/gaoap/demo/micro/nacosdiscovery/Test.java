package com.gaoap.demo.micro.nacosdiscovery;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class Test {

    static String KEY = "TEST_CONTEXT_KEY";
    static String KEY2 = "TEST_CONTEXT_KEY2";

    public static void main(String[] args) {
        Flux<String> flux = convert("hello", Flux.just(1, 2, 3));
        flux
                .contextWrite(Context.of(KEY, "Outside"))
                .subscribe(v -> System.out.println(v));

//        Mono.empty().contextWrite(Context.of("version",  "123"));

        Mono.deferContextual(ctx->Mono.just(ctx.get(KEY) + " " + ctx.get(KEY2))).subscribe(v -> System.out.println(v));
    }

    public static Flux<String> convert(String prefix, Flux<Integer> publisher) {
        return publisher.map(v -> prefix + " " + v)
                .contextWrite(Context.of(KEY, "NotUsed"))
                .flatMap(v -> Mono.deferContextual(ctx->Mono.just(ctx.get(KEY) + " " + ctx.get(KEY2) + " " + v)))
                .contextWrite(context -> context.put(KEY2, "Inside"))
                .flatMap(v -> Mono.deferContextual(ctx -> Mono.just(ctx.get(KEY) + " " + v)));
    }
}
