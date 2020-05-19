package com.mars.fw.mybatis;

import com.mars.fw.common.mybatis.BaseEntity;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * BaseEntity的切面 会自动注入定义属性的值 避免业务端重复写代码
 * 如果业务侧有特殊的需求 可以不继承BaseEntity
 * 对公司更友好的做法规定一种比较好的做法 大家都遵照执行
 *
 * @Author King
 * @create 2020/4/21 16:22
 */
@Aspect
@Component
public class BaseMapperAop {

    public BaseMapperAop() {
    }

    @Pointcut("this(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public void mapperPointCut() {

    }

    /**
     * 在执行insert方法前注入属性的值
     *
     * @param baseEntity
     */
    @Before("mapperPointCut()&&execution(public * *.insert(..))&&args(baseEntity)")
    public void beforeInsert(BaseEntity baseEntity) {
        LocalDateTime localDateTime = LocalDateTime.now();
        baseEntity.setCreateTime(localDateTime);
        baseEntity.setUpdateTime(localDateTime);
        baseEntity.setDeleteFlag(0);
        //todo 可扩展用户自动注入
    }

    /**
     *  在执行update方法前执行updateTime值的更新
     *
     * @param baseEntity
     */
    @Before("mapperPointCut()&&execution(public * *.updateById(..))&&args(baseEntity)")
    public void beforeUpdateById(BaseEntity baseEntity) {
        LocalDateTime localDateTime = LocalDateTime.now();
        baseEntity.setUpdateTime(localDateTime);
        //todo 可扩展用户自动注入
    }

}
