package com.macro.mall.portal.util;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import me.hao0.common.security.MD5;
import me.hao0.common.util.Strings;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class WeiXinPayUtil {


    /**
     * 日志记录
     */
//    private static Logger logger = Logger.getLogger(WeiXinPayUtil.class);




    /**
     * 签名
     * @param params
     * @param paternerKey
     * @return
     */
    public static String getSign(Map<String, String> params, String paternerKey ) throws UnsupportedEncodingException {
        String string1 = createSign(params, false);
        String stringSignTemp = string1 + "&key=" + paternerKey;

        String signValue = Md5Util.encodeByMD5(stringSignTemp).toUpperCase();
        return  signValue;
    }

    /**
     * 签名2
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getSign2(Map<String, String> params)
            throws UnsupportedEncodingException {
        String string1 = createSign(params, false);
        String signValue = Md5Util.encodeByMD5(string1).toUpperCase();
        return  signValue;
    }
    public static String createSign(String characterEncoding,SortedMap<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + "daizhenyu888!");//最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        String aa=sb.toString();
        String sign = Md5Util.encodeByMD5(aa.trim()).toUpperCase();
        return sign;
    }


    /**
     * 排序
     * @param params
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> params, boolean encode) throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = value.toString();
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueString, "UTF-8"));
            } else {
                temp.append(valueString);
            }
        }
        return temp.toString();
    }

    /**
     * 将map转换为xml
     * @param arr
     * @return
     */
    public static String ArrayToXml(Map<String, String> arr) {
        String xml = "<xml>";

        Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            xml += "<" + key + ">" + val + "</" + key + ">";
        }

        xml += "</xml>";
        return xml;
    }


    /**
     * 解析xml为map
     * @param xml
     * @return
     * @throws Exception
     * @throws IOException
     */
    public static Map<String, String> xmlToMap(String xml) throws Exception{
            Map<String, String> map = new HashMap<>();
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
                Element e = (Element) iterator.next();
                map.put(e.getName(), e.getText());
                System.out.println(e.getName()+"="+e.getText());
            }
            return map;

    }

    public static void main(String[] args) throws Exception {
        Map<String, String> xmlToMap = xmlToMap("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[openid is invalid]]></return_msg></xml>");
        System.err.println(xmlToMap);
    }


    /**
     * 请求方法
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
//            logger.info("连接超时：{}"+ ce);
        } catch (Exception e) {
//            logger.info("https请求异常：{}"+ e);
        }
        return null;
    }

    /**
     * 微信回调参数解析
     * @param request
     * @return
     */
    public static String getPostStr(HttpServletRequest request){
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String s = "";

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String xml = sb.toString(); //次即为接收到微信端发送过来的xml数据
//        logger.info(xml+"========================");
        return xml;
    }
    /**
     * 支付请求前签名
     * @param params 参数(已经升序, 排出非空值和sign)
     * @return MD5的签名字符串(大写)
     */
    protected static String doSign(final Map<String, String> params) {
        StringBuilder signing = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!Strings.isNullOrEmpty(entry.getValue())){
                signing.append(entry.getKey()).append('=').append(entry.getValue()).append("&");
            }
        }

        // append key
        signing.append("key=").append("");

        // md5
        return MD5.generate(signing.toString(), false).toUpperCase();
    }

}
