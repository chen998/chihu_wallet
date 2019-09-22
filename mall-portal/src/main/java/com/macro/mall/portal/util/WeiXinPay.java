package com.macro.mall.portal.util;






import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WeiXinPay {




    /**
     * 微信统一下单
     * @param appid 微信开发者id/微信公众号id
     * @param mch_id 商户号
     * @param key 签名密匙
     * @param orderCode	订单号
     * @param total_fee	总价 单位为分
     * @param ip	终端ip
     * @param notify_url	回调地址
     * @param trade_type	支付类型（APP，JSAPI）
     * @param body	支付描述
     * @return
     * @throws Exception
     */
    public static Map<String, String> weixinUnifiedOrder(String appid, String mch_id, String orderNo, String total_fee, String ip, String notify_url, String trade_type, String body, String key) throws Exception{

        SortedMap<String, String> condition = new TreeMap<String, String>();
        //1应用ID
        condition.put("appid", appid);

        //2商户号
        condition.put("mch_id", mch_id);

        //3随机字符串
        condition.put("nonce_str", UUID.getUUID());

        //4商品描述
        condition.put("body", body);

        //5商户订单号
        condition.put("out_trade_no", orderNo);

        //6订单总金额，单位为分
        condition.put("total_fee", total_fee);

        //7终端IP
        condition.put("spbill_create_ip", ip);

        //8接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        condition.put("notify_url", notify_url);

        //9支付类型
        condition.put("trade_type", trade_type);
         System.out.println(condition);
        //签名
        String sign = WeiXinPayUtil.createSign("UTF-8",condition);

        System.out.println(sign);

        //签名
        String sign1 = WeiXinPayUtil.doSign(condition);
        System.out.println(sign1);
        condition.put("sign", sign1);
        condition.put("fee_type", "CNY");

        String xml = WeiXinPayUtil.ArrayToXml(condition);
        System.out.println(xml);
        //发送请求
        String result = WeiXinPayUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", xml);
        System.out.println(result);
        Map<String, String> resuleMap = WeiXinPayUtil.xmlToMap(result);

        resuleMap.put("ip", ip);

        if (result.indexOf("SUCCESS") != -1) {
            if(resuleMap.get("return_code")!=null && "SUCCESS".equals(resuleMap.get("return_code"))){
                //成功
                return resuleMap;
            }else{
                return resuleMap;
            }
        }else{
            return resuleMap;
        }
    }

    /**
     * 生成签名
     * @param map
     * @return
     */
    public static Map<String,String> getSign3(Map<String,String> map){
        try {
            if (map!=null) {
                HashMap<String, String> signMap = new HashMap<>();
                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                String packages = "prepay_id="+map.get("prepay_id");
                signMap.put("appId", map.get("appid"));
                signMap.put("timeStamp", timestamp);
                signMap.put("nonceStr", map.get("nonce_str"));
                signMap.put("package", packages);
                signMap.put("signType", "MD5");
                String sign = WeiXinPayUtil.getSign(signMap, "");
                signMap.put("paySign", sign);
                return signMap;
            }
        } catch (Exception e) {
        }
        return null;
    }


}
