package com.example.kenvin.t;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kenvin on 2017/11/13.
 */

public class FastJson {

    public static String  toJson(Object object)
    {
        //JSON载体
        StringBuffer jsonBuffer=new StringBuffer();
        //判断是否是集合类型
        if(object instanceof List<?>){
            jsonBuffer.append("[");
            List<?> list= (List<?>) object;
            //循环取集合类型
            for (int i=0;i<list.size();i++) {
                addObjectToJson(jsonBuffer, list.get(i));
                //jsonArarry添加  逗号分隔
                if (i < list.size() - 1)
                {
                    jsonBuffer.append(",");
                }
            }
        }else {
            addObjectToJson(jsonBuffer, object);
        }
        return jsonBuffer.toString();
    }

    /**
     * 解析单独的JSONObject类型
     * 递归准备
     * @param jsonBuffer
     * @param o
     */
    private static void addObjectToJson(StringBuffer jsonBuffer, Object o) {
        jsonBuffer.append("{");

        List<Field> fields=new ArrayList<>();
        getAllFields(o.getClass(),fields);
        for(int i=0;i<fields.size();i++)
        {
            //代表getMethod方法
            Method method=null;
            Field field=fields.get(i);
            //代表成员变量的值  张三
            Object  fieldValue=null;

            String fieldName=field.getName();
            //fieldName  成员名称 name   a 97 A 65   getName
            String methodName="get"+((char)(fieldName.charAt(0)-32)+fieldName.substring(1));

            try {
                //拿到Method对象
                method=o.getClass().getMethod(methodName);
            } catch (NoSuchMethodException e) {
                if(!fieldName.startsWith("is"))
                {
                    methodName="is"+((char)(fieldName.charAt(0)-32)+fieldName.substring(1));
                    //拿到Method对象
                }
                try {
                    method=o.getClass().getMethod(methodName);
                } catch (NoSuchMethodException e1) {
                    replaceChar(i,fields,jsonBuffer);
                    continue;
                }
            }
            //拿到了成员变量对应的get方法
            if(method!=null)
            {
                try {
                    //Integer 对象类型
                    fieldValue =method.invoke(o);
                } catch (Exception e) {
                    replaceChar(i,fields,jsonBuffer);
                    continue;
                }
            }
            if(fieldValue!=null)
            {
                jsonBuffer.append("\"");
                jsonBuffer.append(fieldName);
                jsonBuffer.append("\":");
                if(fieldValue instanceof Integer
                        || fieldValue  instanceof  Double||
                        fieldValue instanceof Long||
                        fieldValue instanceof Boolean ||
                        fieldValue instanceof Float)
                {
                    jsonBuffer.append(fieldValue.toString());
                }else if(fieldValue instanceof String)
                {
                    jsonBuffer.append("\"");
                    jsonBuffer.append(fieldValue.toString());
                    jsonBuffer.append("\"");
                }else if(fieldValue instanceof  List<?>)
                {
                    addListToBuffer(jsonBuffer,fieldValue);
                }else
                {
                    addObjectToJson(jsonBuffer,fieldValue);
                }
                jsonBuffer.append(",");
            }
            /**
             * 1 要
             * 2
             * 替换最后的逗号
             */
            if(i==fields.size()-1&&jsonBuffer.charAt(jsonBuffer.length()-1)==',')
            {
                //删除最后一个逗号
                jsonBuffer.deleteCharAt(jsonBuffer.length()-1);
            }
        }
        jsonBuffer.append("}");
    }
    public static  void replaceChar(int i,List<Field> fields,StringBuffer jsonBuffer)
    {
        if(i==fields.size()-1&&jsonBuffer.charAt(jsonBuffer.length()-1)==',')
        {
            //删除最后一个逗号
            jsonBuffer.deleteCharAt(jsonBuffer.length()-1);
        }
    }

    /**
     * 解析集合类型数据
     * @param jsonBuffer
     * @param fieldValue
     * neng  1
     * 不能 2
     */
    private static void addListToBuffer(StringBuffer jsonBuffer, Object fieldValue) {
        //前面已经判断了fieldValue的类型
        List<?> list= (List<?>) fieldValue;
        jsonBuffer.append("[");
        for (int i=0;i<list.size();i++)
        {
            //遍历集合中的每一个元素
            addObjectToJson(jsonBuffer,list.get(i));
            if(i<list.size()-1)
            {
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("]");
    }

    /**
     * 获取当前Class  所有的成员变量 Field
     * 父类的Class  成员变量
     *  Object 类型
     *  final 修饰的成员变量
     *  会 1
     *  不会 2
     *   递归方法
     * @param aClass
     * @param fields
     */
    private static void getAllFields(Class<?> aClass, List<Field> fields) {
        if(fields==null)
        {
            fields=new ArrayList<>();
        }
        //排除Object类型
        if(aClass.getSuperclass()!=null)
        {
            //拿到当前Class的所有成员变量的Field
            Field[] fieldsSelf=aClass.getDeclaredFields();
            for(Field field:fieldsSelf)
            {
                //排除final修饰的成员变量
                if(!Modifier.isFinal(field.getModifiers()))
                {
                    fields.add(field);
                }
            }
            getAllFields(aClass.getSuperclass(),fields);
        }
    }
}

