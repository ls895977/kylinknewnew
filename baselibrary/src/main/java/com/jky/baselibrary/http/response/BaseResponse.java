package com.jky.baselibrary.http.response;

import java.util.List;
import java.util.Map;

/**
 * 通用网络响应类
 */
public class BaseResponse {
    private int code;
    private String message;
    private Map<String, List<String>> headers;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
//                ", headers=" + headers +
                '}';
    }
}
