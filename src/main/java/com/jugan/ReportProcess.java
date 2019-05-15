package com.jugan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jugan.entity.Channel;
import com.jugan.entity.Data;
import com.jugan.tools.CrcByte;
import com.jugan.tools.Utilty;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author GL & CL
 * @Date 2019/4/30-11:51
 */
public class ReportProcess {

    private String msgType = "deviceReq";
    private int hasMore = 0;
    private int errcode = 0;
    private byte bDeviceReq = 0x00;
    private byte bDeviceRsp = 0x01;
    private byte hasMid = 0x01;
    private boolean isContainMid = false;
    private int mid = 0;
    private int addressField;//地址域

    /*公司所需*/
    private String name = "data";
    private String type = "ntf";
    private String serviceId0 = "General";
    private String serviceId1 = "Frist";//正式平台  记得改回来
    private String ver = "1.0";
    private int ft = 1;


    /*项目所需*/
    //private int k = 0;
    private int intData;
    private float floatData;
    private String octetData;
    private String stringData;
    private int isOk;
    private Data datas;
    private final int SUCCESS = 1;//魔术变量
    private final int FAIL = 0;//魔术变量
    private final String TYPEINT = "int",TYPEFLOAT = "float",TYPESTR = "str",TYPEOCTET = "octet";

    /**
     * 解析payload数据
     * 设备发送给平台coap报文的payload部分
     * @param binaryData binaryData为设备发过来的CoAP报文的payload部分
     */
    public ReportProcess(byte[] binaryData) {
       //检验crc8
        cheak(binaryData);

        /*地址域*/
        this.addressField = Utilty.getInstance().bytes2Int(binaryData,0,1);
        this.ft = Utilty.getInstance().bytes2Int(binaryData, 3, 1);
       // System.out.println(this.ft);
        switch (ft){//根据帧类型进行判断
            case 1:
               // System.out.println("我走帧类型为1的方法了");
                this.msgType = "deviceReq";
                /*解析数据*/
                this.datas = this.analysisBy_1(binaryData);//解析数据
                break;
            case 2:
                //System.out.println("我走帧类型为2的方法了");
                this.msgType = "deviceRsp";
                this.mid = Utilty.getInstance().bytes2Int(binaryData, 4, 2);
                if (Utilty.getInstance().isValidofMid(mid))
                    this.isContainMid = true;
                /*解析数据*/
                this.datas = this.analysisBy_2(binaryData);//解析数据
                break;
        }
    }

