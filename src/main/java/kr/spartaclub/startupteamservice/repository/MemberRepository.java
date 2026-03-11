package kr.spartaclub.startupteamservice.repository;

import kr.spartaclub.startupteamservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
