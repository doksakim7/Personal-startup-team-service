package kr.spartaclub.startupteamservice.service;


import io.awspring.cloud.s3.S3Template;
import kr.spartaclub.startupteamservice.global.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);
    private static final String UPLOAD_DIRECTORY = "uploads/";

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        String key = createPath(file.getOriginalFilename());

        try {
            log.info("[API - LOG] S3 업로드 시도: key = {}", key);
            s3Template.upload(bucket, key, file.getInputStream());
            return key;
        } catch (IOException e) {
            log.error("[API - LOG] S3 업로드 실패: key = {}", key, e);
            throw new FileUploadException("S3 파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    public URL generatePresignedUrl(String key) {
        log.info("[API - LOG] Presigned URL 생성 중: key = {}", key);
        return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
    }

    private String createPath(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        return UPLOAD_DIRECTORY + uuid + "_" + originalFilename;
    }
}
