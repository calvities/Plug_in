package com.jugan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jugan.entity.json.Channel;
import com.jugan.entity.json.JsonRoot;
import com.jugan.tools.CrcByte;
import com.jugan.tools.Utilty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CmdProcess {

    //private String identifier = "123";
    private String msgType = "deviceReq";
    private String serviceId = "General";
    private String cmd = "SET_DEVICE_LEVEL";
    private int hasMore = 0;
    private int errcode = 0;
    private boolean midExistent = false;
    private int mid = 0;
    private JsonNode paras;
    public int isOk;
    private final int SUCCESS = 1;//魔术变量
    private final int FAIL = 0;//魔术变量
    /** * 通道号和数据长度所占字节长度 */
    private final int HEADLEN = 2;
    JsonRoot rootBean = null;

    public CmdProcess() { }

    public CmdProcess(ObjectNode input) {

        try {
            // this.identifier = input.get("identifier").asText();
            this.msgType = input.get("msgType").asText();
            /*
            平台收到设备上报消息，编码ACK
            {
                "identifier":"0",
                "msgType":"cloudRsp",
                "request": ***,//设备上报的码流
                "errcode":0,
                "hasMore":0
            }
            * */
            if (msgType.equals("cloudRsp")) {
                this.check(input);//校验crc8
                //在此组装ACK的值
                this.errcode = input.get("errcode").asInt();
                this.hasMore = input.get("hasMore").asInt();
            } else {
            /*
            平台下发命令到设备，输入
            {
                "identifier":0,
                "msgType":"cloudReq",
                "serviceId":"WaterMeter",
                "cmd":"SET_DEVICE_LEVEL",
                "paras":{"value":"20"},
                "hasMore":0

            }
            * */
                //此处需要考虑兼容性，如果没有传mId，则不对其进行编码
                if (input.get("mid") != null) {
                    this.mid = input.get("mid").intValue();
                    this.midExistent = true;
                }
                this.serviceId = input.get("serviceId").asText();//获取serviceId
                this.cmd = input.get("cmd").asText();//命令名
                this.paras = input.get("paras");//命令
                this.hasMore = input.get("hasMore").asInt();
                this.rootBean = this.getRootBean(paras);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] toByte() {
        try {
            if (this.msgType.equals("cloudReq")) {
                /*
                应用服务器下发的控制命令，本例只有一条控制命令：SET_DEVICE_LEVEL
                如果有其他控制命令，增加判断即可。
                * */
                if (this.cmd.equals("SetData")) {
                    if (rootBean == null) return null;
                    StringBuilder sb = new StringBuilder();
                    int length = 0;//payload长度
                    String id = Utilty.toAscii(rootBean.getNdid());
                    sb.append(id);
                    length = length + id.length()/2;
                    for (Channel channel : rootBean.getChannel()){
                        String type = channel.getVt().toLowerCase();
                        long chnos = channel.getChno();
                        switch (type){
                            case "int":
                                sb.append(Utilty.parseByte2HexStr(chnos));
                                int valueInt = Integer.parseInt(channel.getValue());
                                byte[] bytes = Utilty.getInstance().int2Bytes(valueInt,2);
                                sb.append(Utilty.parseByte2HexStr(2));
                                sb.append(Utilty.parseByte2HexStr(bytes));
                                length = length + this.HEADLEN + 2;
                                break;
                            case "float":
                                int headFloat = Utilty.generatorHead(chnos, type);// 通道号
                                sb.append(Utilty.parseByte2HexStr(headFloat));
                                float valueFloat = Float.parseFloat(channel.getValue());
                                byte[] floatByte = Utilty.float2byte(valueFloat);
                                sb.append(Utilty.parseByte2HexStr(4));//通道数据长度
                                sb.append(Utilty.parseByte2HexStr(floatByte));//通道数据
                                length = length + this.HEADLEN + 4;
                                break;
                            case "octet":
                                int headOctet = Utilty.generatorHead(chnos, type);// 通道号
                                sb.append(Utilty.parseByte2HexStr(headOctet));
                                String valueOctet = Utilty.hex2Str(channel.getValue());
                                int octetLen = valueOctet.length() / 2;//计算通道长度
                                sb.append(Utilty.parseByte2HexStr(octetLen));//通道数据长度
                                sb.append(valueOctet);//通道数据
                                length = length + this.HEADLEN + octetLen;
                                break;
                            case "str":
                                int headStr = Utilty.generatorHead(chnos, type);// 通道号
                                sb.append(Utilty.parseByte2HexStr(headStr));
                                String valueStr = Utilty.toAscii(channel.getValue());
                                int strLen = valueStr.length() / 2;//计算通道长度
                                sb.append(Utilty.parseByte2HexStr(strLen));//通道数据长度
                                sb.append(valueStr);//通道数据
                                //System.out.println(valueStr);
                                length = length + this.HEADLEN + strLen;
                                break;
                        }
                    }

                    // System.out.println("length:" + length+"\t\tlen:"+String.valueOf(length+4));
                    //包长度(包长度 = 包长度的两个字节 + 包类型的一个字节 + payload长度 + CRC占得一个字节,
                    // 即 包长度 = 2(包长度) + 1(包类型长度) + 2(mid长度) + length(payload长度) + 1(CRC长度)
                    byte[] packetLength = Utilty.getInstance().int2Bytes(length+4,2);
                    byte[] packetType = {(byte)0x02};//包类型(占一个字节)
                    byte[] pl_pt = Utilty.concat(packetLength,packetType);//包长度+包类型

                    byte[] byteMid = new byte[2];//mid
                    if (this.midExistent) {//判断mid是否有
                        if (Utilty.getInstance().isValidofMid(this.mid))
                            byteMid = Utilty.getInstance().int2Bytes(this.mid, 2);
                    }else {
                        if (Utilty.getInstance().isValidofMid(rootBean.getSeq()))
                            byteMid = Utilty.getInstance().int2Bytes(rootBean.getSeq(),2);
                    }
                    byte[] payload = Utilty.strToByte(sb.toString());//数据部分
                    byte[] mid_payload = Utilty.concat(byteMid,payload);//payload部分 = 数据部分+mid部分
                    byte[] pl_pt_payload = Utilty.concat(pl_pt,mid_payload);//包长度+包类型+payload
                    //获取校验CRC8
                    int num = CrcByte.cal_crc_table(pl_pt_payload,pl_pt_payload.length);
                    //System.out.println("num:" + num);
                    byte[] crc = Utilty.getInstance().int2Bytes(num,1);//crc占一个字节
                    byte[] pl_pt_payload_crc = Utilty.concat(pl_pt_payload,crc);//包长度+包类型+payload+crc
                    return pl_pt_payload_crc;
                }
            }

            /*
            平台收到设备的上报数据，根据需要编码ACK，对设备进行响应，如果此处返回null，表示不需要对设备响应。
            * */
            else if (this.msgType.equals("cloudRsp")) {
                byte[] ack = new byte[5];
                //ByteBufUtils buf = new ByteBufUtils(ack);
                byte[] length = Utilty.getInstance().int2Bytes(3,2);
                byte[] type = {(byte)0x01};
                byte[] len_type = Utilty.concat(length,type);
                if (this.isOk == this.SUCCESS) {
                    byte[] success = {(byte)0x00};
                    byte[] len_type_success = Utilty.concat(len_type,success);
                    //获取crc8校验值
                    int num = CrcByte.cal_crc_table(len_type_success,len_type_success.length);
                    byte[] crc = Utilty.getInstance().int2Bytes(num,1);
                    byte[] len_type_success_crc = Utilty.concat(len_type_success,crc);
                    ack = len_type_success_crc;
                } else {
                    byte[] fail = {(byte)0x01};
                    byte[] len_type_fail = Utilty.concat(len_type,fail);
                    //获取crc8校验值
                    int num = CrcByte.cal_crc_table(len_type_fail,len_type_fail.length);
                    byte[] crc = Utilty.getInstance().int2Bytes(num,1);
                    byte[] len_type_fail_crc = Utilty.concat(len_type_fail,crc);
                    ack = len_type_fail_crc;
                }
                return ack;
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 校验CRC8
     * @param input
     * @throws Exception
     */
    private void check(ObjectNode input) throws Exception {
        //获取源数据
        byte[] buf = input.get("request").binaryValue();
        if (buf.length == 0) {
            this.isOk = this.FAIL;
            return;
        }
        int k = 0;
        byte[] bytes = new byte[buf.length - 2];//创建校验数组
        int crcByte;//给校验数组赋值
        for(crcByte = 0; crcByte < bytes.length; ++crcByte)
            bytes[crcByte] = buf[++k];
        //调用CRC8校验方法
        crcByte = CrcByte.cal_crc_table(bytes, bytes.length);
        byte[] cr = new byte[]{buf[buf.length - 1]};//获取源数据校验值
        int crcs = Integer.parseInt(Utilty.parseByte2HexStr(cr), 16);
        //System.out.println("校验值:" + crcByte + "\t\t\t实际值:" + crcs);
        if (crcByte == crcs) { //比较校验值
            this.isOk = this.SUCCESS;//校验成功
            //System.out.println("下发:校验成功");
        } else {
            this.isOk = this.FAIL;//校验失败
            //System.out.println("下发:校验失败");
        }

    }

    /**
     * 获得JsonRootBean对象
     * @param paras 实际下发的部分
     * @return
     */
    private JsonRoot getRootBean(JsonNode paras){
        try {
            //{"ver":"1.0","name":"cmd","type":"rqst","seq":12,"ndid":"0000000203000001","time":"20170101112233","channel":[{"chno":0,"vt":"int","value":1},{"chno":1,"vt":"float","value":2.0},{"chno":2,"vt":"int","value":30},{"chno":3,"vt":"str","value":"898601132512345"}]}
            //JsonNode paras = this.paras;
            if (paras == null)  return null;
            String ver = paras.get("ver").asText();//获取ver
            String name = paras.get("name").asText();//获取name
            String type = paras.get("type").asText();//获取type
            int seq = paras.get("seq").asInt();//获取序列号
            String ndid = paras.get("ndid").asText();//获取ID
            String time = paras.get("time").asText();//获取下发命令时间
            JsonNode channelData = paras.get("channel");//获取通道数组
            List<Channel> channelList = new ArrayList<>();
            for (Iterator channelElements = channelData.elements();channelElements.hasNext();){
                JsonNode channel = (JsonNode)channelElements.next();
                long chno = channel.get("chno").asLong();//获取通道号
                String vt = channel.get("vt").asText();//获取通道类型
                String value = channel.get("value").asText();//获取通道值
                //封装通道
                Channel channels = new Channel();
                channels.setChno(chno);
                channels.setVt(vt);
                channels.setValue(value);
                channelList.add(channels);
            }
            //封装命令包
            JsonRoot root = new JsonRoot();
            root.setVer(ver);
            root.setName(name);
            root.setType(type);
            root.setSeq(seq);
            root.setNdid(ndid);
            root.setTime(time);
            root.setChannel(channelList);
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
