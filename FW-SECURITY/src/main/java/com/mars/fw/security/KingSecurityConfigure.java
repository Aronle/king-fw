package com.mars.fw.security;

import com.mars.fw.security.authentication.filter.CustomAuthenticationFilter;
import com.mars.fw.security.authentication.handler.AuthFailureHandler;
import com.mars.fw.security.authentication.handler.AuthSuccessHandler;
import com.mars.fw.security.authentication.provider.MobilePasswordAuthenticationProvider;
import com.mars.fw.security.authentication.provider.SmsAuthenticationProvider;
import com.mars.fw.security.authentication.provider.UserPasswordAuthenticationProvider;
import com.mars.fw.security.authentication.service.CustomUserDetailsService;
import com.mars.fw.security.authorization.filter.AuthenticationFilter;
import com.mars.fw.security.tool.SecurityConstant;
import com.mars.fw.security.tool.model.password.MD5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * @Author King
 * @create 2020/5/6 15:28
 */
@Configuration
@EnableWebSecurity
public class KingSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

    @Bean
    SmsAuthenticationProvider smsAuthenticationProvider() {
        return new SmsAuthenticationProvider();
    }

    @Bean
    MobilePasswordAuthenticationProvider emailAuthenticationProvider() {
        return new MobilePasswordAuthenticationProvider();
    }

    @Bean
    AuthenticationProvider customDaoAuthenticationProvider() {
        return new UserPasswordAuthenticationProvider(passwordEncoder(), userDetailsService);
    }

    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(new AuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new AuthFailureHandler());
        return filter;
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager providerManager = new ProviderManager(Arrays.asList(customDaoAuthenticationProvider(), emailAuthenticationProvider(), smsAuthenticationProvider()));
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    /**
     * 安全认证配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginProcessingUrl(SecurityConstant.LOGIN_ACTION)
                .and()
                .logout()
                .logoutUrl(SecurityConstant.LOGOUT_ACTION)
                // .logoutSuccessHandler(null)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()));
    }
}

