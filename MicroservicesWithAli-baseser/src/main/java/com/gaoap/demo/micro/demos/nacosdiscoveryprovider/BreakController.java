package com.gaoap.demo.micro.demos.nacosdiscoveryprovider;

import com.gaoap.demo.micro.nacosdiscovery.WebConfig;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 熔断策略
 * <p>
 * 慢调用比例
 * 请求的响应时间大于设置的 RT 即为慢调用
 * 单位时间慢调用的比例大于阈值时，进行熔断
 * 单位时间请求数需要大于设置的最小请求数才生效（最小 5）
 * 熔断后经过熔断时长后会进入探测恢复状态，发起一个请求，请求超时继续熔断，否则恢复
 * 异常比例
 * 单位时间请求数大于最小请求数
 * 发生异常的请求比例大于阈值
 * 异常数
 * 单位统计时长内的异常数目超过阈值后进行熔断
 */
// 模拟接口
@RestController
@RequestMapping("/break")
public class BreakController {
    @Resource
    WebConfig webConfig;

    @GetMapping("/test1/{type}")
    public String test1(@PathVariable("type") Integer type) {
        String out = null;
        System.out.println("test1:" + webConfig.getServerPort());
        if (type == 0) {
            out = "正常接口调用 test1:" + webConfig.getServerPort();
            System.out.println(out);
        } else {
            if (type == 1) {
                out = "慢调用 test1:" + webConfig.getServerPort();
                System.out.println(out);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (type == 2) {
                out = "异常调用 test1:" + webConfig.getServerPort();
                System.out.println(out);
                throw new RuntimeException("接口发生异常 test1:" + webConfig.getServerPort());
            }
        }
        return "nacos-service-baseser:" + out;
    }
}
