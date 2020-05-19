package com.mars.fw.security.authentication.service;

import com.mars.fw.security.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * @Author King
 * @create 2020/5/6 15:28
 */
@Getter
@Setter
@Accessors(chain = true)
public class CustomUserDetails extends User {

    /**
     * 用户id
     */
    private Long userId;
    private Integer userType;
    private String userName;
    private String password;
    private String phone;
    private String email;
    private String nickName;
    private Integer sex;
    private Integer admin;
    private Integer status;
    private Integer deleted;
    private Long createAt;
    private Long companyId;


    public CustomUserDetails(UserEntity userEntity) {
        super(userEntity.getUserName(), userEntity.getPassword(), 1 == userEntity.getStatus() ? true : false,
                true, true,
                true, AuthorityUtils.NO_AUTHORITIES);

        this.userId = userEntity.getId();
        this.userName = userEntity.getUserName();
        this.userType = 1000;
        this.password = userEntity.getPassword();
        this.phone = userEntity.getPhone();
        this.email = userEntity.getEmail();
        this.nickName = userEntity.getNickName();
        this.sex = userEntity.getSex();
        this.admin = userEntity.getAdmin();
        this.status = userEntity.getStatus();
        this.deleted = userEntity.getDeleted();
        this.createAt = userEntity.getCreateAt();
        this.companyId = userEntity.getCompanyId();
    }

}
