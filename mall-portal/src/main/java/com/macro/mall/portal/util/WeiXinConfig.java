package com.macro.mall.portal.util;


import java.util.Properties;

public class WeiXinConfig {


    /**
     * 预支付请求地址
     */
    public static final String  UnifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 查询订单地址
     */
    public static final String  OrderUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 关闭订单地址
     */
    public static final String  CloseOrderUrl = "https://api.mch.weixin.qq.com/pay/closeorder";

    /**
     * 申请退款地址
     */
    public static final String  RefundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 查询退款地址
     */
    public static final String  RefundQueryUrl = "https://api.mch.weixin.qq.com/pay/refundquery";

    /**
     * 下载账单地址
     */
    public static final String  DownloadBillUrl = "https://api.mch.weixin.qq.com/pay/downloadbill";

    /**
     * 商户APPID
     */
    public static final String  AppId = "wx95247b19d012c012";

    /**
     * 商户账户
     */
    public static final String  MchId = "1552778011";

    /**
     * 商户秘钥
     */
    public static final String  AppSercret = "2f72ae915c93241d5aa0a3e64caf0fb7";

    /**
     * 服务器异步通知页面路径
     */
    public static String notify_url = "http://localhost:8085/Wxin/weiXinPayCallback";

    /**
     * 页面跳转同步通知页面路径
     */
    public static String return_url = "";

    /**
     * 退款通知地址
     */
    public static String refund_notify_url = "";

    /**
     * 退款需要证书文件，证书文件的地址
     */
    public static String refund_file_path = "";

    /**
     * 商品名称
     */
    public static String subject =  "";

    /**
     * 商品描述
     */
    public static String body = "";

    private static  Properties properties;



}
