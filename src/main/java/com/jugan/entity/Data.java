package com.jugan.entity;

import java.util.List;

/**
 * @Author CL
 * @Date 2019/3/28-13:25
 */
public class Data {
    /**
     * 包长度
     */
    private int frameLen;

    /**
     * 帧类型
     */
    private int frameType;

    /**
     * ID
     */
    private String id;



    /**
     * 应答
     * 00 成功
     * 01 失败
     * 02 参数错误
     * 04 参数超限
     */
    private int code;

    /**
     * 通道值
     */
    private List<Channel> chnos;

    public int getFrameLen() {
        return frameLen;
    }

    public void setFrameLen(int frameLen) {
        this.frameLen = frameLen;
    }

    public int getFrameType() {
        return frameType;
    }

    public void setFrameType(int frameType) {
        this.frameType = frameType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {  return code; }

    public void setCode(int code) { this.code = code; }

    public List<Channel> getChnos() {
        return chnos;
    }

    public void setChnos(List<Channel> chnos) {
        this.chnos = chnos;
    }


}
