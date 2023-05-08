package com.gaoap.demo.micro.permission;

import java.lang.annotation.*;

/**
 * API开放权限注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PublicApi {
    /**
     * 公开域，默认内部公开 即：只要登录了系统都能访问
     */
    PublicScopeEnum scope() default PublicScopeEnum.INTERNAL_PUBLIC;
}

