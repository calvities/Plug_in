package com.jugan.entity.json;

import java.util.List;

/**
 * 下发命令
 * @Author CL
 * @Date 2019/5/15-17:05
 */
public class JsonRoot {

    /**
     * 版本号
     */
    private String ver;
    /**
     * 包名称
     */
    private String name;
    /**
     * 包类型
     */
    private String type;
    /**
     * 序列号
     */
    private Integer seq;
    /**
     * ID
     */
    private String ndid;
    /**
     * 下发命令时间
     */
    private String time;
    /**
     * 通道部分
     */
    private List<Channel> channel;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getNdid() {
        return ndid;
    }

    public void setNdid(String ndid) {
        this.ndid = ndid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Channel> getChannel() {
        return channel;
    }

    public void setChannel(List<Channel> channel) {
        this.channel = channel;
    }

}
