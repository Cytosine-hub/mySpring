package com.cytosine.spring.mvc.servlet;

import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cytosine.spring.mvc.annotation.*;
import com.cytosine.spring.mvc.entity.Handler;
import com.cytosine.spring.mvc.entity.Result;
import com.cytosine.spring.mvc.proxy.ProxyFactory;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 增强版servlet 可以实现依赖注入
 */
public class MyDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();
    private List<String> classNames = new ArrayList<>();
    private Map<String,Object> ioc = new HashMap<>();
    private List<Handler> handlerMapping = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("====================================mini Spring MVC=========================================");
        //获取在web.xml中配置的配置路径
        String demoConfig=  config.getInitParameter("demoConfig");
        //加载配置
        loadConfig(demoConfig);
        //扫包
        doScanner(properties.getProperty("scanPackage"));
        //实例化对象
        doInstance();
        //依赖注入
        inject();
        //事务动态代理
        dynamicProxy();
        //初始化处理器映射器
        initHandlerMapping();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "text/html;charset=utf-8");
        //获取对应的handler 一个handler对应一个接口
        Handler handler = getHandler(req);
        if (handler == null){
            resp.getWriter().write("404 NOT FOUND");
            return;
        }
        //参数绑定
        Method method = handler.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        //方法的参数列表
        Object[] paramValues = new Object[parameterTypes.length];
        Map<String, String[]> parameterMap = req.getParameterMap();

        //获取请求参数
        Map<String, Integer> paramIndexMapping = handler.getParamIndexMapping();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String join = StringUtils.join(entry.getValue(),",");
            if (!paramIndexMapping.containsKey(entry.getKey())){ continue;}
            Integer index = paramIndexMapping.get(entry.getKey());
            paramValues[index] = join;
        }
        Integer requestIndex = paramIndexMapping.get(HttpServletRequest.class.getSimpleName());
        if (requestIndex != null){paramValues[requestIndex] = req;}

        Integer responseIndex = paramIndexMapping.get(HttpServletResponse.class.getSimpleName());
        if (responseIndex != null){paramValues[responseIndex] = resp;}

        try {
            Result result = (Result)handler.getMethod().invoke(handler.getController(), paramValues);

            //解决Date转为时间戳的问题
            String o = JSONObject.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
            req.setAttribute("result",o);
            req.getRequestDispatcher("/page/result.jsp").forward(req,resp);
//            resp.getWriter().write(o);
//            resp.getWriter().flush();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //加载配置文件  配置文件的名字配置在web.xml中
    private void loadConfig(String config){

        InputStream resourceAsStream = MyDispatcherServlet.class.getClassLoader().getResourceAsStream(config);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描包 包的根路径配置在配置文件中
     * 记录类的全路径名 放在classNames
     */
    private void doScanner(String scanPackage){
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + scanPackage.replaceAll("\\.", "/");
//        /D:/Project/virtual_bank_java/exam/target/exam-1.0-SNAPSHOT/WEB-INF/classes/
//        net.i2finance.training.exam.demo
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()){
                doScanner(scanPackage + "." + file1.getName() );
            }else {
                if (file1.getName().endsWith(".class")) {
                    classNames.add(scanPackage + "." + file1.getName().replace(".class", ""));
                }
            }
        }
    }

    /**
     * 实例化
     * 根据类名 通过反射 实例化Service对象和Controller对象
     * 把实例化之后的对象放在IOC容器中
     */
    private void doInstance(){
        if (classNames.isEmpty()){
            return;
        }
        for (int i = 0; i < classNames.size(); i++) {
            try {
                Class<?> aClass = Class.forName(classNames.get(i));
                if (aClass.isAnnotationPresent(MyController.class)){
                    Object o = aClass.newInstance();
                    String simpleName = aClass.getSimpleName();
                    String beanName = castName(simpleName);
                    ioc.put(beanName,o);
                }else if (aClass.isAnnotationPresent(MyService.class)){
                    Object o = aClass.newInstance();
                    String beanName = aClass.getAnnotation(MyService.class).value();
                    if ("".equals(beanName.trim())){
                        String simpleName = aClass.getSimpleName();
                        beanName = castName(simpleName);
                    }
                    ioc.put(beanName, o);
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (int j = 0; j < interfaces.length; j++) {
                        Class<?> anInterface = interfaces[j];
                        String simpleName = anInterface.getSimpleName();
                        ioc.put(simpleName,o);
                    }
                }else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //依赖注入
    private void  inject(){
        if (ioc.isEmpty()){
            return;
        }
        // 遍历IOC容器中的对象
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Object value = entry.getValue();
            //拿到对象的属性
            Field[] declaredFields = value.getClass().getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                //有注解 需要注入
                if (declaredField.isAnnotationPresent(MyAutoWired.class)){
                    declaredField.setAccessible(true);
                    MyAutoWired annotation = declaredField.getAnnotation(MyAutoWired.class);
                    if ("".equals(annotation.value().trim())){
                        try {
                            //判断是否需要动态代理
                            if (!declaredField.getClass().isAnnotationPresent(Transaction.class)) {
                                declaredField.set(value, ioc.get(declaredField.getType().getSimpleName()));
                            } else {
                                Object proxy = ProxyFactory.getInstance().getProxy(ioc.get(declaredField.getType().getSimpleName()));
                                declaredField.set(value,proxy);
                            }

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            declaredField.set(value,ioc.get(annotation.value()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //动态代理增强
    private void dynamicProxy(){
        if (ioc.isEmpty()){return;}
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Object value = entry.getValue();
            if (!value.getClass().isAnnotationPresent(Transaction.class)){ continue;}
            Object proxy = ProxyFactory.getInstance().getProxy(value);
            ioc.put(entry.getKey(), proxy);
        }
    }

    //取名 将首字母大写的名字改为首字母小写
    private String castName(String simpleName){
        char[] chars = simpleName.toCharArray();
        if ( 'A' <= chars[0] && chars[0] <= 'Z'){
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    //初始化handlerMapping
    private void initHandlerMapping(){
        if (ioc.isEmpty()){
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            //判断是否有controller注解
            if (!aClass.isAnnotationPresent(MyController.class)){
                continue;
            }
            String classUrl = "";
            //类上的注解
            if (aClass.isAnnotationPresent(MyRequestMapping.class)){
                //类上的路径
                classUrl = aClass.getAnnotation(MyRequestMapping.class).value();
            }

            //方法上的注解
            Method[] methods = entry.getValue().getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                //方法上的路径
                String url = "";
                Method method = methods[i];
                method.setAccessible(true);
                if (!method.isAnnotationPresent(MyRequestMapping.class)){ continue; }

                url = classUrl + method.getAnnotation(MyRequestMapping.class).value();
                //handler
                Handler handler = new Handler(Pattern.compile(url),entry.getValue(),method);
                Map<String, Integer> paramIndexMapping = handler.getParamIndexMapping();
                Parameter[] parameters = method.getParameters();
                //记录形参
                for (int j = 0; j < parameters.length; j++) {
                   Parameter parameter = parameters[j];
                    if (parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class){
                        paramIndexMapping.put(parameter.getType().getSimpleName(),j);
                    }else {
                        paramIndexMapping.put(parameter.getName(),j);
                    }
                }
                handlerMapping.add(handler);

            }

        }
    }

    private Handler getHandler(HttpServletRequest request){
        if (handlerMapping.isEmpty()){ return null; }

        String requestURI = request.getRequestURI();
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getUrl().matcher(requestURI);
            if (!matcher.matches()){ continue; }
            return handler;
        }
        return null;
    }
}
