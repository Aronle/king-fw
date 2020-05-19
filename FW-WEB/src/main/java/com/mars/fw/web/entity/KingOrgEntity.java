package com.mars.fw.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mars.fw.common.mybatis.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author King
 * @create 2020/4/21 14:35
 */
@Data
@Accessors(chain = true)
@TableName(value = "king_org")
public class KingOrgEntity extends BaseEntity {

    private String kingCode;

    private String kingName;

}
