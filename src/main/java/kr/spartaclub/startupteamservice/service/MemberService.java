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

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Transactional
    public CreateMemberResponse registerMember(CreateMemberRequest request) {
        log.info("[API - LOG] 새로운 팀원 등록 시작: name = {}", request.getName());

        Member member = new Member(
                request.getName(),
                request.getAge(),
                request.getMbti()
        );

        Member savedMember = memberRepository.save(member);

        log.info("[API - LOG] 새로운 팀원 등록 완료: memberId = {}", savedMember.getMemberId());
        return new CreateMemberResponse(
                savedMember.getMemberId(),
                savedMember.getName()
        );
    }

    @Transactional(readOnly = true)
    public GetMemberResponse getMember(Long memberId) {
        log.info("[API - LOG] 팀원 상세 조회 요청 : memberId = {}", memberId);

        Member member = findMemberById(memberId);

        return new GetMemberResponse(
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
    }

    @Transactional
    public String updateProfileImage(Long memberId, MultipartFile file) {

        // 파일이 비어있는지 체크
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        log.info("[API - LOG] 프로필 이미지 업데이트 시작 : memberId = {}", memberId);

        Member member = findMemberById(memberId);

        String profileImageKey = s3Service.upload(file);
        member.updateProfileImage(profileImageKey);

        log.info("[API - LOG] 프로필 이미지 업데이트 완료 : key = {}", profileImageKey);
        return profileImageKey;
    }

    @Transactional(readOnly = true)
    public String generateProfilePresignedUrl(Long memberId) {
        log.info("[API - LOG] Presigned URL 생성 요청 : memberId = {}", memberId);

        Member member = findMemberById(memberId);

        if (member.getProfileImageKey() == null) {
            log.warn("[API - LOG] 등록된 프로필 이미지가 없습니다: memberId = {}", memberId);
            return null;
        }

        String presignedUrl = s3Service.generatePresignedUrl(member.getProfileImageKey()).toString();
        log.info("[API - LOG] Presigned URL 생성 완료");
        return presignedUrl;
    }

    // 공통 예외 로직 분리
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            log.error("[API - LOG] 존재하지 않는 멤버 조회 시도: id = {}", memberId);
            return new IllegalArgumentException("존재하지 않는 멤버입니다.");
        });
    }
}
