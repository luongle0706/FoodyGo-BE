package com.foodygo.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "aws.s3")
@NoArgsConstructor
@AllArgsConstructor
public class S3Properties {
    // Getters và Setters
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucketName;
}
