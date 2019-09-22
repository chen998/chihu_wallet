package com.macro.mall.portal.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class Tool {

	//秒转成时分秒
	public String changeSec(String value) {
		int theTime = Integer.parseInt(value);//
		int theTime1 = 0;
		int theTime2 = 0;// 小时
		if(theTime > 60) {
			theTime1 = theTime/60;
			theTime = theTime%60;
			if(theTime1 > 60) {
				theTime2 = theTime1/60;
				theTime1 = theTime1%60;
			}
		}
		String result = ""+theTime+"秒";
		if(theTime1 > 0) {
			result = ""+theTime1+"分"+result;
		}
		if(theTime2 > 0) {
			result = ""+theTime2+"时"+result;
		}
		return result;
	}

	/**
	 *
	 * @Description 验证字符串是否为空
	 * @param
	 *
	 */
	public static boolean isEmpty(String s){
		if(s!=null && !s.trim().equals("")){
			return false;
		}
		return true;
	}
    public static boolean isNotEmpty(String s) {
        return  !isEmpty(s);
    }

	/**
	 *
	 * @Description 对象是空
	 * @param
	 *
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}


	/**
	 *
	 * @Description 首字母转小写
	 * @param
	 *
	 */
	public static String toLowerCaseFirstChar(String s){
		if(s==null || s.equals("")) return null;
		return s.replaceFirst(s.substring(0, 1),s.substring(0, 1).toLowerCase()) ;
	}

	/**
	 *
	 * @Description 首字母转大写
	 * @param
	 *
	 */
	public static String toUpCaseFirstChar(String s) {
		if(s==null || s.equals("")) return null;
		return s.replaceFirst(s.substring(0, 1),s.substring(0, 1).toUpperCase()) ;
	}
	/**
	 *
	 * @Description 格式化日期-->字符串
	 * @param
	 *
	 */
	public static String fromDateH(Date date) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format1.format(date);
	}
	/**
	 *
	 * @Description 格式化日期字符串-->
	 * @param
	 *
	 */
	public static Date fromStringToDate(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		try {
			date1 = sDateFormat.parse(date);
		}catch (ParseException px){
			px.printStackTrace();

		}

		return date1;
	}
}
