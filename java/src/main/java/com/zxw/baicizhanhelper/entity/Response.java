package com.zxw.baicizhanhelper.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/12
 */
@Data
public class Response {
    private int code;
    private Object data;
    private String message;

    public static Response successful() {
        return successful(null);
    }

    public static Response successful(Object data) {
        Response response = new Response();
        response.code = HttpServletResponse.SC_OK;

        if (Objects.nonNull(data)) {
            response.data = data;
        }

        return response;
    }

    public static Response failure() {
        return failure(null);
    }

    public static Response failure(String message) {
        Response response = new Response();
        response.code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        if (StringUtils.isNotBlank(message)) {
            response.message = message;
        }

        return response;
    }

    public static Response unauthorized() {
        Response response = new Response();

        response.code = HttpServletResponse.SC_UNAUTHORIZED;
        response.message = "请重新登录";

        return response;
    }
}
