package com.mars.fw.web.context;

/**
 * 定义GlobalEntry 获取的接口 由业务侧做具体的实现
 *
 * 这边主要是想约束GlobalEntry获取的实现 因为GlobalEntry是比较敏感或者说能影响全局的数据
 *
 * 尽量避免应用中过多的实现
 *
 * @Author King
 * @create 2020/4/21 17:48
 */
public interface KingContextProvider {

    /**
     * 获取默认的GlobalEntry
     *
     * @return
     */
    GlobalEntry getGlobalEntry();

    /**
     * 通过GlobalEntry
     *
     * @param token
     * @return
     * @throws Exception
     */
    GlobalEntry getGlobalEntry(String token) throws Exception;

}
