package com.lxw.fastjson.serializer;

import com.lxw.fastjson.JsonConfig;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/22
 *     desc   :
 * </pre>
 */
public interface ObjectSerializer {

    void serializer(JsonConfig config, StringBuilder out, Object object);
}
