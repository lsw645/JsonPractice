package com.lxw.fastjson;

import com.lxw.fastjson.serializer.FieldSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
public class Utils {

    public static boolean isBox(Class type) {
        return type == Integer.class ||
                type == Character.class ||
                type == Boolean.class ||
                type == Double.class ||
                type == Float.class ||
                type == Short.class;
    }

    public static boolean isString(Class type) {
        return CharSequence.class.isAssignableFrom(type);
    }

    public static void parseAllFieldToCache(Map<String, Field> cacheMap, Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String name = field.getName();
            if (!cacheMap.containsKey(name)) {
                cacheMap.put(name, field);
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            parseAllFieldToCache(cacheMap, clazz.getSuperclass());
        }
    }

    public static List<FieldSerializer> computeGetters(Map<String, Field> cacheMap, Class clazz) {
        Map<String, FieldInfo> map = new LinkedHashMap<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            //static修饰的不是我们需要的
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            //返回值为空的
            if (method.getReturnType().equals(Void.TYPE)) {
                continue;
            }
            //有参数的不要
            if (method.getParameterTypes().length != 0) {
                continue;
            }

            String methodName = method.getName();

            if (methodName.equals("getClass")) {
                continue;
            }
            String propertyName;
            if (methodName.startsWith("get")) {
                if (methodName.length() < 4) {
                    continue;
                }

                char c3 = methodName.charAt(3);
                propertyName = Character.toLowerCase(c3) + methodName.substring(4);
                Field field = cacheMap.get(propertyName);
                FieldInfo fieldInfo = new FieldInfo(propertyName, field, method);
                map.put(propertyName, fieldInfo);
            }


            if (methodName.startsWith("is")) {
                if (methodName.length() < 3) {
                    continue;
                }
                //不是boolean或者Boolean
                if (method.getReturnType() != Boolean.TYPE
                        && method.getReturnType() != Boolean.class) {
                    continue;
                }

                char c2 = methodName.charAt(2);
                propertyName = Character.toLowerCase(c2) + methodName.substring(3);
                //可能已经在get找到了
                if (map.containsKey(propertyName)) {
                    continue;
                }
                Field field = cacheMap.get(propertyName);
                FieldInfo fieldInfo = new FieldInfo(propertyName, field, method);
                map.put(propertyName, fieldInfo);
            }
            //获取父类及自身的pulic 字段
            Field[] fields = clazz.getFields();
            for (Field field : fields) {

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                String name = field.getName();
                if (!map.containsKey(name)) {
                    FieldInfo fieldInfo = new FieldInfo(name, field, null);
                    map.put(name, fieldInfo);
                }
            }
        }

        List<FieldSerializer> fieldSerializers = new ArrayList<>();
        for (FieldInfo fieldInfo : map.values()) {
            fieldSerializers.add(new FieldSerializer(fieldInfo));
        }
        return fieldSerializers;
    }

    /**
     * 首先查找setter，再查找公有field
     *
     * @param cachedMap
     * @param clazz
     */
    public static List<FieldInfo> computeSetters(Map<String, Field> cachedMap, Class<?> clazz) {
        Map<String, FieldInfo> map = new LinkedHashMap<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (method.getReturnType() != Void.TYPE) {
                continue;
            }

            if (method.getParameterTypes().length != 1) {
                continue;
            }

            String methodName = method.getName();
            if (methodName.length() < 4) {
                continue;
            }
            if (methodName.startsWith("set")) {
                char c = methodName.charAt(3);
                String propertyName = Character.toLowerCase(c) + methodName.substring(4);
                Field field = cachedMap.get(propertyName);
                FieldInfo fieldInfo = new FieldInfo(propertyName, field, method, true);
                map.put(propertyName, fieldInfo);
            }
        }
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            String name = field.getName();
            if (!map.containsKey(name)) {
                FieldInfo fieldInfo = new FieldInfo(name, field, null, true);
                map.put(name, fieldInfo);
            }

        }

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (FieldInfo fieldInfo : map.values()) {
            fieldInfoList.add(fieldInfo);
        }
        return fieldInfoList;

    }


    public static Type getItemType(Type fieldType) {
        if (fieldType instanceof Class) {
            return fieldType;
        }

        if (fieldType instanceof ParameterizedType) {
            Type type = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
            //通配符 <? extends String>
            if (type instanceof WildcardType) {
                //<? extends String>  获取 String
                Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return upperBounds[0];
                }
                // <? super String> 获取String 下线
//                ((WildcardType) type).getLowerBounds();
            }
            return type;

        }
        return Object.class;
    }
}
