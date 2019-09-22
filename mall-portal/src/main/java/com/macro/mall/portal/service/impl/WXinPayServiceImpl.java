package com.macro.mall.portal.service.impl;


import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.service.WXinPayService;
import com.macro.mall.portal.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WXinPayServiceImpl implements WXinPayService {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private UmsMemberMapper umsMemberMapper;

    //自定义的特殊产品货号
    @Value("${myCustom.prodctSn}")
    private String My_Custom_Prodct_Sn;

    @Override
    public ReturnJson WxinUnifiedOrder(Map<String, Object> mp, HttpServletRequest request) throws Exception{
        Map<String, String> result = new HashMap<>();
        String body = (String)mp.get("body");
        String orderNo = (String)mp.get("orderNo");
        String payMoney = (String)mp.get("payMoney");

        result= WeiXinPay.weixinUnifiedOrder(WeiXinConfig.AppId, WeiXinConfig.MchId,orderNo, payMoney, IpUtil.getIpAddr(request), "http://127.0.0.1:8085/Wxin/weiXinPayCallback", "APP", body, "");
        String returnCode =  result.get("return_code");
        String returnMSg =  result.get("return_msg");

        if(returnCode.equals("FAIL")){
            ReturnJson.fail(returnMSg);
        }




        return ReturnJson.success(returnMSg);
    }

    /**
     * 调用官方SDK 获取预支付订单等参数
     * @param attach 额外参数
     * @param total_fee 总价
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> dounifiedOrder(String attach, String total_fee,String body,String orderNo) throws Exception{
        WxMD5Util md5Util = new WxMD5Util();
        Map<String, String> returnMap = new HashMap<>();
        WXConfigUtil config = new WXConfigUtil();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        //生成商户订单号，不可重复
        //String out_trade_no = "wxpay" + System.currentTimeMillis();

        data.put("appid", config.getAppID());
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", attach);
        data.put("body", body);
        data.put("fee_type", "CNY");
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", "http://127.0.0.1:8085/Wxin/notify");
        data.put("out_trade_no", orderNo);
        //自己的服务器IP地址
        data.put("spbill_create_ip", "127.0.0.1");
        data.put("total_fee", total_fee);
        //交易类型
        data.put("trade_type", "APP");
        data.put("time_start", System.currentTimeMillis()+"");

        String sign = md5Util.getSign(data);
        data.put("sign", sign);





        try {
            //使用官方API请求预付订单
            Map<String, String> response = wxpay.unifiedOrder(data);
            System.out.println(response);
            String returnCode = response.get("return_code");   //获取返回码
            //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
            if (returnCode.equals("SUCCESS")) {//主要返回以下5个参数
                String resultCode = response.get("result_code");
                returnMap.put("appid", response.get("appid"));
                returnMap.put("mch_id", response.get("mch_id"));
                returnMap.put("nonce_str", response.get("nonce_str"));
                returnMap.put("sign", response.get("sign"));
                if ("SUCCESS".equals(resultCode)) {//resultCode 为SUCCESS，才会返回prepay_id和trade_type
                    //获取预支付交易回话标志
                    returnMap.put("trade_type", response.get("trade_type"));
                    returnMap.put("prepay_id", response.get("prepay_id"));
                    return returnMap;
                } else {
                    //此时返回没有预付订单的数据
                    return returnMap;
                }
            } else {
                return returnMap;
            }
        } catch (Exception e) {
            System.out.println(e);
            //系统等其他错误的时候
        }
        return returnMap;
    }

    /**
     *
     * @param notifyData 异步通知后的XML数据
     * @return
     */
    @Override
    public String payBack(String notifyData) {

        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(notifyData);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理

                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        //修改订单状态
                        OmsOrder order = new OmsOrder();
                        order.setOrderSn(out_trade_no);
                        order.setPayType(2);//支付方式：0->未支付；1->支付宝；2->微信
                        order.setSourceType(1);//订单来源：0->PC订单；1->app订单
                        order.setStatus(1);//订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
                        order.setOrderType(0);//订单类型：0->正常订单；1->秒杀订单
                        order.setConfirmStatus(0);//确认收货状态：0->未确认；1->已确认
                        order.setDeleteStatus(0);//删除状态：0->未删除；1->已删除
                        order.setPaymentTime(Tool.fromStringToDate(Tool.fromDateH(new Date())));
                        order.setModifyTime(new Date());
                        //修改订单状态
                        orderMapper.updateByOrderSnSelective(order);
                        //先查询此订单下产品
                        Map<String,Object>  mp = new HashMap<>();
                        mp.put("orderSn",out_trade_no);
                        List<Map<String,Object>> orderProducts = orderMapper.selectByOrderSn(mp);
                        //遍历订单下产品查询是否有特殊产品
                        for(Map<String,Object> mpo:orderProducts){
                            String productSn =  (String)mpo.get("product_sn");
                            if(productSn.equals(My_Custom_Prodct_Sn)){
                                String memberId =  (String)mpo.get("member_id");
                                UmsMember mem = new UmsMember();
                                mem.setId(Long.valueOf(memberId));
                                //设置成高级会员
                                mem.setStatus(3);
                                umsMemberMapper.updateByPrimaryKeySelective(mem);
                            }

                        }
                        System.err.println("支付成功");

                        System.out.println("微信手机支付回调成功订单号:{}"+out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.out.println("微信手机支付回调失败订单号:{}"+out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                System.out.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            System.out.println("手机支付回调通知失败"+ e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;

    }




}
