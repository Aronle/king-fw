package com.mars.fw.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * @author king
 */
@TableName("king_user")
public class UserEntity extends Model<UserEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableId(value = "user_name")
    private String userName;
    @TableId(value = "user_type")
    private Long userType;
    @TableId(value = "password")
    private String password;
    @TableId(value = "phone")
    private String phone;
    @TableId(value = "email")
    private String email;
    @TableId(value = "nick_name")
    private String nickName;
    @TableId(value = "sex")
    private Integer sex;
    @TableId(value = "admin")
    private Integer admin;
    @TableId(value = "status")
    private Integer status;
    @TableId(value = "deleted")
    private Integer deleted;
    @TableId(value = "create_at")
    private Long createAt;
    @TableId(value = "companyId")
    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
