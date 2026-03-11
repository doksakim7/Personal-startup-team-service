package kr.spartaclub.startupteamservice.dto.response;


import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 11.
 * Time: 오후 4:10
 **/

@Getter
public class GetMemberResponse {
    private final String name;
    private final Integer age;
    private final String mbti;

    public GetMemberResponse(String name, Integer age, String mbti) {
        this.name = name;
        this.age = age;
        this.mbti = mbti;
    }
}
