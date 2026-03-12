package kr.spartaclub.startupteamservice.controller;


import kr.spartaclub.startupteamservice.dto.request.CreateMemberRequest;
import kr.spartaclub.startupteamservice.dto.response.CreateMemberResponse;
import kr.spartaclub.startupteamservice.dto.response.FileDownloadUrlResponse;
import kr.spartaclub.startupteamservice.dto.response.FileUploadResponse;
import kr.spartaclub.startupteamservice.dto.response.GetMemberResponse;
import kr.spartaclub.startupteamservice.service.MemberService;
import kr.spartaclub.startupteamservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 11.
 * Time: 오후 2:03
 **/

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateMemberResponse> saveMember(@RequestBody CreateMemberRequest request) {

        CreateMemberResponse response = memberService.saveMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<GetMemberResponse> getMember(@PathVariable Long memberId) {

        GetMemberResponse response = memberService.getOneMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{memberId}/profile-image")
    public ResponseEntity<FileUploadResponse> uploadProfileImage(
            @PathVariable Long memberId,
            @RequestParam("file") MultipartFile file
    ) {
        String key = memberService.updateProfileImage(memberId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FileUploadResponse(key));
    }

    @GetMapping("/{memberId}/profile-image")
    public ResponseEntity<FileDownloadUrlResponse> getDownloadUrl(
            @PathVariable Long memberId
    ) {
        String url = memberService.getProfilePresignedUrl(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(new FileDownloadUrlResponse(url));
    }
}
