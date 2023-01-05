package com.zxw.baicizhanhelper.controller;

import com.zxw.baicizhanhelper.entity.Response;
import com.zxw.baicizhanhelper.service.BusinessService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/10
 */
@Slf4j
//@CrossOrigin
@RestController
@AllArgsConstructor
public class LoginController {

    private BusinessService businessService;

    @PostMapping("/login/sendSmsVerifyCode/{phoneNum}")
    public Response sendSmsVerifyCode(@PathVariable String phoneNum) throws Exception {
        businessService.sendSmsVerifyCode(phoneNum);
        return Response.successful();
    }

    @PostMapping("/login/{phoneNum}/{verifyCode}")
    public Response loginWithPhone(@PathVariable String phoneNum, @PathVariable String verifyCode) throws Exception {
        return Response.successful(businessService.loginWithPhone(phoneNum, verifyCode));
    }

    @GetMapping("/userInfo")
    public Response getUserInfo() throws Exception {
        return Response.successful(businessService.getUserInfo());
    }
}
