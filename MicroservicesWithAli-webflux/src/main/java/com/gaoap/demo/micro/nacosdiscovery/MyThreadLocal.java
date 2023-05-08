package com.gaoap.demo.micro.nacosdiscovery;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * 单实例
 */
public class MyThreadLocal {

    private MyThreadLocal() {

    }

    private static ThreadLocal<Map<String,String>> threadLocal=new TransmittableThreadLocal<Map<String,String>>(){
        public Map<String,String> initialValue(){
            return new HashMap<>();
        }
        @Override
        protected Map<String, String> childValue(Map<String, String> parentValue) {
            return new HashMap<>(parentValue);
        }

        @Override
        public Map<String, String> copy(Map<String, String> parentValue) {
            return new HashMap<>(parentValue);
        }
    };


    public static void set(String str) {

        threadLocal.get().put("version",str);
    }

    public static String get() {
        return threadLocal.get().get("version");
    }

    /**
     * 本质上是调用当前线程的remove，不会对其他线程产生影响
     */
    public static void remove() {

        threadLocal.remove();
    }
}
