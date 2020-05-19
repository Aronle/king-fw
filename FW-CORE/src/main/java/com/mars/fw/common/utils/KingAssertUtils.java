package com.mars.fw.common.utils;

import com.mars.fw.web.exception.KingException;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @Author King
 * @create 2020/4/29 14:03
 */
public class KingAssertUtils {


    public static void assertNull(Object obj) {
        if (null == obj) {
            throw new KingException("数据不能为空");
        }
    }

    public static void assertNull(Object obj, String msg) {
        if (null == obj) {
            throw new KingException(msg);
        }
    }

    public static void assertBlank(String s) {
        if (StringUtils.isBlank(s)) {
            throw new KingException("数据不能为空");
        }
    }


    public static void assertBlank(String s, String msg) {
        if (StringUtils.isBlank(s)) {
            throw new KingException(msg);
        }
    }

    public static void assertEmpty(String s) {
        if (StringUtils.isBlank(s)) {
            throw new KingException("数据集合不能为空");
        }
    }


    public static void assertEmpty(Collection<?> s, String msg) {
        if (CollectionUtils.isEmpty(s)) {
            throw new KingException(msg);
        }
    }


}
