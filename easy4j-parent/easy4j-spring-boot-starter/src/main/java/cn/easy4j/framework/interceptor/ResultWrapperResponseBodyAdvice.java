package cn.easy4j.framework.interceptor;

import cn.easy4j.common.constant.OrderedConstant;
import cn.easy4j.common.response.Result;
import cn.easy4j.common.response.SuccessResult;
import cn.easy4j.framework.annotation.IgnoreResultWrapper;
import cn.easy4j.framework.util.JacksonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 拦截所有响应，封装一层标准响应格式
 *
 * @author yangzongmin
 * @date 2019/8/19
 */
@RestControllerAdvice
@Order(value = OrderedConstant.RESULT_WRAPPER_ORDER)
public class ResultWrapperResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果不想让方法走这个统一封装，加上注解@IgnoreResultWrapper即可
        return !returnType.hasMethodAnnotation(IgnoreResultWrapper.class);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // 写死swagger的放行地址、避免每次需要重复配置swagger放行
        String swaggerUrl1 = "/v2/api-docs";
        String swaggerUrl2 = "/swagger-resources";

        String path = request.getURI().getPath();
        if (path.startsWith(swaggerUrl1) || path.startsWith(swaggerUrl2)) {
            return body;
        }

        if (!(body instanceof Result)) {
            // 如果响应类不是Result，则套一层标准返回格式
            Result<Object> result = new SuccessResult<>(body);
            if (body == null) {
                return new SuccessResult<>(new Object());
            }
            if (body instanceof String) {
                return JacksonUtil.toJson(result);
            }
            return result;
        }
        return body;
    }

}
