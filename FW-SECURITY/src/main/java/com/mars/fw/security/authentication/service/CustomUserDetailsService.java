package com.mars.fw.security.authentication.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserDetailsService {

    UserDetails loadUserByUsernameAndCompanyTypeAndPlatform(String username) throws AuthenticationException;
}
