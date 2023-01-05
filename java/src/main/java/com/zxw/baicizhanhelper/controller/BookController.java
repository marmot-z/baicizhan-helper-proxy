package com.zxw.baicizhanhelper.controller;

import com.zxw.baicizhanhelper.entity.AddWordsRsp;
import com.zxw.baicizhanhelper.entity.Response;
import com.zxw.baicizhanhelper.service.BusinessService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/12
 */
@Slf4j
@RestController
@AllArgsConstructor
public class BookController {

    private BusinessService businessService;

    // TODO 增加参数验证
    @CrossOrigin
    @GetMapping("/books")
    public Response getUserBookList() throws Exception {
        return Response.successful(businessService.getBookList());
    }

    // TODO 增加删除单词接口
    @PutMapping("/book/{bookId}/word/{word}")
    public Response addWord(@PathVariable Integer bookId, @PathVariable String word) throws Exception {
        return Response.successful(Objects.nonNull(businessService.addWord2book(bookId, word)));
    }

    @DeleteMapping("/book/{bookId}/word/{topicId}")
    public Response removeWord(@PathVariable Integer bookId, @PathVariable Integer topicId) throws Exception {
        AddWordsRsp resp = businessService.removeWordFromBook(bookId, topicId);
        return Response.successful(Objects.nonNull(resp));
    }
}
