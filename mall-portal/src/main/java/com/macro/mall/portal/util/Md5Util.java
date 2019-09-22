package com.macro.mall.portal.util;

import java.security.MessageDigest;

/**
 * 2018.10.22
 * @author DELL01
 *  MD5加密工具类
 */
public class Md5Util {

	//公盐--xzff 徐州非凡的首字母
    private static final String PUBLIC_SALT = "xzff" ;

    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",  "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    /**
               *    用户密码加密，盐值为 ：私盐+公盐
     * @param  password 密码
     * @param  salt 私盐
     * @return  MD5加密字符串
     */
    public static String toMd5Password(String password,String salt){
        return encodeByMD5(PUBLIC_SALT+password+salt);
    }


    /**
     * md5加密算法
     * @param  originString----原始密码字符串
     * @return
     */
    public static String encodeByMD5(String originString){
        if (originString != null){
            try{
                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();//转换成了大写
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }


    /**
                * 转换字节数组为十六进制字符串
     * @param     字节数组
     * @return    十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /** 将一个字节转化成十六进制形式的字符串     */
    private static String byteToHexString(byte b){
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
               *     加密测试
     * @param args
     */
    public static void main(String[] args) {

    	String secret = Md5Util.toMd5Password("000000","abc");
		System.out.println(secret);  //A189EB453972B429530805CE7D919662
	}












}
