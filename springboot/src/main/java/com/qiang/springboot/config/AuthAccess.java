package com.qiang.springboot.config;

import java.lang.annotation.*;

/**
 * @author XuQiang
 * @creat 2022-07-03 8:19
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
}
