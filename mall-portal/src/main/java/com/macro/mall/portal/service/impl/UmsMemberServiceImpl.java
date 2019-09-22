package com.macro.mall.portal.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.UmsMemberLevelMapper;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.model.*;
import com.macro.mall.portal.domain.MemberDetails;
import com.macro.mall.portal.service.RedisService;
import com.macro.mall.portal.service.UmsMemberService;
import com.macro.mall.portal.util.SmsUtils;
import com.macro.mall.portal.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 会员管理Service实现类
 * Created by macro on 2018/8/3.
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Autowired
    private UmsMemberMapper memberMapper;
    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public UmsMember getByUsername(String username) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        }
        return null;
    }

    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public CommonResult register(RegisterVo data) {
        //验证验证码
        if(!verifyAuthCode(data.getAuthCode(),data.getTelephone())){
            return CommonResult.failed("验证码错误");
        }
        //查询是否已有该用户
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(data.getUsername());
        example.or(example.createCriteria().andPhoneEqualTo(data.getTelephone()));
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsMembers)) {
            return CommonResult.failed("该用户已经存在");
        }
        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(data.getUsername());
        umsMember.setNickname(data.getNickname()==null?"默认昵称":data.getNickname());
        umsMember.setPhone(data.getTelephone());
        umsMember.setPassword(passwordEncoder.encode(data.getPassword()));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        //获取默认会员等级并设置
        UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
        levelExample.createCriteria().andDefaultStatusEqualTo(1);
        List<UmsMemberLevel> memberLevelList = memberLevelMapper.selectByExample(levelExample);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            umsMember.setMemberLevelId(memberLevelList.get(0).getId());
        }
        memberMapper.insert(umsMember);
        umsMember.setPassword(null);
        return CommonResult.success(null,"注册成功");
    }

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<6;i++){
            sb.append(random.nextInt(10));
        }
        String code = sb.toString();
        try {
            SendSmsResponse sendSms = SmsUtils.sendSms(telephone,code);//填写你需要测试的手机号码
        }catch (ClientException e){
            e.printStackTrace();
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE+telephone,sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE+telephone,AUTH_CODE_EXPIRE_SECONDS);
        return CommonResult.success(sb.toString(),"获取验证码成功");
    }

    @Override
    public CommonResult updatePassword(RegisterVo data) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(data.getTelephone());
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(memberList)){
            return CommonResult.failed("该账号不存在");
        }
        //验证验证码
        if(!verifyAuthCode(data.getAuthCode(),data.getTelephone())){
            return CommonResult.failed("验证码错误");
        }
        UmsMember umsMember = memberList.get(0);
        umsMember.setPassword(passwordEncoder.encode(data.getPassword()));
        memberMapper.updateByPrimaryKeySelective(umsMember);
        return CommonResult.success(null,"密码修改成功");
    }

    @Override
    public UmsMember getCurrentMember() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        System.err.println(auth.getPrincipal());
        MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
        return memberDetails.getUmsMember();
    }

    @Override
    public UmsMember getMember(Long memberId) {
        UmsMember umsMember;
        umsMember = memberMapper.selectByPrimaryKey(memberId);
        System.out.println(umsMember);
        return umsMember;
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember record=new UmsMember();
        record.setId(id);
        record.setIntegration(integration);
        memberMapper.updateByPrimaryKeySelective(record);
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone){
        if(StringUtils.isEmpty(authCode)){
            return false;
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        return authCode.equals(realAuthCode);
    }
    @Override
    public CommonResult login(String username, String password, HttpServletRequest request) {
        Map<String, Object> tokenMap = new HashMap<>();
        //密码需要客户端加密后传递
        UmsMember umsMember = null;
        try {
            umsMember = memberMapper.selectByUserName(username);
            if(umsMember==null){
                return CommonResult.failed("用户不存在");
            }
            if(!passwordEncoder.matches(password,umsMember.getPassword())){
                return CommonResult.failed("验证码错误");
            }
         /*   MemberDetails memberDetails = new MemberDetails(umsMember);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);*/
            request.getSession().setAttribute("umsMember",umsMember);
            HttpSession session = request.getSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tokenMap.put("memInfo", umsMember);
        tokenMap.put("msg", "登录成功");

        return CommonResult.success(tokenMap);

    }
    @Override
    public Map<String, Object> addSignUp(Map<String, Object> mpt) {
        Map<String,Object> mat = new HashMap<String,Object>();

        List<Map<String,Object>> slist =  memberMapper.selectCurPerIsHaveSignUp(mpt);
        if (slist.size()>0){
            mat.put("success",-1);
            mat.put("msg","签到失败，您已经签到过了!");
            return mat;
        }else{
            mpt.put("id", UUID.getUUID());
            mpt.put("money",3);
            //添加签到表
            int ms =  memberMapper.addSignUp(mpt);
            //修改会员积分或金额
            int um = memberMapper.updateSignUpMoney(mpt);
            if(ms>0&&um>0){
                mat.put("success",1);
            }else{
                mat.put("success",-1);
            }
            return mat;
        }
    }
}
