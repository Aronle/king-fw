package com.mars.fw.security.authentication.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mars.fw.common.utils.SpringUtil;
import com.mars.fw.security.entity.UserEntity;
import com.mars.fw.security.mapper.KingUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * * @description: Security 认证服务类
 * 虚拟账户登录
 * * @author: aron
 */
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private KingUserMapper kingUserMapper;

    @Override
    public UserDetails loadUserByUsernameAndCompanyTypeAndPlatform(String userName) throws AuthenticationException {


        if (StringUtils.isEmpty(userName)) {
            throw new UsernameNotFoundException("请输入用户名");
        }

        UserEntity userEntity = kingUserMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_name", userName));
        if (null == userEntity) {
            throw new UsernameNotFoundException("账户不存在");
        }

        //构建数据库用户信息
        CustomUserDetails userAccountDetails = new CustomUserDetails(userEntity);
        return userAccountDetails;
    }


}
