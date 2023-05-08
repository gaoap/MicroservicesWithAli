package com.gaoap.demo.micro.flux;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.hamcrest.core.IsInstanceOf;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一日志处理切面
 * Created by 石磊
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class WebLogAspect {


    @Pointcut("execution(public * com.gaoap.demo.micro.flux.ToDoService.*(..))")
    public void webLog() {
        log.info("1111111111111111111111111111");
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("toShortString(){}", joinPoint.toShortString());
        log.info("toLongString(){}", joinPoint.toLongString());
        log.info("toString(){}", joinPoint.toString());
//        log.info("joinPoint.getTarget(){}", joinPoint.getTarget());
        Method method = methodSignature.getMethod();
        String resourceName =   joinPoint.toString();
        Entry entry = null;
        Object result = null;
        try {
            ContextUtil.enter(resourceName);
            entry = SphU.entry(resourceName, EntryType.OUT);
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("123",ex);
            // fallback handle
            if (!BlockException.isBlockException(ex)) {
                Tracer.traceEntry(ex, entry);
            }
            log.info(String.valueOf(method.getReturnType()));
            log.info(method.getReturnType().getName());
            log.info(String.valueOf( method.getGenericReturnType() instanceof java.lang.Class ));
               return "断流！！！";


        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
        log.info("result{}", result);
        return result;
    }
}
