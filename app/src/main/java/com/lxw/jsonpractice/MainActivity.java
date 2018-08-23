package com.lxw.jsonpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.lxw.fastjson.JSON;
import com.lxw.fastjson.TypeReference;
import com.lxw.jsonpractice.test.JsonRootBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private long l;
    String bigJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            InputStream is = getAssets().open("test.json");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[2048];
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            is.close();
            bigJson = new String(bos.toByteArray());
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void testFastJson() {
        /**
         * 提升首次序列化/反序列化性能
         */
        SerializeConfig.getGlobalInstance().registerIfNotExists(Child.class);
        ParserConfig.getGlobalInstance().registerIfNotExists(Child.class);

        Child child = new Child("name", 100);
        String json = com.alibaba.fastjson.JSON.toJSONString(child);
        Log.e(TAG, json);
        Log.e(TAG, com.alibaba.fastjson.JSON.parseObject(json, Child.class).toString());


        String before = com.alibaba.fastjson.JSON.toJSONString(child, new BeforeFilter() {
            @Override
            public void writeBefore(Object object) {
                Child child1 = (Child) object;
                child1.name = "修改咯";
            }
        });
        Log.e(TAG, before);
    }

    public void testJson(View view) {
        Child child = new Child("testname", 100);
        String json = JSON.toJSONString(child);
        Log.e(TAG, "序列化对象:" + json);

        List<Object> b = new ArrayList<>();
        b.add("1");
        b.add("2");
        b.add(2);
        b.add(2);
        Log.e(TAG, "序列化集合1:" + JSON.toJSONString(b));

        List<Child> childs = new ArrayList<>();
        childs.add(child);
        childs.add(new Child("张三", 1));
        childs.add(new Child("李四", 2));
        String jsons = JSON.toJSONString(childs);
        Log.e(TAG, "序列化集合2:" + jsons);


        child = JSON.parse(json, Child.class);
        Log.e(TAG, "反序列化单个对象：" + child);


        Type type = new TypeReference<List<Child>>() {
        }.getType();
        childs = JSON.parse(jsons, type);
        Log.e(TAG, "反序列化集合：" + childs);


        /**
         * 来个复杂的
         */
        List<List<Child>> childLists = new ArrayList<>();
        List<Child> child1 = new ArrayList<>();
        child1.add(new Child("T1", 100));
        child1.add(new Child("T2", 200));
        childLists.add(child1);
        List<Child> child2 = new ArrayList<>();
        Child t3 = new Child("T3", 300);
        t3.childs = new ArrayList<>();
        t3.childs.add(new Child("T3_1", 3100));
        t3.childs.add(new Child("T3_2", 3200));
        child2.add(t3);
        child2.add(new Child("T4", 400));
        childLists.add(child2);
        String list = JSON.toJSONString(childLists);
        Log.e(TAG, "序列化复杂集合：" + list);

        Type type1 = new TypeReference<List<List<Child>>>() {
        }.getType();

        List<List<Child>> childLists2 = JSON.parse(list, type1);
        Log.e(TAG, "反序列化复杂集合:" + childLists2);
    }

    public void testFast(View view) {
        Child child = new Child("testname", 100);

        begin();
        JSON.toJSONString(child);
        end("序列化对象");

        begin();
        String json = com.alibaba.fastjson.JSON.toJSONString(child);
        end("FastJson序列化对象");


        List<Object> b = new ArrayList<>();
        b.add("1");
        b.add("2");
        b.add(2);
        b.add(2);
        begin();
        JSON.toJSONString(b);
        end("序列化集合");

        begin();
        com.alibaba.fastjson.JSON.toJSONString(b);
        end("FastJson序列化集合");


        List<Child> childs = new ArrayList<>();
        childs.add(child);
        childs.add(new Child("张三", 1));
        childs.add(new Child("李四", 2));
        begin();
        String jsons = JSON.toJSONString(childs);
        end("序列化集合2");

        begin();
        com.alibaba.fastjson.JSON.toJSONString(childs);
        end("FastJson序列化集合2");


        begin();
        JSON.parse(json, Child.class);
        end("反序列化单个对象");

        begin();
        com.alibaba.fastjson.JSON.parseObject(json, Child.class);
        end("FastJson反序列化单个对象");

//        List<Child> children = com.alibaba.fastjson.JSON.parseArray(json, Child.class);

        begin();
        Type type = new TypeReference<List<Child>>() {
        }.getType();
        JSON.parse(jsons, type);
        end("反序列化集合");


        begin();
        com.alibaba.fastjson.JSON.parseArray(jsons, Child.class);
        end("FastJson反序列化集合");


        List<List<Child>> childLists = new ArrayList<>();
        List<Child> child1 = new ArrayList<>();
        child1.add(new Child("T1", 100));
        child1.add(new Child("T2", 200));
        childLists.add(child1);
        List<Child> child2 = new ArrayList<>();
        Child t3 = new Child("T3", 300);
        t3.childs = new ArrayList<>();
        t3.childs.add(new Child("T3_1", 3100));
        t3.childs.add(new Child("T3_2", 3200));
        child2.add(t3);
        child2.add(new Child("T4", 400));
        childLists.add(child2);

        begin();
        String list = JSON.toJSONString(childLists);
        end("序列化复杂集合");

        begin();
        com.alibaba.fastjson.JSON.toJSONString(childLists);
        end("FastJson序列化复杂集合");

        begin();

        Type type1 = new TypeReference<List<List<Child>>>() {
        }.getType();
        JSON.parse(list, type1);
        end("反序列化复杂集合");

        begin();
        com.alibaba.fastjson.JSON.parseObject(list, new com.alibaba.fastjson
                .TypeReference<List<List<Child>>>() {
        }.getType());
        end("FastJson反序列化复杂集合");


        /**
         * 100K +
         */
        begin();
        JsonRootBean parse = JSON.parse(bigJson, JsonRootBean.class);
        end("反序列化大数据");

        begin();
        JsonRootBean parse1 = com.alibaba.fastjson.JSON.parseObject(bigJson, JsonRootBean.class);
        end("FastJson反序列化大数据");


        begin();
        String jsonString = JSON.toJSONString(parse);
        end("序列化大数据");

        begin();
        String fastJsonString = com.alibaba.fastjson.JSON.toJSONString(parse1);
        end("FastJson序列化大数据");


    }

    public void begin() {
        l = System.currentTimeMillis();
    }

    public void end(String tag) {
        Log.e(TAG, tag + "耗时：" + (System.currentTimeMillis() - l));
    }
}
