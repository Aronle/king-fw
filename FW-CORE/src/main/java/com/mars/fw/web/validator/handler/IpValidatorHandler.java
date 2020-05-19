package com.mars.fw.web.validator.handler;


import com.mars.fw.common.utils.ValidateUtils;
import com.mars.fw.web.validator.annotation.Ip;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * description:
 * 检验逻辑实现Handler
 *
 * @Ip校验的注解
 * @author:dengjinde
 * @date:2020/04/20
 */
public class IpValidatorHandler implements ConstraintValidator<Ip, String> {

    private String message;

    @Override
    public void initialize(Ip constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ValidateUtils.isIPAddr(value);
    }
}
