package kr.spartaclub.startupteamservice.controller;


import kr.spartaclub.startupteamservice.dto.response.FileDownloadUrlResponse;
import kr.spartaclub.startupteamservice.dto.response.FileUploadResponse;
import kr.spartaclub.startupteamservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 12.
 * Time: 오후 2:50
 **/

@RestController
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @PostMapping("/files/upload")
    public ResponseEntity<FileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        String key = s3Service.upload(file);
        return ResponseEntity.ok(new FileUploadResponse(key));
    }

    @GetMapping("/files/download-url")
    public ResponseEntity<FileDownloadUrlResponse> getDownloadUrl(@RequestParam String key) {
        URL url = s3Service.getDownloadUrl(key);
        return ResponseEntity.ok(new FileDownloadUrlResponse(url.toString()));
    }
}
