package com.abbos.moviego.service.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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

import java.util.UUID;

/**
 * Service for handling file operations with Amazon S3 storage.
 * <p>
 * Provides methods to upload, download, and delete files from an S3 bucket.
 * Automatically generates unique file keys to avoid naming conflicts.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageService {

    @Value("${spring.cloud.aws.s3.bucket.name}")
    public String BUCKET;

    @Value("${spring.cloud.aws.s3.base-link}")
    public String BASE_LINK;

    private final S3Client s3Client;

    /**
     * Uploads a file to S3 with a uniquely generated key.
     *
     * @param key  the base key (e.g., file name or identifier)
     * @param file the file to upload
     * @return the generated unique key, or {@code null} if upload fails
     */
    public String uploadObject(String key, MultipartFile file) {
        final var generatedKey = generateKey(key, file.getOriginalFilename());
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

    /**
     * Downloads a file from S3 as a byte array.
     *
     * @param key the key of the file in S3
     * @return the file content as bytes, or {@code null} if not found
     */
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

    /**
     * Deletes a file from S3 by its key.
     *
     * @param key the key of the file to delete
     */
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


    /**
     * Generates a unique key using a short uuid prefix and the file extension.
     * <p>
     * Format: {@code [suffix]_[key].[extension]}
     *
     * @param key      the base key
     * @param fileName the original file name to extract the extension
     * @return the generated unique key
     */
    private String generateKey(String key, String fileName) {
        String random = UUID.randomUUID().toString();
        String randomPart = random.substring(random.length() - 5); // 5-digit
        return "%s_%s.%s".formatted(randomPart, key, FilenameUtils.getExtension(fileName));
    }
}
