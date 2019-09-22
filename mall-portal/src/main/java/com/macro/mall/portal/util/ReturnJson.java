package com.macro.mall.portal.util;

/**
 * @author DELL01
 * @Since 2019.03.22
 */
public class ReturnJson {
    private int code;
    private String msg;
    private Long count;
    private Object data;
    private Integer pageNum;

    public ReturnJson() {
    }

    /**
     * @return 成功
     */
    public static ReturnJson success() {
        ReturnJson json = new ReturnJson(0, "成功");
        return json;
    }

    /**
     * @param msg  信息
     * @param data 数据
     * @return 成功
     */
    public static ReturnJson success(String msg, Object data) {
        ReturnJson json = new ReturnJson(0, msg, data);
        return json;
    }

    public static ReturnJson success(Long count, Object data) {
        ReturnJson result = new ReturnJson();
        result.setCode(0);
        result.setMsg("加载成功");
        result.setCount(count);
        result.setData(data);
        return result;
    }

    public static ReturnJson success(Long count, Object data,Integer pageNum) {
        ReturnJson result = new ReturnJson();
        result.setCode(0);
        result.setMsg("加载成功");
        result.setCount(count);
        result.setData(data);
        result.setPageNum(pageNum);
        return result;
    }

    /**
     * @param data 数据
     * @return 成功
     */
    public static ReturnJson success(Object data) {
        ReturnJson json = new ReturnJson(0, "成功", data);
        return json;
    }

    /**
     * @param msg   信息
     * @param count 分页数据
     * @param data  数据
     * @return 成功
     */
    public static ReturnJson success(String msg, Long count, Object data) {
        ReturnJson json = new ReturnJson(0, msg, count, data);
        return json;
    }

    public ReturnJson(int code, String msg, Long count, Integer pageNum, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
        this.pageNum = pageNum;
    }

    /**
     * @return com.xx.xian.utils.ReturnJson
     * @Author szw
     * @Description   分页
     * @Date 10:03 2019/3/7
     * @Param [count, pageNum, data]
     **/
    public static ReturnJson success(Long count, Integer pageNum, Object data) {
        ReturnJson json = new ReturnJson(0, "成功", count, pageNum, data);
        return json;
    }

    /**
     * @return 失败
     */
    public static ReturnJson failException() {
        ReturnJson json = new ReturnJson(2, "数据异常");
        return json;
    }


    /**
     * @return 失败
     */
    public static ReturnJson fail() {
        ReturnJson json = new ReturnJson(1, "系统异常");
        return json;
    }

    /**
     * @param msg 信息
     * @return 失败
     */
    public static ReturnJson fail(String msg) {
        ReturnJson json = new ReturnJson(1, msg);
        return json;
    }


    public ReturnJson(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnJson(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ReturnJson(int code, String msg, Long count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}

