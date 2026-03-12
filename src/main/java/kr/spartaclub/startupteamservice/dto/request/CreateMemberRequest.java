package kr.spartaclub.startupteamservice.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMemberRequest {

    private String name;
    private Integer age;
    private String mbti;
}
