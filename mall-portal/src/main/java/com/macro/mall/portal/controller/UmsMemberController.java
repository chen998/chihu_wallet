package com.macro.mall.portal.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.RegisterVo;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员登录注册管理Controller
 * Created by macro on 2018/8/3.
 */
@Controller
@Api(tags = "UmsMemberController", description = "会员登录注册管理")
@RequestMapping("/sso")
public class UmsMemberController {
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult register(@RequestBody RegisterVo data) {
        return memberService.register(data);
    }
    @ApiOperation(value = "登录以后返回成员信息")
    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestBody UmsMember member, BindingResult result, HttpServletRequest request) {
        CommonResult memInfo = memberService.login(member.getUsername(), member.getPassword(),request);
        if (memInfo == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }


        return memInfo;


    }
    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam String telephone) {
        return memberService.generateAuthCode(telephone);
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestBody RegisterVo data) {
        return memberService.updatePassword(data);
    }
    @ApiOperation("签到")
    @RequestMapping(value = "/addSignUp", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addSignUp(String memberId) {
        Map<String,Object> mpt = new HashMap<String,Object>();
        mpt.put("memberId",memberId);
        Map<String,Object> mp = memberService.addSignUp(mpt);
        return CommonResult.success(mp);
    }
}
