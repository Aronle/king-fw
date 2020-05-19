package com.mars.fw.security.authorization.filter;

import com.mars.fw.cache.CacheService;
import com.mars.fw.common.utils.JsonMapper;
import com.mars.fw.security.tool.model.AuthResponseModel;
import com.mars.fw.web.context.GlobalEntry;
import com.mars.fw.web.context.KingContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author King
 * @create 2020/5/8 9:49
 */
@Service
public class AuthContextProviderImpl implements KingContextProvider {

    @Autowired
    private CacheService cacheService;

    @Override
    public GlobalEntry getGlobalEntry() {
        return new GlobalEntry();
    }

    @Override
    public GlobalEntry getGlobalEntry(String token) throws Exception {
        AuthResponseModel detail = JsonMapper.getDefault().fromJson((String) cacheService.get(token),
                AuthResponseModel.class);
        GlobalEntry entry = new GlobalEntry();
        entry.setToken(token);
        entry.setUserId(detail.getUserId());
        entry.setObject(detail);
        return entry;
    }
}
