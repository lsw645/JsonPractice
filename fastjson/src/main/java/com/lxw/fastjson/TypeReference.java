package com.lxw.fastjson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/23
 *     desc   :
 * </pre>
 */
public class TypeReference<T> {

    private Type mType;

    protected TypeReference() {
        //获取泛型T的真正类型
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        mType = parameterizedType.getActualTypeArguments()[0];
    }

    public Type getType() {
        return mType;
    }
}
