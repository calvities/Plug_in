/**
  * Copyright 2019 bejson.com 
  */
package com.jugan.entity.json;
import java.util.List;

/**
 * @Author CL
 * @Date 2019/4/9-14:29
 */
public class Data {

    private String ndid;

    private String time;

    private List<Channel> channel;

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