package com.zxw.baicizhanhelper.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxw.baicizhanhelper.entity.LogicException;
import com.zxw.baicizhanhelper.entity.Response;
import com.zxw.baicizhanhelper.entity.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/12
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({LogicException.class, SystemException.class})
    public Response handleException(Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        Map<String, String[]> parameters = request.getParameterMap();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            log.error("请求(path: {}, parameters: {}) 出现了异常", path, objectMapper.writeValueAsString(parameters), e);
        } catch (JsonProcessingException ex) {
            // ignore to json exception
        }

        if (e instanceof SystemException) {
            return Response.failure("系统异常，请稍后再试");
        }

        if (e instanceof LogicException) {
            return Response.unauthorized();
        }

        return Response.failure("系统异常，请反馈给开发人员");
    }
}
