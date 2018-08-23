package com.lxw.fastjson.parse;

import com.lxw.fastjson.JsonConfig;
import com.lxw.fastjson.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/23
 *     desc   :
 * </pre>
 */
public class ListDeserializer implements ObjectDeserializer {
    private ParameterizedType parameterizedType;

    public ListDeserializer(ParameterizedType parameterizedType) {
        this.parameterizedType = parameterizedType;
    }

    @Override
    public <T> T deserializer(JsonConfig config, String jsonStr, Object object) throws Exception {
        //只存在jsonArray
        JSONArray jsonArray;
        if (object == null) {
            jsonArray = new JSONArray(jsonStr);
        } else {
            jsonArray = (JSONArray) object;
        }

        List list = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject || value instanceof JSONArray) {
                Type itemType = Utils.getItemType(parameterizedType);
                ObjectDeserializer deserializer = config.getDeserializer(itemType);
                Object obj = deserializer.deserializer(config, null, value);
                list.add(obj);
            } else {
                if (value != JSONObject.NULL) {
                    list.add(value);
                }
            }
        }


        return (T) list;
    }
}
