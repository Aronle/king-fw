package com.mars.fw.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.fw.web.entity.KingOrgEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author King
 * @create 2020/4/21 14:40
 */
@Repository
@Mapper
public interface KingOrgMapper extends BaseMapper<KingOrgEntity> {

    /**
     * 测试
     * @param page
     * @return
     */
    Page<KingOrgEntity> pageByCode(Page<KingOrgEntity> page);
}
