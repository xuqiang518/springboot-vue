package com.qiang.springboot.exception;

/**
 * @author XuQiang
 * @creat 2022-06-28 21:20
 */

import lombok.Getter;

/**自定义异常*/

@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public ServiceException(String code, String msg){
        super(msg);
        this.code = code;
    }
}
