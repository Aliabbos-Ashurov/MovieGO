package com.abbos.moviego.service.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@SuppressWarnings("unchecked")
@DisplayName("AWS S3 test")
class S3StorageServiceTest {

    @InjectMocks
    private S3StorageService s3StorageService;

    @Mock
    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        s3StorageService.BUCKET = "test-bucket";
        s3StorageService.BASE_LINK = "https://bucket-url/";
    }

    @Test
    void uploadObject_success_returns_generated_key() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testfile.txt",
                "text/plain",
                "Hello World".getBytes());

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String key = "myfile";

        String result = s3StorageService.uploadObject(key, file);

        assertNotNull(result);
        assertTrue(result.matches(".{5}_myfile.txt"));
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void upload_object_exception_returns_null() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "fail.txt",
                "text/plain",
                "fail".getBytes());

        doThrow(new RuntimeException("fail")).when(s3Client).putObject(
                any(PutObjectRequest.class),
                any(RequestBody.class)
        );

        String result = s3StorageService.uploadObject("failKey", file);

        assertNull(result);
        verify(s3Client).putObject(
                any(PutObjectRequest.class),
                any(RequestBody.class)
        );
    }

    @Test
    void downloadObject_success_returnsBytes() {
        byte[] expectedData = "data".getBytes();

        ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes.fromByteArray(GetObjectResponse.builder().build(), expectedData);
        when(s3Client.getObject(any(GetObjectRequest.class), any(ResponseTransformer.class))).thenReturn(responseBytes);

        byte[] actual = s3StorageService.downloadObject("someKey");

        assertArrayEquals(expectedData, actual);
        verify(s3Client).getObject(any(GetObjectRequest.class), any(ResponseTransformer.class));
    }

    @Test
    void downloadObject_exception_returnsNull() {
        when(s3Client.getObject(any(GetObjectRequest.class), any(ResponseTransformer.class))).thenThrow(S3Exception.builder().message("fail").build());

        byte[] result = s3StorageService.downloadObject("key");

        assertNull(result);
        verify(s3Client).getObject(any(GetObjectRequest.class), any(ResponseTransformer.class));
    }

    @Test
    void deleteObject_success() {
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(DeleteObjectResponse.builder().build());

        s3StorageService.deleteObject("key");

        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void deleteObject_exception_logsWarning() {
        doThrow(S3Exception.builder().message("fail").build()).when(s3Client).deleteObject(any(DeleteObjectRequest.class));

        s3StorageService.deleteObject("key");

        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }
}