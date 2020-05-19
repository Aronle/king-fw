package com.mars.fw.common.utils;

import java.util.List;

/**
 * 字符串静态类
 *
 * @Author King
 * @create 2020/4/22 16：00
 */
public class StringUtils {
    /**
     * @param source
     * @return 若字符串为null或仅包含空格，返回true；否则返回false。
     */
    public static boolean isBlank(String source) {
        return null == source || source.trim().length() == 0;
    }


    public static boolean isNotBlank(String source) {
        return !isBlank(source);
    }


    public static String concat(String[] values) {
        if (values == null)
            return null;

        String result = "";
        for (String value : values) {
            result = result + value;
        }
        return result;
    }


    public static String formatList(List<?> list) {
        if (list == null) {
            return null;
        } else if (list.size() == 0) {
            return "[]";
        }

        StringBuffer result = null;
        for (Object each : list) {
            if (result == null) {
                result = new StringBuffer().append("[ ").append(each);
            } else {
                result.append("\n    ").append(each);
            }
        }
        result.append(" ]");
        return result.toString();
    }

    public static String formatArray(String[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return "[]";
        }

        StringBuffer result = null;
        for (Object each : array) {
            if (result == null) {
                result = new StringBuffer("[ ").append(each);
            } else {
                result.append(", ").append(each);
            }
        }
        result.append(" ]");
        return result.toString();
    }


    private StringUtils() {
    }
}