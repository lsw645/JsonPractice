package com.lxw.fastjson.serializer;

import com.lxw.fastjson.JsonConfig;
import com.lxw.fastjson.Utils;

import java.util.List;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/22
 *     desc   :
 * </pre>
 */
public class ListSerializer implements ObjectSerializer {
    public static final ListSerializer INSTANCE = new ListSerializer();

    @Override
    public void serializer(JsonConfig config,
                           StringBuilder out,
                           Object object) {

        List list = (List) object;
        if (list.isEmpty()) {
            out.append("[]");
            return;
        }
        out.append("[");
        int i = 0;
        for (Object item : list) {
            if (i != 0) {
                out.append(",");
            }
            i++;
            if (item == null) {
                out.append("null");
            } else if (Utils.isBox(item.getClass())) {
                out.append(item);
            } else if (Utils.isString(item.getClass())) {
                out.append("\"").append(item).append("\"");
            } else {
                ObjectSerializer serializer = config.getSerializer(item.getClass());
                serializer.serializer(config, out, item);
            }
        }
        out.append("]");
    }
}
