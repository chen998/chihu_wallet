package com.macro.mall.portal.controller;


import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.MyAnswer;
import com.macro.mall.portal.service.MyAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 添加做题记录管理Controller
 * Created by polo on 2019
 */
@Controller
@Api(tags = "MyAnswerController", description = "添加做题记录管理")
@RequestMapping("/MyAnswer")
public class MyAnswerController {

    @Autowired
    private MyAnswerService myAnswerService;

    @ApiOperation("添加做题记录管理")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(MyAnswer myAnswer) {
        int count = myAnswerService.add(myAnswer);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }



}