    /**
     * 组装body体
     *
     * @return
     */
    public ObjectNode toJsonNode() {
        try {
            if (this.isOk != this.SUCCESS)  return null;//当校验失败或帧类型不为1时,不组装json体
            //组装body体
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            // root.put("identifier", this.identifier);
            root.put("msgType", this.msgType);

            //根据msgType字段组装消息体
            if (this.msgType.equals("deviceReq")) {
                //if (addressField == 0) {
                root.put("hasMore", this.hasMore);
                ArrayNode arrynode = mapper.createArrayNode();
                ObjectNode brightNode = mapper.createObjectNode();

                if (this.addressField == this.FAIL) {//根据地址域判断是心跳数据还是设备启动第一帧数据
                    brightNode.put("serviceId", this.serviceId0);
                } else if (this.addressField == this.SUCCESS) {
                    brightNode.put("serviceId", this.serviceId1);
                }

                ObjectNode secondNode = mapper.createObjectNode();
                secondNode.put("ver", this.ver);
                secondNode.put("name", this.name);
                secondNode.put("type", this.type);
                ArrayNode secondArray = mapper.createArrayNode();
                ObjectNode thirdNode = mapper.createObjectNode();
                thirdNode.put("ndid", datas.getId());
                thirdNode.put("time", Utilty.obtainByTime());

                ArrayNode thirdArry = mapper.createArrayNode();
                for (Channel chno : this.datas.getChnos()) {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("chno", chno.getChno());
                    switch (chno.getChnoFormat()) {
                        case 0:
                            this.intData = Integer.parseInt(String.valueOf(chno.getChnoData()));
                            node.put("vt",this.TYPEINT);
                            node.put("value", this.intData);
                            break;
                        case 1:
                            this.floatData = Float.parseFloat(String.valueOf(chno.getChnoData()));
                            node.put("vt",this.TYPEFLOAT);
                            node.put("value", this.floatData);
                            break;
                        case 10:
                            this.octetData = String.valueOf(chno.getChnoData());
                            node.put("vt",this.TYPEOCTET);
                            node.put("value", this.octetData);
                            break;
                        case 11:
                            this.stringData = String.valueOf(chno.getChnoData());
                            node.put("vt",this.TYPESTR);
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
                // }
                datas = null;//清空集合
            } else {
                root.put("errcode", datas.getCode());
                //此处需要考虑兼容性，如果没有传mid，则不对其进行解码
                if (isContainMid) {
                    root.put("mid", this.mid);//mid
                }
                //组装body体，只能为ObjectNode对象
                ObjectNode body = mapper.createObjectNode();
                body.put("ver", "1.0");
                body.put("name", "cmd");
                body.put("type", "rsps");
                body.put("req", this.mid);
                body.put("ndid",datas.getId());
                body.put("time",Utilty.obtainByTime());
                body.put("result", 0);
                root.put("body", body);
                datas = null;//清空集合
            }
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解析数据
     * 帧类型为<1>
     * @param buf payload报文
     * @return
     */
    private Data analysisBy_1(byte[] buf) {
        Data data = new Data();
        int k = 3;//k从4时为payload部分
        /*ID*/
        byte[] id = new byte[16];
        for (int i = 0; i < id.length; i++)
            id[i] = buf[++k];
        //System.out.println(Utilty.parseByte2HexStr(id));
        //this.id = Utilty.hex2Str(Utilty.parseByte2HexStr(id));
        data.setId(Utilty.hex2Str(Utilty.parseByte2HexStr(id)));
        /*解析通道*/
        List<Channel> list = new ArrayList<>();
        //将通道部分数据赋给新数组
        byte[] bytes = new byte[buf.length - 21];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = buf[++k];//此时k值应当是21,否则报错
        /*
         * 循环思路:
         * 先解析第一个通道,解析完成后, 累加一个通道总长度(头信息 + 数据长度 + 数据部分),再创建新数组
         * 新数组长度 = 通道部分的数组 - 累加通道长度
         * 再解析新数组的第一个通道,解析完成后,再创建新数组
         * 循环以上部分
         * 当通道部分数组长度 减去 累加的通道长度为零时循环终止
         * */
        int kk = -1, p = 0;
        int numlen = bytes.length;//新数组长度
        int chnoLen = 0;//通道总长度
        do {
            byte[] payload = new byte[numlen];
            for (int i = 0; i < payload.length; i++)
                payload[i] = bytes[++kk];
            kk = -1;//初始化k
            byte[] head = new byte[1];//头信息
            head[0] = payload[p];
            Channel channel = ReportProcess.getChannel(Integer.parseInt(Utilty.parseByte2HexStr(head), 16));
            int len = payload[++p];//payload数据长度
            byte[] payData = new byte[len];
            for (int i = 0; i < payData.length; i++)
                payData[i] = payload[++p];
            Channel chno = ReportProcess.channelAssignment(payData,channel,len);
            list.add(chno);
            data.setChnos(list);
            p = 0;
            int chnolength = len + 2;//数据部分长度+头长度+数据长度的字节长度
            chnoLen = chnolength + chnoLen;//通道总长度
            kk = chnoLen + kk;//kk加上通道长,确保新数组是从解析的通道之后的部分
            numlen = numlen - chnolength;//通道部分数组长度 减去 累加的通道长度
            //System.out.println("\nchnoLen:" + chnoLen + "\tkk:" + kk+"\tnumLen:"+numlen+"\tchnolength:"+chnolength);
        } while (numlen != 0);


        return data;
    }

    /**
     * 解析数据
     * 帧类型为<2>
     * @param buf payload报文
     * @return
     */
    private Data analysisBy_2(byte[] buf) {
        Data data = new Data();
        int k = 5;//k从4时为payload部分

        /*ID*/
        byte[] id = new byte[16];
        for (int i = 0; i < id.length; i++)
            id[i] = buf[++k];
       // System.out.println(Utilty.parseByte2HexStr(id));
        data.setId(Utilty.hex2Str(Utilty.parseByte2HexStr(id)));
        /*应答*/
        int code = buf[++k];
        data.setCode(code);


        return data;
    }
    /**
     * 按通道号进行解析
     *
     * @param buf
     */
    private static Channel channelAssignment(byte[] buf,Channel channel,int len) {
        if (channel == null) return null;
        Channel chno = new Channel();
        switch (channel.getChnoFormat()) {
            case 0://按整型解析
                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
                long number = 0;
                try {
                    number = Long.parseLong(Utilty.parseByte2HexStr(buf),16);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //int data = Integer.parseInt(Utilty.parseByte2HexStr(buf), 16);
                chno.setChnoData(Utilty.num2Hex(number,len));
                break;
            case 1://按Float解析
                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
                float num = Utilty.getFloat(buf);
                chno.setChnoData(num);
                break;
            case 10://octet 字节流解析
                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
                String octet = Utilty.parseByte2HexStr(buf);
                chno.setChnoData(Utilty.toAscii(octet));
                break;
            case 11://ASCII码字符串
                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
                //chno.setChnoData(Utilty.hex2Str(Utilty.parseByte2HexStr(buf)));
                chno.setChnoData(Utilty.byteAsciiToString(buf));
                break;
        }

        return chno;
    }

    /**
     * 获取通道类型,和通道号
     *
     * @param head 头信息
     * @return
     */
    public static Channel getChannel(int head) {
        Channel channel = new Channel();//实例化对象
        String str = Utilty.toBinaryString(head);//转二进制
        channel.setChnoFormat(Integer.parseInt(str.substring(0, 2)));//获取头两个数值
        channel.setChno(Integer.parseInt(str.substring(2), 2));//获取后六位且转成十进制
        return channel;
    }


    /**
     * 检验CRC8
     * @param binaryData 报文
     */
    private void cheak(byte[] binaryData){
        //校验CRC
        //创建校验数组
        int add = 0;
        byte[] crc = new byte[binaryData.length - 2];
        //给校验数组赋值
        for (int i = 0; i < crc.length; i++)
            crc[i] = binaryData[++add];
        //调用CRC8校验方法
        int crcByte = CrcByte.cal_crc_table(crc, crc.length);
        //获取源数据校验值
        byte[] cr = new byte[1];
        cr[0] = binaryData[binaryData.length - 1];
        int crcs = Integer.parseInt(Utilty.parseByte2HexStr(cr), 16);
        System.out.println("校验值:" + crcByte + "\t\t\t实际值:" + crcs);
        //比较校验值
        if (crcByte == crcs) {
            this.isOk = this.SUCCESS;
            //校验成功
            //System.out.println("上报校验成功");
        } else {
            //校验失败
            this.isOk = this.FAIL;
            //System.out.println("上报校验失败");
            return;
        }
    }

}
