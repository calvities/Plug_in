package com.jugan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jugan.entity.Channel;
import com.jugan.entity.Data;
import com.jugan.tools.AnalysisData;
import com.jugan.tools.Utilty;


/**
 * 解析设备发送给平台coap报文的payload部分
 * @Author CL
 * @Date 2019/4/30-11:51
 */
public class ReportProcess {


    /*公司所需的固定值*/
    private final String name = "data";
    private final String type = "ntf";
    /**
     * 心跳服务名
     */
    private final String serviceId0 = "General";
    /**
     * 第一次通电的服务名
     */
    private final String serviceId1 = "First";
    /**
     * 版本信息
     */
    private final String ver = "1.0";

    /*项目所需*/
    private int intData;
    private float floatData;
    private String octetData;
    private String stringData;
    private Data data;
    /**
     * 魔术变量
     */
    private final int SUCCESS = 1;
    /**
     * 魔术变量
     */
    private final int FAIL = 0;
    private final String TYPEINT = "int", TYPEFLOAT = "float", TYPESTR = "str", TYPEOCTET = "octet";

    /**
     * 解析payload数据
     * 设备发送给平台coap报文的payload部分
     *
     * @param binaryData binaryData为设备发过来的CoAP报文的payload部分
     */
    public ReportProcess(byte[] binaryData) {
        //调用解析类,解析数据并赋值给Data
        this.data = new AnalysisData().getData(binaryData);
    }

    /**
     * 组装body体
     *
     * @return
     */
    public ObjectNode toJsonNode() {

        try {

            //当校验失败或帧类型不为1时,不组装json体
            if (this.data.getIsOk() != this.SUCCESS) {
                return null;
            }
            //组装body体
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            // root.put("identifier", this.identifier);
            root.put("msgType", this.data.getMsgType());

            //根据msgType字段组装消息体
            if (this.data.getMsgType().equals("deviceReq")) {
                //if (addressField == 0) {
                root.put("hasMore", this.data.getHasMore());
                ArrayNode arrynode = mapper.createArrayNode();
                ObjectNode brightNode = mapper.createObjectNode();

                //根据地址域判断是心跳数据还是设备启动第一帧数据
                if (this.data.getAddressField() == this.FAIL) {

                    brightNode.put("serviceId", this.serviceId0);

                } else if (this.data.getAddressField() == this.SUCCESS) {

                    brightNode.put("serviceId", this.serviceId1);
                }

                ObjectNode secondNode = mapper.createObjectNode();
                secondNode.put("ver", this.ver);
                secondNode.put("name", this.name);
                secondNode.put("type", this.type);
                ArrayNode secondArray = mapper.createArrayNode();
                ObjectNode thirdNode = mapper.createObjectNode();
                thirdNode.put("ndid", data.getId());
                thirdNode.put("time", Utilty.obtainByTime());

                ArrayNode thirdArry = mapper.createArrayNode();
                for (Channel chno : this.data.getChnos()) {

                    ObjectNode node = mapper.createObjectNode();
                    node.put("chno", chno.getChno());

                    switch (chno.getChnoFormat()) {

                        case 0:
                            this.intData = Integer.parseInt(String.valueOf(chno.getChnoData()));
                            node.put("vt", this.TYPEINT);
                            node.put("value", this.intData);
                            break;
                        case 1:
                            this.floatData = Float.parseFloat(String.valueOf(chno.getChnoData()));
                            node.put("vt", this.TYPEFLOAT);
                            node.put("value", this.floatData);
                            break;
                        case 10:
                            this.octetData = String.valueOf(chno.getChnoData());
                            node.put("vt", this.TYPEOCTET);
                            node.put("value", this.octetData);
                            break;
                        case 11:
                            this.stringData = String.valueOf(chno.getChnoData());
                            node.put("vt", this.TYPESTR);
                            node.put("value", this.stringData);
                            break;
                    }
                    thirdArry.add(node);
                }
                thirdNode.put("channel", thirdArry);

                secondArray.add(thirdNode);
                secondNode.put("data", secondArray);
                brightNode.put("serviceData", secondNode);
                arrynode.add(brightNode);
                root.put("data", arrynode);
                // 清空集合
                data = null;

            } else {

                root.put("errcode", data.getCode());
                //此处需要考虑兼容性，如果没有传mid，则不对其进行解码
                if (this.data.getIsContainMid()) {
                    //mid
                    root.put("mid", this.data.getMid());
                }

                //组装body体，只能为ObjectNode对象
                ObjectNode body = mapper.createObjectNode();
                body.put("ver", "1.0");
                body.put("name", "cmd");
                body.put("type", "rsps");
                body.put("req", this.data.getMid());
                body.put("ndid", data.getId());
                body.put("time", Utilty.obtainByTime());
                body.put("result", data.getCode());
                root.put("body", body);
                //清空集合
                data = null;

            }

            return root;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
