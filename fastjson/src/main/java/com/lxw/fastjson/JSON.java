package com.lxw.fastjson;

import com.lxw.fastjson.parse.ObjectDeserializer;
import com.lxw.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/22
 *     desc   :
 * </pre>
 */
public class JSON {

    public static String toJSONString(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException(" obj == null");
        }
        JsonConfig globalInstance = JsonConfig.getGlobalInstance();
        ObjectSerializer serializer = globalInstance.getSerializer(obj.getClass());
        StringBuilder sb = new StringBuilder();
        serializer.serializer(globalInstance, sb, obj);
        return sb.toString();
    }

    public static <T> T parse(String jsonStr, Class<T> clazz) {
        return parse(jsonStr, (Type) clazz);

    }

    public static <T> T parse(String jsonStr, Type type) {
        JsonConfig globalInstance = JsonConfig.getGlobalInstance();
        ObjectDeserializer deserializer = globalInstance.getDeserializer(type);
        try {
            return deserializer.deserializer(globalInstance, jsonStr, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
