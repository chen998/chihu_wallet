package com.macro.mall.model;

import java.io.Serializable;

/**
 * 问题表实体类
 */
public class Questions  implements Serializable {
    private String id;
    private String content;
    private int queType;
    private String answer;
    private String queA;
    private String queB;
    private String queC;
    private String queD;
    private String createTime;
    private int money;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getQueType() {
        return queType;
    }

    public void setQueType(int queType) {
        this.queType = queType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQueA() {
        return queA;
    }

    public void setQueA(String queA) {
        this.queA = queA;
    }

    public String getQueB() {
        return queB;
    }

    public void setQueB(String queB) {
        this.queB = queB;
    }

    public String getQueC() {
        return queC;
    }

    public void setQueC(String queC) {
        this.queC = queC;
    }

    public String getQueD() {
        return queD;
    }

    public void setQueD(String queD) {
        this.queD = queD;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
