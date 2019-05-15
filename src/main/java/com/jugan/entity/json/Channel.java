/**
  * Copyright 2019 bejson.com 
  */
package com.jugan.entity.json;



/**
 * 下发命令的通道部分
 * @Author CL
 * @Date 2019/4/9-14:29
 */
public class Channel {

    /**
     * 通道号
     */
    private long chno;
    /**
     * 通道类型
     */
    private String vt;
    /**
     * 通道值
     */
    private String value;

    public long getChno() {
        return chno;
    }

    public void setChno(long chno) {
        this.chno = chno;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}