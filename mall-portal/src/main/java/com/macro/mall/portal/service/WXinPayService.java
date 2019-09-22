package com.macro.mall.portal.service;



import com.macro.mall.portal.util.ReturnJson;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WXinPayService {


    ReturnJson WxinUnifiedOrder(Map<String, Object> mp, HttpServletRequest request) throws Exception;

    Map<String, String> dounifiedOrder(String attach, String total_fee, String body, String orderNo) throws Exception;

    String payBack(String resXml);
}
