package kr.spartaclub.startupteamservice.service;


import kr.spartaclub.startupteamservice.dto.request.CreateMemberRequest;
import kr.spartaclub.startupteamservice.dto.response.CreateMemberResponse;
import kr.spartaclub.startupteamservice.entity.Member;
import kr.spartaclub.startupteamservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
