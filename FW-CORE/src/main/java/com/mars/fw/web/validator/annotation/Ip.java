package com.mars.fw.web.validator.annotation;


import com.mars.fw.web.validator.handler.IpValidatorHandler;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @description: java的JSR 303
 * 自定义IP校验注解 扩展的是javax.validation
 * @author:dengjinde
 * @date:2018/11/26
 */
@Documented
@Constraint(validatedBy = {IpValidatorHandler.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface Ip {

    String message() default "{constraint.default.Ip.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
