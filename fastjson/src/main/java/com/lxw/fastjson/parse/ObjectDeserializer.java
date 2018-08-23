package com.lxw.fastjson.parse;

import com.lxw.fastjson.JsonConfig;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/23
 *     desc   :
 * </pre>
 */
public interface ObjectDeserializer {

    <T> T deserializer(JsonConfig config, String jsonStr, Object object) throws Exception;
}
