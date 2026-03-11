package kr.spartaclub.startupteamservice.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 11.
 * Time: 오후 3:04
 **/

@Getter
@NoArgsConstructor
public class CreateMemberRequest {

    private String name;
    private Integer age;
    private String mbti;
}
