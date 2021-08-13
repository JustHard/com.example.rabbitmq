package com.example.rabbitmq.one;

import sun.awt.windows.ThemeReader;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/08/04 16:10
 */


public class Singleton {

    //饿汉式 单例
    private Singleton test = new Singleton();

    private Singleton() {
    }

    public Singleton getInstance() {
        return test;
    }


    //懒汉式 单例
    private Singleton test1;

//    private Test(){}

    public Singleton getInstance1() {
        if (test1 == null) {
            test1 = new Singleton();
        }
        return test1;
    }

    //双重锁 单例
    private volatile static Singleton singleton;

    //    private Test(){}


    public Singleton getInstance2() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    //静态内部类 单例
//    private Singleton(){}
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static final Singleton getInstance3() {
        return SingletonHolder.INSTANCE;
    }

    //枚举 单例
    public enum EasyInstance {
        INSTANCE;
    }
}
