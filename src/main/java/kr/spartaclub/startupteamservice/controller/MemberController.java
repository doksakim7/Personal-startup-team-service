package kr.spartaclub.startupteamservice.controller;


import kr.spartaclub.startupteamservice.dto.request.CreateMemberRequest;
import kr.spartaclub.startupteamservice.dto.response.CreateMemberResponse;
import kr.spartaclub.startupteamservice.dto.response.FileDownloadUrlResponse;
import kr.spartaclub.startupteamservice.dto.response.FileUploadResponse;
import kr.spartaclub.startupteamservice.dto.response.GetMemberResponse;
import kr.spartaclub.startupteamservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateMemberResponse> registerMember(@RequestBody CreateMemberRequest request) {

        CreateMemberResponse response = memberService.registerMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<GetMemberResponse> getMember(@PathVariable Long memberId) {

        GetMemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{memberId}/profile-image")
    public ResponseEntity<FileUploadResponse> uploadProfileImage(
            @PathVariable Long memberId,
            @RequestParam("profileImage") MultipartFile profileImage
    ) {
        String profileImageKey = memberService.updateProfileImage(memberId, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FileUploadResponse(profileImageKey));
    }

    @GetMapping("/{memberId}/profile-image")
    public ResponseEntity<FileDownloadUrlResponse> getDownloadUrl(
            @PathVariable Long memberId
    ) {
        String url = memberService.generateProfilePresignedUrl(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(new FileDownloadUrlResponse(url));
    }
}
