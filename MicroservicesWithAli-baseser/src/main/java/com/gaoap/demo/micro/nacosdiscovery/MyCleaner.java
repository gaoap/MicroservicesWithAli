package com.gaoap.demo.micro.nacosdiscovery;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 防止 /urlCleaner/{id} 这样的请求地址导致限流认为不是一个请求。
 */
@Component
public class MyCleaner implements UrlCleaner {
    private String regex = "\\{[^\\}]+\\}";
    private String replace = "*";
    // 构建模式，其中compile方法还可以传入第二个参数，表示匹配方法，常用的有怱略大小写、匹配多行等。
    private Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    private String changeUrl(String url) {
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.replaceAll(replace);
        }
        return url;
    }

    @Override
    public String clean(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
         
        return this.changeUrl(s);
    }


    public static void main(String args[]) {
        // 待查找字符
        String fileName = "workspace/{test}/1234/{message";
        String regex = "\\{[^\\}]+\\}";
        String replace = "*";
        // 构建模式，其中compile方法还可以传入第二个参数，表示匹配方法，常用的有怱略大小写、匹配多行等。
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        //  System.out.println(matcher.find());
        StringBuffer stringBuffer = new StringBuffer();
//        // 将html中的下划线替换为该input标签
//        while (matcher.find()) {
//            matcher.appendReplacement(stringBuffer, replace);
////            // 匹配区间
////            String substring = fileName.substring(matcher.start(), matcher.end());
////            System.out.println(substring);
////            // 将下划线替换为uuid
////            matcher.appendReplacement(stringBuffer, matcher.group().replace(substring, replace));
//        }
//
//        // 最终结果追加到尾部
//        matcher.appendTail(stringBuffer);
        // 最终完成替换后的结果
        if (matcher.find()) {
            stringBuffer.append(matcher.replaceAll("*"));
        }
        System.out.println(stringBuffer);
    }

}

