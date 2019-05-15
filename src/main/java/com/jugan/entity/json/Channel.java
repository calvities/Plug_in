/**
  * Copyright 2019 bejson.com 
  */
package com.jugan.entity.json;



/**
 * @Author CL
 * @Date 2019/4/9-14:29
 */
public class Channel {

    private long chno;

    private String vt;

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