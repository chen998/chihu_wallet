package com.macro.mall.portal.service;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.RegisterVo;
import com.macro.mall.model.UmsMember;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 会员管理Service
 * Created by macro on 2018/8/3.
 */
public interface UmsMemberService {
    /**
     * 根据用户名获取会员
     */
    UmsMember getByUsername(String username);

    /**
     * 根据会员编号获取会员
     */
    UmsMember getById(Long id);

    /**
     * 用户注册
     */
    @Transactional
    CommonResult register(RegisterVo data);

    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 修改密码
     */
    @Transactional
    CommonResult updatePassword(RegisterVo data);

    /**
     * 获取当前登录会员
     */
    UmsMember getCurrentMember();


    /**
     * 根据username账号获取当前会员
     */
    UmsMember getMember(Long memberId);
    /**
     * 根据会员id修改会员积分
     */
    void updateIntegration(Long id,Integer integration);

    CommonResult login(String username, String password, HttpServletRequest request);

    Map<String, Object> addSignUp(Map<String, Object> mpt);
}
