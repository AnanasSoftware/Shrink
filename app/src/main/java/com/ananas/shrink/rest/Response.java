package com.ananas.shrink.rest;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class Response {
    private int error;
    @SerializedName("short")
    private String url;
    private String msg;

    public int getError() {
        return error;
    }

    public String getUrl() {
        return url;
    }

    public String getMsg() {
        return msg;
    }
}
