package com.seeds.admin.mapping;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * RequestMappingHandlerMapping包装类，实现controller继承拼接父类上的path
 * @author hang.yu
 * @date 2022/7/15
 **/
public class RequestMappingWrapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
        Class<?> superClass = handlerType.getSuperclass();
        mappingInfo = appendSuperRequestMapping(superClass,mappingInfo);
        return mappingInfo;
    }

    /**
     * 添加父类的mapping
     * @param handlerType type
     * @param mappingInfo mapping
     * @return RequestMappingInfo
     */
    protected RequestMappingInfo appendSuperRequestMapping(Class<?> handlerType,RequestMappingInfo mappingInfo) {
        if(handlerType == null || mappingInfo == null) {
            return mappingInfo;
        }
        RequestMapping superRequestMapping = handlerType.getAnnotation(RequestMapping.class);
        if(superRequestMapping != null && superRequestMapping.value().length > 0) {
            RequestMappingInfo typeInfo = this.createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                mappingInfo = typeInfo.combine(mappingInfo);
            }
        }
        return appendSuperRequestMapping(handlerType.getSuperclass(),mappingInfo);
    }

    @Nullable
    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition<?> condition = element instanceof Class ? this.getCustomTypeCondition((Class)element) : this.getCustomMethodCondition((Method)element);
        return requestMapping != null ? this.createRequestMappingInfo(requestMapping, condition) : null;
    }

}
