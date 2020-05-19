package com.mars.fw.web.study;

/**
 * @Author King
 * @create 2020/4/29 11:14
 */
public class Test {

    static{
        System.loadLibrary("bridge");
    }

    public native int nativeAdd(int x,int y);

    public static void main(String[] args){
        Test obj = new Test();
        System.out.printf("%d\n",obj.nativeAdd(2012,3));
    }

}
