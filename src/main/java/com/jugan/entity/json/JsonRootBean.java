/**
  * Copyright 2019 bejson.com 
  */
package com.jugan.entity.json;
import java.util.List;

/**
 * @Author CL
 * @Date 2019/4/9-14:29
 */
public class JsonRootBean {

    private String ver;

    private String name;

    private String type;

    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}