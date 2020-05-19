package com.mars.fw.security.tool.model;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private UserTypeEnum userType;


    public CustomAuthenticationToken(AuthenticationParam input) {
        super(input.getUsername(), input.getPassword());
        this.userType = input.getUserType();
        super.setAuthenticated(false);
    }

    public CustomAuthenticationToken(AuthenticationParam input, Collection<? extends GrantedAuthority> authorities) {
        super(input.getUsername(), input.getPassword(), authorities);
        this.userType = input.getUserType();
        super.setAuthenticated(true);
    }


}
