package com.lxw.fastjson;

import com.lxw.fastjson.parse.JavaBeanDeserializer;
import com.lxw.fastjson.parse.ListDeserializer;
import com.lxw.fastjson.parse.ObjectDeserializer;
import com.lxw.fastjson.serializer.JavaBeanSerializer;
import com.lxw.fastjson.serializer.ListSerializer;
import com.lxw.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/22
 *     desc   :
 * </pre>
 */
public class JsonConfig {
    private static final JsonConfig GLOBAL_INSTANCE = new JsonConfig();
    private Map<Class, ObjectSerializer> mSerializerMap = new HashMap<>();
    private Map<Type, ObjectDeserializer> mDeserializerMap = new HashMap<>();

    public static JsonConfig getGlobalInstance() {
        return GLOBAL_INSTANCE;
    }

    public ObjectSerializer getSerializer(Class clazz) {
        ObjectSerializer objectSerializer = mSerializerMap.get(clazz);
        if (objectSerializer != null) {
            return objectSerializer;
        }
        if (List.class.isAssignableFrom(clazz)) {
            objectSerializer = ListSerializer.INSTANCE;
        } else if (Map.class.isAssignableFrom(clazz)) {
            // TODO
        } else if (clazz.isArray()) {
            //如果是数组  todo
        } else {
            objectSerializer = new JavaBeanSerializer(clazz);
        }
        mSerializerMap.put(clazz, objectSerializer);
        return objectSerializer;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer objectDeserializer = mDeserializerMap.get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        if (type instanceof Class) {
            objectDeserializer = new JavaBeanDeserializer((Class<?>) type);
        } else if(type instanceof ParameterizedType) {
            objectDeserializer = new ListDeserializer((ParameterizedType) type);
        }
        mDeserializerMap.put(type, objectDeserializer);
        return objectDeserializer;

    }


}
