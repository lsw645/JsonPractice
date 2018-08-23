package com.lxw.fastjson.serializer;

import com.lxw.fastjson.FieldInfo;
import com.lxw.fastjson.JsonConfig;
import com.lxw.fastjson.Utils;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/22
 *     desc   :
 * </pre>
 */
public class FieldSerializer {
    private FieldInfo fieldInfo;
    private String key;
    private final boolean isPrimitive;

    public FieldSerializer(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        this.key = '"' + fieldInfo.name + "\":";
        this.isPrimitive = Utils.isBox(fieldInfo.type) || fieldInfo.type.isPrimitive();
    }

    public String serializer(JsonConfig config, Object object) {
        Object value = fieldInfo.get(object);

        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (isPrimitive) {
            sb.append(key).append(value);
        } else if (Utils.isString(fieldInfo.type)) {
            sb.append(key)
                    .append("\"")
                    .append(value)
                    .append("\"");
        } else {
            ObjectSerializer serializer = config.getSerializer(fieldInfo.type);
            sb.append(key);
            serializer.serializer(config, sb, value);
        }
        return sb.toString();
    }
}
