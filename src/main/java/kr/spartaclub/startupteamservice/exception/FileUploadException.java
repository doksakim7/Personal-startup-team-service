package kr.spartaclub.startupteamservice.exception;


/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 3. 12.
 * Time: 오후 7:16
 **/

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
