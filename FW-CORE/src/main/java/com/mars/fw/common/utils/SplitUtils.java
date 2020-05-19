package com.mars.fw.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * 字符串按指定字符转集合
 *
 * @Author King
 * @create 2020/4/22 16：00
 */
public class SplitUtils {

    public static Set<Integer> stringToIntSet(String ids, String separator) {
        return Sets.newLinkedHashSet(stringToIntList(ids, separator));
    }

    public static Set<Long> stringToIdSet(String ids, String separator) {
        return Sets.newLinkedHashSet(stringToIdList(ids, separator));
    }

    public static Set<String> stringToSet(String str, String separator) {
        return Sets.newLinkedHashSet(stringToList(str, separator));
    }

    public static List<Integer> stringToIntList(String ids, String separator) {
        List<Integer> result = Lists.newArrayList();
        if (StringUtils.isBlank(ids)) {
            return result;
        }
        for (String idStr : ids.split(separator)) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            result.add(Integer.valueOf(idStr));
        }
        return result;
    }

    public static List<Long> stringToIdList(String ids, String separator) {
        List<Long> result = Lists.newArrayList();
        if (StringUtils.isBlank(ids)) {
            return result;
        }
        for (String idStr : ids.split(separator)) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            result.add(Long.parseLong(idStr));
        }
        return result;
    }


    public static List<String> stringToList(String str, String separator) {
        List<String> result = Lists.newArrayList();
        if (StringUtils.isBlank(str)) {
            return result;
        }
        for (String s : str.split(separator)) {
            if (StringUtils.isBlank(s)) {
                continue;
            }
            result.add(s.trim());
        }
        return result;
    }
}
