package kr.spartaclub.startupteamservice.dto.response;


import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 11.
 * Time: 오후 3:05
 **/

@Getter
public class CreateMemberResponse {

    private final Long memberId;
    private final String name;


    public CreateMemberResponse(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }
}
