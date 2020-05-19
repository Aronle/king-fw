package com.mars.fw.web.context;

import com.mars.fw.web.exception.KingException;
import org.apache.commons.lang.StringUtils;

/**
 * 封装会话上下文 利用的是ThreadLocal的特性
 * 这个是使用来管理接口生命周期内的全局变量的 最多的应用场景是在 用户信息的存储
 * <p>
 * 这边的封装想尽量简化调用者的复杂度
 *
 * @Author King
 * @create 2020/4/21 17:27
 */
public class KingContext {

    private static ThreadLocal<GlobalEntry> CONTEXT = new ThreadLocal<GlobalEntry>();

    public static void setContext(GlobalEntry context) {
        CONTEXT.set(context);
    }

    public static void removeContext() {
        CONTEXT.remove();
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static Long getUserId() {
        GlobalEntry entry = CONTEXT.get();
        Long userId = null == entry ? null : entry.getUserId();
        if (null == userId) {
            throw new KingException("请先登录");
        }
        return userId;
    }

    /**
     * 获取用户Code
     *
     * @return
     */
    public static String getUserCode() {
        GlobalEntry entry = CONTEXT.get();
        String userCode = null == entry ? null : entry.getUserCode();
        if (StringUtils.isBlank(userCode)) {
            throw new KingException("请先登录");
        }
        return userCode;
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        GlobalEntry entry = CONTEXT.get();
        String token = null == entry ? null : entry.getToken();
        if (org.springframework.util.StringUtils.isEmpty(token)) {
            throw new KingException("请先登录");
        }
        return token;
    }

    /**
     * 获取全局存储的信息
     *
     * @return
     */
    public static Object getObject() {
        GlobalEntry entry = CONTEXT.get();
        Object object = null == entry ? null : entry.getObject();
        if (org.springframework.util.StringUtils.isEmpty(object)) {
            throw new KingException("会话上下文没有设置");
        }
        return object;
    }
}
