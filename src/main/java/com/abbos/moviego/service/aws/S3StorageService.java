package com.abbos.moviego.service.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Amazon Web Services S3 Simple Storage Service
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageService {

    @Value("${spring.cloud.aws.s3.bucket.name}")
    private String BUCKET;

    private final S3Client s3Client;

    /**
     * @return generated unique file name
     */
    public String uploadObject(String key, MultipartFile file) {
        final var suffix = String.valueOf(System.nanoTime()).substring(10); // 5-digit suffix
        final var generatedKey = suffix + "_" + key;
        try {
            var request = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(generatedKey)
                    .contentLength(file.getSize())
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return generatedKey;
        } catch (Exception e) {
            log.warn("Failed to upload object to S3: {}", e.getMessage());
            return null;
        }
    }

    public byte[] downloadObject(String key) {
        try {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(key)
                    .build(), ResponseTransformer.toBytes()).asByteArray();
        } catch (SdkException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public void deleteObject(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(key)
                    .build());
        } catch (SdkException e) {
            log.warn(e.getMessage());
        }
    }
}
