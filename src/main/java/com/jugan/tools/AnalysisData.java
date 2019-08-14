package com.jugan.tools;

import com.jugan.entity.Channel;
import com.jugan.entity.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析数据类
 *
 * @Author CL
 * @Date 2019/5/20-11:13
 */
public class AnalysisData {
    /*项目所需*/
    /**
     * 魔术变量
     */
    private final int SUCCESS = 1;
    /**
     * 魔术变量
     */
    private final int FAIL = 0;

    public Data getData(byte[] binaryData) {

        Data data = new Data();
        //检验crc8
        int isOk = this.cheak(binaryData);
        data.setIsOk(isOk);
        /*地址域*/
        int addressField = Utilty.getInstance().bytes2Int(binaryData, 0, 1);
        data.setAddressField(addressField);
        int ft = Utilty.getInstance().bytes2Int(binaryData, 3, 1);
        data.setFrameType(ft);
        
        //根据帧类型进行判断
        switch (ft) {

            case 1:

                data.setMsgType("deviceReq");
                /*解析数据*/
                data = this.analysisBy_1(binaryData, data);
                break;

            case 2:

                data.setMsgType("deviceRsp");
                int mid = Utilty.getInstance().bytes2Int(binaryData, 4, 2);
                data.setMid(mid);
                if (Utilty.getInstance().isValidofMid(mid)) {
                    data.setIsContainMid(true);
                }
                /*解析数据*/
                data = this.analysisBy_2(binaryData, data);
                break;

        }

        return data;
    }


    /**
     * 解析数据
     * 帧类型为<1>
     *
     * @param buf payload报文
     * @return
     */
    private Data analysisBy_1(byte[] buf, Data data) {

        //k从4时为payload部分
        int k = 3;
        /*ID*/
        byte[] id = new byte[16];
        for (int i = 0; i < id.length; i++) {
            id[i] = buf[++k];
        }
        //System.out.println(Utilty.parseByte2HexStr(id));
        //this.id = Utilty.hex2Str(Utilty.parseByte2HexStr(id));
        data.setId(Utilty.hex2Str(Utilty.parseByte2HexStr(id)));
        /*解析通道*/
        List<Channel> list = new ArrayList<>();
        //将通道部分数据赋给新数组
        byte[] bytes = new byte[buf.length - 21];
        for (int i = 0; i < bytes.length; i++) {
            //此时k值应当是21,否则报错
            bytes[i] = buf[++k];
        }

        /*
         * 循环思路:
         * 先解析第一个通道,解析完成后, 累加一个通道总长度(头信息 + 数据长度 + 数据部分),再创建新数组
         * 新数组长度 = 通道部分的数组 - 累加通道长度
         * 再解析新数组的第一个通道,解析完成后,再创建新数组
         * 循环以上部分
         * 当通道部分数组长度 减去 累加的通道长度为零时循环终止
         * */
        int kk = -1, p = 0;
        //新数组长度
        int numlen = bytes.length;
        //通道总长度
        int chnoLen = 0;
        do {
            byte[] payload = new byte[numlen];
            for (int i = 0; i < payload.length; i++) {
                payload[i] = bytes[++kk];
            }
            //初始化k
            kk = -1;
            //头信息
            byte[] head = new byte[1];
            head[0] = payload[p];
            Channel channel = AnalysisData.getChannel(Integer.parseInt(Utilty.parseByte2HexStr(head), 16));
            //payload数据长度
            int len = payload[++p];
            byte[] payData = new byte[len];

            for (int i = 0; i < payData.length; i++) {
                payData[i] = payload[++p];
            }

            Channel chno = AnalysisData.channelAssignment(payData, channel, len);
            list.add(chno);
            data.setChnos(list);
            p = 0;
            //数据部分长度+头长度+数据长度的字节长度
            int chnolength = len + 2;
            //通道总长度
            chnoLen = chnolength + chnoLen;
            //kk加上通道长,确保新数组是从解析的通道之后的部分
            kk = chnoLen + kk;
            //通道部分数组长度 减去 累加的通道长度
            numlen = numlen - chnolength;
        } while (numlen != 0);


        return data;
    }

    /**
     * 解析数据
     * 帧类型为<2>
     *
     * @param buf payload报文
     * @return
     */
    private Data analysisBy_2(byte[] buf, Data data) {
        //k从4时为payload部分
        int k = 5;
        /*ID*/
        byte[] id = new byte[16];

        for (int i = 0; i < id.length; i++) {

            id[i] = buf[++k];
        }

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
    private static Channel channelAssignment(byte[] buf, Channel channel, int len) {

        if (channel == null) {
            return null;
        }
        Channel chno = new Channel();
        switch (channel.getChnoFormat()) {
            //按整型解析
            case 0:

                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
                long number = 0;

                try {

                    number = Long.parseLong(Utilty.parseByte2HexStr(buf), 16);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                chno.setChnoData(Utilty.num2Hex(number, len));
                break;
            //按Float解析
            case 1:

                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
                float num = Utilty.getFloat(buf);
                chno.setChnoData(num);
                break;

            //octet 字节流解析
            case 10:

                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
                String octet = Utilty.parseByte2HexStr(buf);
                chno.setChnoData(Utilty.toAscii(octet));
                break;

            //ASCII码字符串
            case 11:
                chno.setChno(channel.getChno());
                chno.setChnoFormat(channel.getChnoFormat());
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
        //实例化对象
        Channel channel = new Channel();
        //转二进制
        String str = Utilty.toBinaryString(head);
        //获取头两个数值
        channel.setChnoFormat(Integer.parseInt(str.substring(0, 2)));
        //获取后六位且转成十进制
        channel.setChno(Integer.parseInt(str.substring(2), 2));
        return channel;
    }


    /**
     * 检验CRC8
     *
     * @param binaryData 报文
     */
    private int cheak(byte[] binaryData) {

        try {
            //校验CRC
            //创建校验数组
            int add = 0;
            byte[] crc = new byte[binaryData.length - 2];
            //给校验数组赋值
            for (int i = 0; i < crc.length; i++) {
                crc[i] = binaryData[++add];
            }
            //调用CRC8校验方法
            int crcByte = CrcByte.cal_crc_table(crc, crc.length);
            //获取源数据校验值
            byte[] cr = new byte[1];
            cr[0] = binaryData[binaryData.length - 1];
            int crcs = Integer.parseInt(Utilty.parseByte2HexStr(cr), 16);
            //比较校验值
            if (crcByte == crcs) {
                return this.SUCCESS;
                //校验成功
            } else {
                //校验失败
                return this.FAIL;

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return this.FAIL;
        }
    }

}
