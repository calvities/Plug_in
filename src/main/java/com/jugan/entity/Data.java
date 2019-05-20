package com.jugan.entity;

import java.util.List;

/**
 * @Author CL
 * @Date 2019/3/28-13:25
 */
public class Data {

    /** * 地址域 */
    private int addressField;

    /** * 包长度 */
    private int frameLen;

    /** * 帧类型 */
    private int frameType;

    /** * ID */
    private String id;

    /** * 消息类型 */
    private String msgType = "deviceReq";

    /** * 是否有后续消息 */
    private int hasMore = 0;

    /** * 是否是错误 */
    private int errcode = 0;

    /** * 设备请求 */
    private byte bDeviceReq = 0x00;

    /** * 设备响应 */
    private byte bDeviceRsp = 0x01;

    /** * 是否有mid */
    private byte hasMid = 0x01;

    /** * 判断是否有mid */
    private boolean isContainMid = false;

    /** * mid */
    private int mid = 0;

    /**
     *  用来判断数据是否出错
     *  CRC8校验出错
     *  1:成功
     *  0:失败
     */
    private int isOk;


    /**
     * 应答
     * 00 成功
     * 01 失败
     * 02 参数错误
     * 04 参数超限
     */
    private int code;

    /** * 通道值 */
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

    public int getAddressField() { return addressField; }

    public void setAddressField(int addressField) { this.addressField = addressField; }

    public String getMsgType() {  return msgType;  }

    public void setMsgType(String msgType) { this.msgType = msgType; }

    public int getHasMore() { return hasMore; }

    public void setHasMore(int hasMore) {  this.hasMore = hasMore;  }

    public int getErrcode() { return errcode;  }

    public void setErrcode(int errcode) { this.errcode = errcode;  }

    public byte getbDeviceReq() {  return bDeviceReq;  }

    public void setbDeviceReq(byte bDeviceReq) { this.bDeviceReq = bDeviceReq; }

    public byte getbDeviceRsp() {  return bDeviceRsp;  }

    public void setbDeviceRsp(byte bDeviceRsp) { this.bDeviceRsp = bDeviceRsp; }

    public byte getHasMid() { return hasMid; }

    public void setHasMid(byte hasMid) { this.hasMid = hasMid;  }

    public boolean getIsContainMid() {  return isContainMid; }

    public void setIsContainMid(boolean containMid) { isContainMid = containMid;  }

    public int getMid() {  return mid; }

    public void setMid(int mid) { this.mid = mid;  }

    public int getIsOk() {  return isOk; }

    public void setIsOk(int isOk) {  this.isOk = isOk; }
}
