package com.cytosine.spring.util;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseUtil<T> {
    //执行查询

    /**
     * 执行查询
     * 自动从数据库连接池获取一个连接执行查询，只需传入需要转换的数据类型、SQL语句、SQL参数列表
     * @param clazz  需要转换的对象类型
     * @param sql    需要执行的SQL语句
     * @param o      参数列表
     * @param <T>    泛型
     * @return
     */
    public static <T> List<T> executeQuery(Class<T> clazz, String sql, Object ...o){
        //最终返回的结果列表
        List<T> tList = new ArrayList<>();
        //构造方法参数列表
        try {
            //----------------->从数据库连接池获取一个连接，并初始化基本数据
            Connection connection = ConnectionPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (o != null){            //判断o是否为空
                for (int i = 1; i <= o.length; i++) {
                    preparedStatement.setObject(i,o[i-1]);
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery(); //执行sql语句，获取结果集
            //----------------->获取数据库数据信息
            ResultSetMetaData metaData = resultSet.getMetaData();  //获取核心数据，本次执行从数据库获取的所有数据信息
            int columnCount = metaData.getColumnCount();  //获取列数
            List<String> columnLabels = new ArrayList<>(columnCount);  //每一列的字段名
            List<String> columnClassNames = new ArrayList<>(columnCount);  //每一列的数据类型
            for (int i = 1; i <= columnCount; i++) {
                columnLabels.add(metaData.getColumnLabel(i));   //resultSet相关的下标从1开始
                columnClassNames.add(metaData.getColumnClassName(i));
            }
            //----------------->通过反射获取对象实例信息
            Field[] declaredFields = clazz.getDeclaredFields();   //所有成员变量信息
            //所有字段名
            List<String> paramNames = new ArrayList<>();
            for (Field declaredField : declaredFields) {
                String name = declaredField.getName();
                paramNames.add(name);
            }
            //----------------->遍历结果集
            while (resultSet.next()){   //返回一个布尔值并且将指针向下移动一位
                //获取一个实例
                T t = getInstance(clazz);
                //遍历数据库字段
                for (int i = 0; i < columnLabels.size(); i++) {
                    String name = castToCamel(columnLabels.get(i));
                    boolean contains = paramNames.contains(name);
                    //如果参数列表中包括该字段，则赋值
                    if (contains){
                        assignmentT(t,clazz,columnLabels,resultSet,i);
                    }else {
                        continue;
                    }
                }
                tList.add(t);
            }
            //归还连接
            ConnectionPool.put(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tList;
    }

    /**
     * 执行删除、添加、更改
     * @param sql    需要执行的SQL语句
     * @param o      参数列表
     */
    public static <T> int executeUpdate(Class<T> tClass,String sql, Object ...o){
        try {
            Connection connection = ConnectionUtil.getCurrentThreadConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (o != null){            //判断o是否为空
                for (int i = 1; i <= o.length; i++) {
                    preparedStatement.setObject(i,o[i-1]);
                }
            }
            int count = preparedStatement.executeUpdate();
            System.out.println(count);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Constructor<T>[] declaredConstructors = (Constructor<T>[])clazz.getDeclaredConstructors();
        Constructor<T> constructor = declaredConstructors[0];
        //获取参数最少的构造方法
        for (int i = 0; i < declaredConstructors.length; i++) {
            if (declaredConstructors[i].getParameterCount()<constructor.getParameterCount()){
                constructor = declaredConstructors[i];
            }
        }
        constructor.setAccessible(true);   //暴力破解
        int parameterCount = constructor.getParameterCount();  //构造方法的参数格式
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        //记录构造方法的参数
        Object[] objs = new Object[parameterCount];
        if(parameterCount > 0){
            for (int j = 0; j < parameterCount; j++) {
                //匹配参数类型
                String type = parameterTypes[j].getName();
                //获取初始值
                getInitValue(type,j,objs);
            }
            return constructor.newInstance(objs);
        }else {
            //无参构造直接创建
            return constructor.newInstance();
        }
    }
    /**
     * 将数据库中_分割的形式转换为驼峰
     * @param originalName
     * @return
     */
    private static String castToCamel(String originalName){
        String camelName = "";
        //以下划线分割后的数组
        String[] emName = originalName.split("_");

        if(originalName == null ||  "".equals(originalName)){
            return null;
        }
        //如果只有一个单词，直接返回名字
        if (emName.length == 1){
            return emName[0];
        }else{
            camelName = emName[0];
        }
        //第一个单词首字母不大写
        for (int i = 1; i < emName.length; i++) {
            char[] temp = emName[i].toCharArray();
            if(temp[0] >= 97 && temp[0] <=122){
                temp[0] -= 32;
            }
            //将转换后的单词重新装回数组
            emName[i] = new String(temp);
            //组装成新的名字
            camelName = camelName + emName[i];
        }

        return camelName;
    }
    //赋值
    private static <T> void assignmentT(T t, Class<?> clazz, List<String> columnLabels, ResultSet resultSet,int currentIndex) throws NoSuchFieldException, SQLException, IllegalAccessException {
        //获取Java成员变量的信息
        Field field = clazz.getDeclaredField(castToCamel(columnLabels.get(currentIndex)));
        field.setAccessible(true); //暴力破解
        String type = field.getType().getName(); //成员变量数据类型
        Object o1 = resultSet.getObject(columnLabels.get(currentIndex));

        switch (type){
            case "byte":
                byte byteV = Byte.class.cast(o1);
                field.set(t,byteV);
                break;
            case "short":
                short shortV = Short.class.cast(o1);
                field.set(t,shortV);
                break;
            case "int":
                int intValue = Integer.class.cast(o1);
                field.set(t,intValue);
                break;
            case "long":
                long longV = Long.class.cast(o1);
                field.set(t,longV);
                break;
            case "float":
                float floatV = Float.class.cast(o1);
                field.set(t,floatV);
                break;
            case "double":
                double doubleV = Double.class.cast(o1);
                field.set(t,doubleV);
                break;
            case "char":
                char charV = Character.class.cast(o1);
                field.set(t,charV);
                break;
            case "java.lang.String":
                String stringV = String.class.cast(o1);
                field.set(t,stringV);
                break;
            case "java.util.Date":
                Date dateV = Date.class.cast(o1);
                field.set(t,dateV);
                break;
            case "boolean":
                boolean booleanV = Boolean.class.cast(o1);
                field.set(t,booleanV);
                break;
            case "java.math.BigDecimal":
                BigDecimal bigDecimalV = BigDecimal.class.cast(o1);
                field.set(t,bigDecimalV);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
    //获取构方法参数的初始值
    private static void getInitValue(String type,int j,Object[] objs){
        switch (type){
            case "int":
                objs[j] = 0;
                break;
            case "byte":
                objs[j] = 0;
                break;
            case "short":
                objs[j] = 0;
                break;
            case "long":
                objs[j] = 0L;
                break;
            case "float":
                objs[j] = 0.0f;
                break;
            case "double":
                objs[j] = 0.0;
                break;
            case "char":
                objs[j] = null;
                break;
            case "boolean":
                objs[j] = false;
                break;
            default:
                objs[j] = null;
                break;
        }
    }
}
