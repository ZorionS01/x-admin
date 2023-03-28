package com.szw.demo.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Szw 2001
 * @Date 2023/3/14 18:01
 * @Slogn 致未来的你！
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    //成功
    public static <T> Result<T> success(){
        return new Result<>(20000,"success",null);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(20000,"success",data);
    }

    public static <T> Result<T> success(T data,String message){
        return new Result<>(20000,message,data);
    }

    public static <T> Result<T> success(String message){
        return new Result<>(20000,message,null);
    }

    //失败
    public static <T> Result<T> fail(){
        return new Result<>(20001,"fail",null);
    }

    public static <T> Result<T> fail(Integer code){
        return new Result<>(code,"fail",null);
    }

    public static <T> Result<T> fail(Integer code,String message){
        return new Result<>(code,message,null);
    }

    public static <T> Result<T> fail(String message){
        return new Result<>(20001,message,null);
    }

}
