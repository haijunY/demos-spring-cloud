package com.demos.basic.designpattern.proxy.dynamicproxy.gpproxy;


import com.demos.basic.designpattern.proxy.Person;
import com.demos.basic.designpattern.proxy.staticproxy.Son;

/**
 * Created by yhj on @date: 2019/05/08
 */
public class GPProxyTest {
    public static void main(String[] args) {
        try {
            Person obj = (Person) new GPMeipo().getInstance(new Son());
//            obj.findLove();
            obj.findHose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
