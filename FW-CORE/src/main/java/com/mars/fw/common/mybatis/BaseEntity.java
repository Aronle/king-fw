package com.mars.fw.common.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定义通用的baseEntity 这边现在只定义了 四个属性 可以扩展
 * 在底层会使用切面将这些值自动注入
 *
 * @Author King
 * @create 2020/4/21 16:20
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -6206037862724173194L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleteFlag;

}
