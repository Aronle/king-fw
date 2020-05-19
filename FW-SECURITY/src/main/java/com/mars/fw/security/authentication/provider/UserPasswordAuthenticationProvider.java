package com.mars.fw.security.authentication.provider;

import com.mars.fw.security.authentication.service.CustomUserDetailsService;
import com.mars.fw.security.tool.model.CustomAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;


/**
 * @description: 自定义用户名密码登录
 * @author: aron
 * @date: 2019-07-04 11:45
 */
public class UserPasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;
    private CustomUserDetailsService userDetailsService;

    private String userNotFoundEncodedPassword;

    public UserPasswordAuthenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials()
                .toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
        this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        CustomAuthenticationToken auth = (CustomAuthenticationToken) authentication;
        UserDetails loadedUser;

        try {
            loadedUser = this.userDetailsService.loadUserByUsernameAndCompanyTypeAndPlatform(auth.getPrincipal()
                    .toString());
        } catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials()
                        .toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, "
                    + "which is an interface contract violation");
        }
        return loadedUser;
    }
}
