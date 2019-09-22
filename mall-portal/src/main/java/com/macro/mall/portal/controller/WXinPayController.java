package com.macro.mall.portal.controller;


import com.macro.mall.portal.service.WXinPayService;
import com.macro.mall.portal.util.ReturnJson;
import com.macro.mall.portal.util.WeiXinPayUtil;
import com.macro.mall.portal.util.WxMD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Wxin")
public class WXinPayController {

    @Autowired
    WXinPayService wpService;
    /**
     *
     * 统一下单
     */
    @RequestMapping("/WxinUnifiedOrder")
    @ResponseBody
    public ReturnJson WxinUnifiedOrder(String orderNo, String payMoney, String description, HttpServletRequest request) throws Exception{
        Map<String,Object>  mp = new HashMap<String,Object>();

        mp.put("orderNo",orderNo); //订单编号
        mp.put("body",description); //订单描述
        mp.put("payMoney",payMoney); //支付金额

        ReturnJson rj = wpService.WxinUnifiedOrder(mp, request);

        return rj;
    }

    @RequestMapping("/weiXinPayCallback")
    @ResponseBody
   public  ReturnJson weiXinPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception{

        //获取微信支付回调结果
        String result = WeiXinPayUtil.getPostStr(request);
        System.out.println(result);
        //把回调转成map
        Map<String, String> resuleMap = WeiXinPayUtil.xmlToMap(result);

        if(resuleMap.get("result_code")!=null && "SUCCESS".equals(resuleMap.get("result_code"))){
            //成功
            //获取实际支付的钱数
            String totalFee = resuleMap.get("total_fee");
            //获取订单号
            String orderNo = resuleMap.get("out_trade_no").split("_")[0];
            //获取交易流水号
            String tradeNo = resuleMap.get("transaction_id");
            //修改订单状态
            //这里是我根据订单编号修改我的订单信息，更多的字段意思看微信官方文档给出的返回结果来获取你想要的信息
            /*Order order=orderService.selectbyordernumber(orderNo);
            order.setPaymentType("2");            //支付完成
            order.setPayMoney(order.getCurrent());
            order.setSerialNumber(tradeNo);
            order.setOrderStatus(2);
            Integer zhifuddoilb=0;
            int returnResult = orderService.updateByPrimaryKeySelective(order);    //更新交易表中状态
            //判断订单表数据是否更新成功*/
           // if(returnResult>0){
                //在这里处理你支付成功的业务逻辑。

            /*}else {
                return ReturnJson.fail("支付失败");
            }*/
        }

        return ReturnJson.success("支付成功");

   }


    @RequestMapping("/wepay")
    @ResponseBody
    public Map<String, String> wxPay(String memberId, String total_fee,String body,String orderNo) throws Exception {

        String attach = "{\"user_id\":\"" + memberId + "\"}";
        //请求预支付订单
        Map<String, String> result = wpService.dounifiedOrder(attach, total_fee,body,orderNo);
        Map<String, String> map = new HashMap<>();

        WxMD5Util md5Util = new WxMD5Util();
        //返回APP端的数据
        //参加调起支付的签名字段有且只能是6个，分别为appid、partnerid、prepayid、package、noncestr和timestamp，而且都必须是小写
        //参加调起支付的签名字段有且只能是6个，分别为appid、partnerid、prepayid、package、noncestr和timestamp，而且都必须是小写
        //参加调起支付的签名字段有且只能是6个，分别为appid、partnerid、prepayid、package、noncestr和timestamp，而且都必须是小写
        map.put("appid", result.get("appid"));
        map.put("partnerid", result.get("mch_id"));
        map.put("prepayid", result.get("prepay_id"));
        map.put("package", "Sign=WXPay");
        map.put("noncestr", result.get("nonce_str"));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));//单位为秒
//      这里不要使用请求预支付订单时返回的签名
//      这里不要使用请求预支付订单时返回的签名
//      这里不要使用请求预支付订单时返回的签名
        map.put("sign", md5Util.getSign(map));
        map.put("extdata", attach);
        return map;
    }


    /**
     *   支付异步结果通知，我们在请求预支付订单时传入的地址
     *   官方文档 ：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3
     */
    @RequestMapping(value = "/notify", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wpService.payBack(resXml);
            return result;
        } catch (Exception e) {
            System.out.println("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }










}
