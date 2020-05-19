package com.mars.fw.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.fw.security.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: king
 * @Description:
 * @Date: 2020/04/29 10:16
 * @Version: 1.0
 */
@Repository
@Mapper
public interface KingUserMapper extends BaseMapper<UserEntity> {
}
