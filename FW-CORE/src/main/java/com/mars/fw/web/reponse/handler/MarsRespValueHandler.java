package com.mars.fw.web.reponse.handler;

import com.mars.fw.web.reponse.King;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * @description: mvc 返回值统一处理 统一处理成Response实体结构 标准输出到前端
 * @author:dengjinde
 * @date:2020/4/20
 */
public class MarsRespValueHandler implements HandlerMethodReturnValueHandler {

    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    /**
     * 构造函数
     *
     * @param requestResponseBodyMethodProcessor
     */
    public MarsRespValueHandler(RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor) {
        this.requestResponseBodyMethodProcessor = requestResponseBodyMethodProcessor;
    }

    /**
     * 当返回值为 true的时候才会开启自定义的handler
     *
     * @param returnType
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) || returnType.hasMethodAnnotation(ResponseBody.class));
    }

    /**
     * 自定义handler逻辑 当返回值为空的时候自动返回标准格式
     * 这边的逻辑可以根据需要扩展
     *
     * @param returnValue
     * @param returnType
     * @param mavContainer
     * @param webRequest
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        King response = null;
        if (!King.class.isAssignableFrom(returnType.getParameterType())) {
            response = new King(King.SUCCESS_CODE, "success", returnValue);
        } else {
            response = (King) returnValue;
        }
        this.requestResponseBodyMethodProcessor.handleReturnValue(response, returnType, mavContainer, webRequest);
    }
}
