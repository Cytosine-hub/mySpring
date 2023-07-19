package com.cytosine.spring.mvc.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE}) //设置注解的作用点 表示该注解作用在方法上
@Retention(RetentionPolicy.RUNTIME)  //设置注解的生效时时间 SOURCE-源文件 RUNTIME-运行时 CLASS-在class文件中生效
@Documented
@Inherited
public @interface Transaction {
    /**
     * 传入方法所在的类名
     * @return
     */
    
    public Class className() default Object.class;
}
