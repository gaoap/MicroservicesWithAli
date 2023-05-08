package com.gaoap.demo.micro.nacosdiscovery;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class WebConfig {
    int serverPort;
}
