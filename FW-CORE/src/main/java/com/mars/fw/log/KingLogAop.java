package com.mars.fw.log;

import com.mars.fw.common.utils.JsonMapper;
import com.mars.fw.config.KingConfigure;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

/**
 * 日志切面 接口执行
 *
 * @Author King
 * @create 2020/4/22 15:21
 */
@Slf4j
@Aspect
@Component
public class KingLogAop {

    @Autowired
    private KingConfigure kingConfigure;

    public KingLogAop() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void ctrlAop() {
    }

    /**
     * 方法执行前日志操作
     *
     * @param joinPoint
     */
    @Around(value = "ctrlAop()")
    public Object beforeCtrl(ProceedingJoinPoint joinPoint) throws Throwable {

        boolean isLog = kingConfigure.isLog();

        //是否开启日志配置
        if (!isLog) {
            return joinPoint.proceed(joinPoint.getArgs());
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String uuid = UUID.randomUUID().toString().replaceAll("-", ",");
        String url = request.getRequestURL().toString();
        log.info("=============请求开始========" + uuid);
        log.info("请求地址: " + url);
        log.info("请求方式: " + request.getMethod());
        log.info("请求类方法: " + joinPoint.getSignature());
        log.info("请求接口参数: " + Arrays.toString(joinPoint.getArgs()));
        log.info("=============请求结束========" + uuid);

        Object result = joinPoint.proceed(joinPoint.getArgs());

        log.info("--------------返回内容----------------");
        log.info(url + "Response内容:" + JsonMapper.getDefault().toJson(result));
        log.info("--------------返回内容----------------");

        return result;

        //todo 可以定义日志的存储方式 eg:db存储，非关系数据存储，消息队列
    }

}
