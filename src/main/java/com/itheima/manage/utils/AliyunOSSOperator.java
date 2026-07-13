package com.itheima.manage.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliyunOSSOperator {

    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    public String upload(byte[] content, String originalFilename) throws Exception {
        if (content == null) {
            throw new IllegalArgumentException("File content must not be null");
        }
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Original filename must not be blank");
        }

        String endpoint = aliyunOSSProperties.getEndpoint();
        String bucketName = aliyunOSSProperties.getBucketName();
        String region = aliyunOSSProperties.getRegion();
        String accessKeyId = aliyunOSSProperties.getAccessKeyId();
        String accessKeySecret = aliyunOSSProperties.getAccessKeySecret();

        if (accessKeyId == null || accessKeyId.isBlank()) {
            throw new IllegalStateException("Aliyun OSS accessKeyId is not configured");
        }
        if (accessKeySecret == null || accessKeySecret.isBlank()) {
            throw new IllegalStateException("Aliyun OSS accessKeySecret is not configured");
        }

        DefaultCredentialProvider credentialsProvider =
                new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        int extensionIndex = originalFilename.lastIndexOf('.');
        String extension = extensionIndex > 0 ? originalFilename.substring(extensionIndex) : "";
        String objectName = dir + "/" + UUID.randomUUID() + extension;

        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } finally {
            ossClient.shutdown();
        }

        String normalizedEndpoint = endpoint.startsWith("http://") || endpoint.startsWith("https://")
                ? endpoint
                : "https://" + endpoint;
        int protocolSeparator = normalizedEndpoint.indexOf("//");
        String protocol = normalizedEndpoint.substring(0, protocolSeparator + 2);
        String host = normalizedEndpoint.substring(protocolSeparator + 2);
        return protocol + bucketName + "." + host + "/" + objectName;
    }
}
