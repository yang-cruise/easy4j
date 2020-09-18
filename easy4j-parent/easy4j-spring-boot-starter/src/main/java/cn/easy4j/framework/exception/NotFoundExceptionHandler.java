package cn.easy4j.framework.exception;

import cn.easy4j.common.enums.HttpStatusEnum;
import cn.easy4j.common.response.FailedResult;
import cn.easy4j.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * 处理404异常
 *
 * @author yangzongmin
 * @since 2019-08-14
 */
@Slf4j
@RestController
public class NotFoundExceptionHandler {

    @Resource
    private BasicErrorController basicErrorController;

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<Object> error(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        ResponseEntity<Map<String, Object>> error = basicErrorController.error(request);
        log.info("请求路径：{}，404异常：{}", Objects.requireNonNull(error.getBody()).get("path"), error.toString());
        return new FailedResult<>(HttpStatusEnum.NOT_FOUND.code(), HttpStatusEnum.NOT_FOUND.msg());
    }
}
