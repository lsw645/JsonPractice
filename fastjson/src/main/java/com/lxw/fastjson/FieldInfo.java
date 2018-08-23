package com.lxw.fastjson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/22
 *     desc   :
 * </pre>
 */
public class FieldInfo {
    public String name;
    public Field field;
    public Method method;
    public Class type;
    //set 参数的类型
    public Type genericType;

    public FieldInfo(String name, Field field, Method method) {
        this.name = name;
        this.field = field;
        this.method = method;
        this.type = this.method != null ?
                this.method.getReturnType() : field.getType();
    }

    public FieldInfo(String name, Field field, Method method, boolean isSetter) {
        this.name = name;
        this.field = field;
        this.method = method;
        this.type = this.method != null ?
                this.method.getReturnType() : field.getType();
        if (isSetter) {
            genericType = method != null ?
                    method.getGenericParameterTypes()[0]
                    : field.getGenericType();
        }
    }


    public Object get(Object object) {
        try {
            return method != null ?
                    method.invoke(object) : field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void set(Object object, Object value) {
        try {
            if (method != null) {
                method.invoke(object, value);
            } else {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
