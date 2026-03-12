package kr.spartaclub.startupteamservice.service;


import kr.spartaclub.startupteamservice.dto.request.CreateMemberRequest;
import kr.spartaclub.startupteamservice.dto.response.CreateMemberResponse;
import kr.spartaclub.startupteamservice.dto.response.GetMemberResponse;
import kr.spartaclub.startupteamservice.entity.Member;
import kr.spartaclub.startupteamservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 11.
 * Time: 오후 2:28
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Transactional
    public CreateMemberResponse saveMember(CreateMemberRequest request) {

        // 로그 남기기 (요구사항)
        log.info("[API - LOG] 새로운 팀원 등록 시도: {}", request.getName());

        Member member = new Member(
                request.getName(),
                request.getAge(),
                request.getMbti()
        );

        Member savedMember = memberRepository.save(member);
        return new CreateMemberResponse(
                savedMember.getMemberId(),
                savedMember.getName()
        );
    }

    @Transactional(readOnly = true)
    public GetMemberResponse getOneMember(Long memberId) {

        // 로그 남기기 (요구사항)
        log.info("[API - LOG] 팀원 상세 조회 요청 : {}", memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 멤버입니다.")
        );

        return new GetMemberResponse(
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
    }

    @Transactional
    public String updateProfileImage(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 멤버입니다.")
        );

        String key = s3Service.uploadProfileImage(file);
        member.updateProfileImage(key);

        return key;
    }

    public String getProfilePresignedUrl(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 멤버입니다.")
        );

        if (member.getProfileImageKey() == null) {
            return null; // 또는 기본 이미지 URL 반환
        }

        // 3. 7일짜리 Presigned URL 생성
        return s3Service.getDownloadUrl(member.getProfileImageKey()).toString();
    }
}
