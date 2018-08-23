package com.lxw.fastjson.serializer;

import com.lxw.fastjson.JsonConfig;
import com.lxw.fastjson.Utils;

import java.lang.reflect.Field;
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
public class JavaBeanSerializer implements ObjectSerializer {
    private final List<FieldSerializer> mFieldSerializers;

    //获取所有需要 序列化的字段 包括 get is开头的  以及 public修饰的字段
    public JavaBeanSerializer(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Utils.parseAllFieldToCache(fieldMap, clazz);
        mFieldSerializers = Utils.computeGetters(fieldMap, clazz);
    }

    @Override
    public void serializer(JsonConfig config, StringBuilder out, Object object) {
        out.append("{");
        int i = 0;

        for (FieldSerializer fieldSerializer : mFieldSerializers) {
            if (i != 0) {
                out.append(",");
            }
            String serializer = fieldSerializer.serializer(config, object);
            out.append(serializer);
            if (serializer.isEmpty()) {
                i = 0;
            } else {
                i = 1;
            }
        }
        out.append("}");
    }
}
