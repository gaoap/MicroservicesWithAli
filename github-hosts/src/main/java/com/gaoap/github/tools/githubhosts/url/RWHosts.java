package com.gaoap.github.tools.githubhosts.url;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import java.util.ArrayList;
import java.util.List;

public class RWHosts {
    /**
     * 获取host文件路径
     */
    public static String getHostFile() {
        String fileName = "";
        // 判断系统
        if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
            fileName = "/etc/hosts";
        } else {
            fileName = System.getenv("windir") + "\\system32\\drivers\\etc\\hosts";
        }
        return fileName;
    }


    public synchronized static boolean updateHost(List<String> hosts) {


        /**
         * Step1: 获取host文件
         */
        String fileName = getHostFile();
        List<String> hostFileDataLines = null;
        hostFileDataLines = new FileReader(fileName).readLines();

        List<String> newLinesList = new ArrayList<>();
        // 标识本次文件是否有更新，比如如果指定的IP和域名已经在host文件中存在，则不用再写文件
        boolean updateFlag = false;

        for (Object line : hostFileDataLines) {
            String strLine = line.toString();
            if (strLine.trim().equalsIgnoreCase("###GitHubHostsStart###")) {
                updateFlag = true;
            }
            if (strLine.trim().equalsIgnoreCase("###GitHubHostsEnd###")) {
                updateFlag = false;
                continue;
            }
            // 如果没有匹配到，直接将当前行加入代码中
            if (!updateFlag) {
                newLinesList.add(strLine);
            }
        }

        newLinesList.add("###GitHubHostsStart###");
        newLinesList.add(" ");
        newLinesList.addAll(hosts);
        newLinesList.add(" ");
        newLinesList.add("###GitHubHostsEnd###");

        /**
         * Step3: 将更新写入host文件中去
         */

        new FileWriter(fileName).writeLines(newLinesList);

        return true;
    }
}
