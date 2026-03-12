package kr.spartaclub.startupteamservice.dto.response;


import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 12.
 * Time: 오후 2:48
 **/

@Getter
public class FileDownloadUrlResponse {

    private final String url;

    public FileDownloadUrlResponse(String url) {
        this.url = url;
    }
}
