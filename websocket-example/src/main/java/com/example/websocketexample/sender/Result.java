package com.example.websocketexample.sender;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private boolean status;

    private Integer code;

    private String message;

    private T data;

    public static Result ok() {
        Result result = new Result();
        result.setCode(HttpStatus.OK.value());
        result.setStatus(true);
        result.setMessage("request success");
        result.setData(null);
        return result;
    }

    public static Result ok(Object data) {

        Result result = Result.ok();
        result.setData(data);
        System.out.println("result ok -->>>>>" + result);
        return result;
    }

    public static Result ok(String msg) {
        Result result = Result.ok();
        result.setMessage(msg);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setStatus(false);
        result.setData(null);
        return result;
    }

    public static Result error(String msg) {
        Result result = Result.error();
        result.setMessage(msg);
        return result;
    }

    public static Result error(Integer code) {
        Result result = Result.error();
        result.setCode(code);
        return result;
    }

    public static Result error(Integer code, String msg) {
        Result result = Result.error();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
