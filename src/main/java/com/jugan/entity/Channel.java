package com.jugan.entity;


/**
 * 通道格式,通道号
 * @Author CL
 * @Date 2019/3/28-9:51
 */
public class Channel {

    /** * 数据格式 */
    private int chnoFormat;

    /** * 数据通道 */
    private int chno;

    /** * 通道值 */
    private Object chnoData;

    /** * 数据格式 */
    public int getChnoFormat() {
        return chnoFormat;
    }

    public void setChnoFormat(int chnoFormat) {
        this.chnoFormat = chnoFormat;
    }
    /** * 数据通道 */
    public int getChno() {
        return chno;
    }

    public void setChno(int chno) {
        this.chno = chno;
    }

    /** * 通道值 */
    public Object getChnoData() {
        return chnoData;
    }


    public void setChnoData(Object chnoData) {
        this.chnoData = chnoData;
    }
}
