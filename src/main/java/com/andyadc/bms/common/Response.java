package com.andyadc.bms.common;

import java.time.LocalDateTime;

public class Response<T> {

    private final LocalDateTime timestamp;
    private Integer status;
    private String code;
    private String message;
    private T data;

    public Response() {
        this.timestamp = LocalDateTime.now();
    }

    public Response(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public Response(String code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public static <T> Response<T> of(String code, String message) {
        return new Response<>(code, message);
    }

    public static <T> Response<T> of(String code, String message, T t) {
        return new Response<>(code, message, t);
    }

    public static <T> Response<T> of(RespCode respCode) {
        return of(respCode.getCode(), respCode.getMessage());
    }

    public static <T> Response<T> of(RespCode respCode, T t) {
        return of(respCode.getCode(), respCode.getMessage(), t);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
