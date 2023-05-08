package com.gaoap.demo.micro.demos.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

/**
 * 授权异常处理
 * 第一步:我们发现该异常是我们自定义BlockExceptionHandler实现类所报的异常,我们通过debug发现该异常类型为AuthorityException
 *  第二步:设置AuthorityException异常的前端显示内容
 */
@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse response,
                       BlockException e) throws Exception {
        //设置响应状态码
        response.setStatus(429);
        //设施设备响应编码格式
        response.setContentType("text/html;charset=UTF-8");
        //设置输出流对象
        PrintWriter out = response.getWriter();
        String msg ="访问太过频繁,反应不过来啦!666";//默认为限流异常(直接和关联方式)
        if (e instanceof DegradeException){//熔断异常
            msg = "服务不可用,稍后再访问吧!";
        }
        if (e instanceof AuthorityException){//授权异常
            msg = "您已被拉入黑名单,无法访问该资源";
        }
        out.println(msg);
        out.flush();
        out.close();
    }
}
