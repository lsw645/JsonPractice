package com.lxw.fastjson.parse;

import com.lxw.fastjson.FieldInfo;
import com.lxw.fastjson.JsonConfig;
import com.lxw.fastjson.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/23
 *     desc   :
 * </pre>
 */
public class JavaBeanDeserializer implements ObjectDeserializer {

    private final List<FieldInfo> fieldInfoList;
    private final Class beanType;

    public JavaBeanDeserializer(Class<?> clazz) {
        beanType = clazz;
        Map<String, Field> cachedMap = new HashMap<>();
        Utils.parseAllFieldToCache(cachedMap, clazz);
        fieldInfoList = Utils.computeSetters(cachedMap, clazz);
    }

    @Override
    public <T> T deserializer(JsonConfig config, String jsonStr, Object object) throws Exception {
        JSONObject jsonObject;
        if (object == null) {
            jsonObject = new JSONObject(jsonStr);
        } else {
            jsonObject = (JSONObject) object;
        }

        T t = (T) beanType.newInstance();
        if (t != null) {
            for (FieldInfo fieldInfo : fieldInfoList) {
                Object value = jsonObject.opt(fieldInfo.name);
                if (value instanceof JSONObject || value instanceof JSONArray) {
                    ObjectDeserializer deserializer = config.getDeserializer(fieldInfo.genericType);
                    Object obj = deserializer.deserializer(config, null, value);
                    fieldInfo.set(t, obj);
                } else {
                    if (value != JSONObject.NULL) {
                        fieldInfo.set(t, value);
                    }
                }
            }
        }
        return t;
    }
}
