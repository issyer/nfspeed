package com.example.commonutil.util.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author sunwanghe
 * @Date 2022/02/03
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;

    private int port;

    private String accessKey;

    private String secretKey;

    private Boolean secure;

    private String bucketName;

    @Bean
    @RefreshScope
    public MinioClient getMinioClient() throws InvalidEndpointException, InvalidPortException {
        return new MinioClient(endpoint,port, accessKey, secretKey,secure);
    }

}
