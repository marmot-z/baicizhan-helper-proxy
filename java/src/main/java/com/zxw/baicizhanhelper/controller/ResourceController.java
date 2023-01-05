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
 * @date 2022/8/11
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ResourceController {

    private BusinessService businessService;

    @GetMapping("/search/word/{word}")
    public Response searchWord(@PathVariable String word) throws Exception {
        return Response.successful(businessService.searchWord(word));
    }

    @GetMapping("/word/{topicId}")
    public Response getWordDetail(@PathVariable Integer topicId) throws Exception {
        return Response.successful(businessService.getWordDetail(topicId));
    }
}
