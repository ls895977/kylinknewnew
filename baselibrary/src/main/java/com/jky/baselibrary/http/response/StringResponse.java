package com.jky.baselibrary.http.response;


public class StringResponse extends BaseResponse {

    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return "StringResponse{" +
                "string='" + string + '\'' +
                '}' + super.toString();
    }
}
