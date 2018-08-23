package com.lxw.jsonpractice;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lance
 * @date 2018/5/10
 */
@JSONType(typeKey = "Parent")
class Parent {
    String name;

    public Parent() {
    }

    public Parent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
/**
 * FastJson在进行操作时，优先根据getter和setter的方法进行的，之后再依据PUBLIC Field进行。
 * 1、@JSONType
 * 定义类的序列化
 * 指定忽略某个字段: {@JSONType(ignores = {"c_name"})}
 * <p>
 * 2、@JSONField
 * 定义属性序列化Key
 * 作用在Fileld上
 * 针对 json数据的 Key与JavaBean 中的属性不能匹配,可以使用@JSONField进行解释
 * 作用在setter和getter方法上
 * getter表示 序列化后的Key; setter表示 反序列化对应的Key
 * 作用在函数参数上
 * 配合 @JSONCreator 使用
 * <p>
 * 3、@JSONCreator
 * Fastjson在反序列化时必须提供无参构造函数
 * 如果定义的构造函数都需要参数则需要使用 @JSONCreator 标明提供此构造函数用于反序列化
 * 同时由于构造函数中参数没有办法与类属性相匹配,所以可以配合 @JSONField 同时使用
 */

/**
 *
 * 不序列化 c_name
 */
@JSONType(ignores = {"name"}, orders = {"list", "age", "childs", "name"}, typeName = "Child")
public class Child extends Parent {
    private int age;
    public List<String> list;

    public List<Child> childs;

    public Child() {

    }

    /**
     * 指定FastJson反序列化构造函数
     * @param name
     * @param age
     */
    @JSONCreator
    public Child(@JSONField(name = "name") String name, @JSONField(name = "age") int age) {
        super(name);
        this.age = age;
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
    }

    public int getTest() {
        return 1;
    }

    //非公有属性需要有
    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", list=" + list +
                ", childs=" + childs +
                '}';
    }
}


