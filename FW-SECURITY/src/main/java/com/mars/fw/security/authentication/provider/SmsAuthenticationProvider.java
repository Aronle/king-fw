package com.mars.fw.security.authentication.provider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.mars.fw.cache.CacheService;
import com.mars.fw.common.utils.SpringUtil;
import com.mars.fw.common.utils.StringUtils;
import com.mars.fw.security.authentication.exception.AuthenticationSmsException;
import com.mars.fw.security.authentication.service.CustomUserDetails;
import com.mars.fw.security.entity.UserEntity;
import com.mars.fw.security.mapper.KingUserMapper;
import com.mars.fw.security.tool.model.CustomAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @description: 自定义短信验证码登录的provider
 * @author: aron
 * @date: 2019-04-20 09:46
 */
public class SmsAuthenticationProvider extends AbstractAuthenticationProvider {

    public static String SMS_CODE = "sms_code";

    @Autowired
    private KingUserMapper kingUserMapper;

    @Override
    protected Authentication authenticateCustom(CustomAuthenticationToken customAuthenticationToken) {
        try {
            String phone = (String) customAuthenticationToken.getPrincipal();
            String smsCode = (String) customAuthenticationToken.getCredentials();
            UserEntity userEntity = kingUserMapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getPhone, phone));
            if (userEntity == null) {
                throw new UsernameNotFoundException("手机用户不存在或该手机未绑定用户!");
            }
            //从缓存中获取手机号的短信验证码
            CacheService cacheService = SpringUtil.getBean(CacheService.class);
            Object object = cacheService.get(SMS_CODE + "_" + phone);
            if (null == object) {
                throw new AuthenticationSmsException("短信验证码已过期");
            }
            if (!smsCode.equals(object.toString())) {
                throw new AuthenticationSmsException("短信验证码错误");
            }
            //密码校验通过后 构建数据库用户信息
            CustomUserDetails userAccountDetails = new CustomUserDetails(userEntity);
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userAccountDetails, customAuthenticationToken.getCredentials(), null);
            return result;
        } catch (Exception e) {
            throw new AuthenticationSmsException("手机短信验证登录失败");
        }
    }
}
