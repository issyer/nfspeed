package com.example.commonutil.util.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author sunwanghe
 * @Date 2022/02/05
 */
@Data
@Component
@ConfigurationProperties(prefix = "kdn")
public class KdnConfig {
    private String userId;
    private String apiKey;
    private String kdQueryUrl;
}
