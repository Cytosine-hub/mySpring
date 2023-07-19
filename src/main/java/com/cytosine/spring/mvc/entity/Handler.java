package com.cytosine.spring.mvc.entity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 记录url和对应的方法、方法所在的对象 以及形参列表
 */
public class Handler {

    private Pattern url;
    private Object controller;
    private Method method;
    private Map<String,Integer> paramIndexMapping;

    public Handler(Pattern url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
        this.paramIndexMapping = new HashMap<>();
    }


    public Pattern getUrl() {
        return url;
    }

    public void setUrl(Pattern url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }
}
