package com.mars.fw.security.authentication.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mars.fw.security.authentication.exception.AuthenticationMobileException;
import com.mars.fw.security.authentication.service.CustomUserDetails;
import com.mars.fw.security.entity.UserEntity;
import com.mars.fw.security.mapper.KingUserMapper;
import com.mars.fw.security.tool.model.CustomAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @description: 自定义手机密码登录登录的provider
 * @author: king
 * @date: 2020-04-20 09:46
 */
public class MobilePasswordAuthenticationProvider extends AbstractAuthenticationProvider {

    @Autowired
    private KingUserMapper kingUserMapper;

    @Override
    protected Authentication authenticateCustom(CustomAuthenticationToken authentication) {
        try {
            String userName = (String) authentication.getPrincipal();
            if (StringUtils.isBlank(userName)) {
                throw new UsernameNotFoundException("请输入用户名");
            }

            UserEntity userEntity = kingUserMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", userName));
            if (null == userEntity) {
                throw new UsernameNotFoundException("账户不存在");
            }

            CustomUserDetails userAccountDetails = new CustomUserDetails(userEntity);
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userAccountDetails, authentication.getCredentials(), null);
            return result;
        } catch (Exception e) {
            throw new AuthenticationMobileException("手机密码登录失败");
        }

    }
}
