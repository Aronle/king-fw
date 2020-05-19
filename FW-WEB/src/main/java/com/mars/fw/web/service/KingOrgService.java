package com.mars.fw.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.fw.web.dto.KingOrgInput;
import com.mars.fw.web.entity.KingOrgEntity;
import com.mars.fw.web.mapper.KingOrgMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author King
 * @create 2020/4/21 14:43
 */
@Service
public class KingOrgService {

    @Autowired
    private KingOrgMapper kingOrgMapper;

    public List<KingOrgEntity> listKingOrg() {
        return kingOrgMapper.selectList(new QueryWrapper<KingOrgEntity>().gt("id", 0));
    }

    public Page<KingOrgEntity> pageKingOrg() {
        Page<KingOrgEntity> page = new Page<KingOrgEntity>();
        page.setCurrent(1);
        page.setSize(10);
        Page<KingOrgEntity> resultPage = kingOrgMapper.pageByCode(page);
        return (Page<KingOrgEntity>) kingOrgMapper.selectPage(page, new QueryWrapper<KingOrgEntity>().gt("id", 0));
    }

    public void addKingOrg(KingOrgInput input) {
        KingOrgEntity orgEntity = new KingOrgEntity();
        BeanUtils.copyProperties(input, orgEntity);
        kingOrgMapper.insert(orgEntity);
    }
}
