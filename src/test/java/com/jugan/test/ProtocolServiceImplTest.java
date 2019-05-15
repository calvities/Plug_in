package com.jugan.test;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;
import com.jugan.ProtocolAdapterImpl;
import com.jugan.tools.Utilty;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ProtocolServiceImplTest {

    private IProtocolAdapter protocolAdapter;

    @Before
    public void setProtocolAdapter() {
        this.protocolAdapter = new ProtocolAdapterImpl();
    }

    /**
     * 测试用例1：设备向平台上报数据。
     * <p>
     * <pre>
     * 设备上报数据:AA72000032088D0320623399
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void testDecodeDeviceReportData() throws Exception {
        byte[] deviceReqByte = initDeviceReqByte();
        //System.out.println(Utilty.parseByte2HexStr(deviceReqByte));
        ObjectNode objectNode = protocolAdapter.decode(deviceReqByte);
        //System.out.println(Utilty.parseByte2HexStr(aaa()));
        /*ObjectNode objectNode = protocolAdapter.decode(aaa());*/
        String str = null;
        if (objectNode != null){
            str = objectNode.toString();
        }
        //String str = (objectNode == null) ? null:objectNode.toString();
        System.out.println("数据上报:" + str);
    }

    /**
     * 测试用例2：平台向设备下发控制命令:
     */
 /*   @Test
    public void testEncodeIoTSendCommand() throws Exception {
        ObjectNode CloudReqObjectNode = initCloudReqObjectNode();
        System.out.println(CloudReqObjectNode.toString());
        byte[] outputByte = protocolAdapter.encode(CloudReqObjectNode);
        System.out.println("\ncloudReq output:" + parseByte2HexStr(outputByte));
    }*/

    /**
     * 测试用例3：设备对平台命令的应答消息 有命令短id
     * 设备应答消息:AA7201000107E0
     * @throws Exception
     */
/*    @Test
    public void testDecodeDeviceResponseIoT() throws Exception {
        byte[] deviceRspByte = initDeviceRspByte();//initDeviceReqByte();//
        ObjectNode objectNode = protocolAdapter.decode(deviceRspByte);
        String str = (objectNode == null) ? null:objectNode.toString();
        System.out.println("平台设备响应:" + str);
    }*/
    /**
     * 测试用例4：平台收到设备的上报数据后对设备的应答，如果不需要应答则返回null即可
     * {
     * "identifier": "0",
     * "msgType": "cloudRsp",
     * "request": [AA,72,00,00,32,08,8D,03,20,62,33,99],
     * "errcode": 0,
     * "hasMore": 0
     * }
     * @throws Exception
     */
    @Test
    public void testEncodeIoTResponseDevice() throws Exception {
        byte[] deviceReqByte = initDeviceReqByte();
        ObjectNode cloudRspObjectNode = initCloudRspObjectNode(deviceReqByte);
        byte[] outputByte2 = protocolAdapter.encode(cloudRspObjectNode);
        System.out.println("cloudRsp output:" + parseByte2HexStr(outputByte2));
    }
    /**
     * 把 byte类型数组转换成16进制的字符串
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {
        if (null == buf) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

private static byte[] aaa(){
        byte[] buf = {(byte)0x00,(byte)0x00,(byte)0x36,(byte)0x02,(byte)0x00,(byte)0x9A,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x32,(byte)0x30,(byte)0x33,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x00,(byte)0x02,(byte)0x0E,(byte)0x28,(byte)0x01,(byte)0x02,(byte)0x01,(byte)0x35,(byte)0x02,(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0x02,(byte)0x00,(byte)0x1A,(byte)0x08,(byte)0x02,(byte)0x00,(byte)0x0F,(byte)0x09,(byte)0x02,(byte)0x00,(byte)0xA1,(byte)0x6D};
        return buf;
}

    /**
     * 初始化：设备数据上报码流
     * @return
     */
   /* private static byte[] initDeviceReqByte() {
        // 本例入参：
        //00 0030 01
        byte[] byteDeviceReq = new byte[49];
        byteDeviceReq[0] = (byte) 0x00;
        byteDeviceReq[1] = (byte) 0x00;
        byteDeviceReq[2] = (byte) 0x30;
        byteDeviceReq[3] = (byte) 0x01;
        // 30 30 30 30 30 30 30 32 30 33 30 30 30 31 35 34
        byteDeviceReq[4] = (byte) 0x30;
        byteDeviceReq[5] = (byte) 0x30;
        byteDeviceReq[6] = (byte) 0x30;
        byteDeviceReq[7] = (byte) 0x30;
        byteDeviceReq[8] = (byte) 0x30;
        byteDeviceReq[9] = (byte) 0x30;
        byteDeviceReq[10] = (byte) 0x30;
        byteDeviceReq[11] = (byte) 0x32;
        byteDeviceReq[12] = (byte) 0x30;
        byteDeviceReq[13] = (byte) 0x33;
        byteDeviceReq[14] = (byte) 0x30;
        byteDeviceReq[15] = (byte) 0x30;
        byteDeviceReq[16] = (byte) 0x30;
        byteDeviceReq[17] = (byte) 0x31;
        byteDeviceReq[18] = (byte) 0x41;
        byteDeviceReq[19] = (byte) 0x32;
        // 00 02 0D 0D
        byteDeviceReq[20] = (byte) 0x00;
        byteDeviceReq[21] = (byte) 0x02;
        byteDeviceReq[22] = (byte) 0x0D;
        byteDeviceReq[23] = (byte) 0x0D;
        // 01 02 01 0B
        byteDeviceReq[24] = (byte) 0x01;
        byteDeviceReq[25] = (byte) 0x02;
        byteDeviceReq[26] = (byte) 0x01;
        byteDeviceReq[27] = (byte) 0x0B;
        //02 01 00
        byteDeviceReq[28] = (byte) 0x02;
        byteDeviceReq[29] = (byte) 0x01;
        byteDeviceReq[30] = (byte) 0x00;
        //03 01 00
        byteDeviceReq[31] = (byte) 0x03;
        byteDeviceReq[32] = (byte) 0x01;
        byteDeviceReq[33] = (byte) 0x00;
        //04 01 00
        byteDeviceReq[34] = (byte) 0x04;
        byteDeviceReq[35] = (byte) 0x01;
        byteDeviceReq[36] = (byte) 0x00;
        // 07 01 1F
        byteDeviceReq[37] = (byte) 0x07;
        byteDeviceReq[38] = (byte) 0x01;
        byteDeviceReq[39] = (byte) 0x1F;
        //08 02 00 00
        byteDeviceReq[40] = (byte) 0x08;
        byteDeviceReq[41] = (byte) 0x02;
        byteDeviceReq[42] = (byte) 0x00;
        byteDeviceReq[43] = (byte) 0x00;
        //09 02 00 01
        byteDeviceReq[44] = (byte) 0x09;
        byteDeviceReq[45] = (byte) 0x02;
        byteDeviceReq[46] = (byte) 0x00;
        byteDeviceReq[47] = (byte) 0x01;
        //57
        byteDeviceReq[48] = (byte) 0x57;

        return byteDeviceReq;
    }*/
  /*  private static byte[] initDeviceReqByte() {
        // 本例入参：
        //00003e01
        byte[] byteDeviceReq = new byte[73];
        byteDeviceReq[0] = (byte) 0x00;
        byteDeviceReq[1] = (byte) 0x00;
        byteDeviceReq[2] = (byte) 0x3E;
        byteDeviceReq[3] = (byte) 0x01;
        // 30303030303030373032303030304132
        byteDeviceReq[4] = (byte) 0x30;
        byteDeviceReq[5] = (byte) 0x30;
        byteDeviceReq[6] = (byte) 0x30;
        byteDeviceReq[7] = (byte) 0x30;
        byteDeviceReq[8] = (byte) 0x30;
        byteDeviceReq[9] = (byte) 0x30;
        byteDeviceReq[10] = (byte) 0x30;
        byteDeviceReq[11] = (byte) 0x37;
        byteDeviceReq[12] = (byte) 0x30;
        byteDeviceReq[13] = (byte) 0x32;
        byteDeviceReq[14] = (byte) 0x30;
        byteDeviceReq[15] = (byte) 0x30;
        byteDeviceReq[16] = (byte) 0x30;
        byteDeviceReq[17] = (byte) 0x30;
        byteDeviceReq[18] = (byte) 0x41;
        byteDeviceReq[19] = (byte) 0x32;
        // 000100
        byteDeviceReq[20] = (byte) 0x00;
        byteDeviceReq[21] = (byte) 0x01;
        byteDeviceReq[22] = (byte) 0x00;
        // 010100
        byteDeviceReq[23] = (byte) 0x01;
        byteDeviceReq[24] = (byte) 0x01;
        byteDeviceReq[25] = (byte) 0x00;
        // 0202FEeb
        byteDeviceReq[26] = (byte) 0x02;
        byteDeviceReq[27] = (byte) 0x02;
        byteDeviceReq[28] = (byte) 0xFE;
        byteDeviceReq[29] = (byte) 0xEB;
        // 030100
        byteDeviceReq[30] = (byte) 0x03;
        byteDeviceReq[31] = (byte) 0x01;
        byteDeviceReq[32] = (byte) 0x00;
        // 040100
        byteDeviceReq[33] = (byte) 0x04;
        byteDeviceReq[34] = (byte) 0x01;
        byteDeviceReq[35] = (byte) 0x00;
        // c5 14 38 39 38 36 30 33 31 37 34 35 32 30 34 33 32 37 33 38 31 34
        byteDeviceReq[36] = (byte) 0xC5;
        byteDeviceReq[37] = (byte) 0x14;
        byteDeviceReq[38] = (byte) 0x38;
        byteDeviceReq[39] = (byte) 0x39;
        byteDeviceReq[40] = (byte) 0x38;
        byteDeviceReq[41] = (byte) 0x36;
        byteDeviceReq[42] = (byte) 0x30;
        byteDeviceReq[43] = (byte) 0x33;
        byteDeviceReq[44] = (byte) 0x31;
        byteDeviceReq[45] = (byte) 0x37;
        byteDeviceReq[46] = (byte) 0x34;
        byteDeviceReq[47] = (byte) 0x35;
        byteDeviceReq[48] = (byte) 0x32;
        byteDeviceReq[49] = (byte) 0x30;
        byteDeviceReq[50] = (byte) 0x34;
        byteDeviceReq[51] = (byte) 0x33;
        byteDeviceReq[52] = (byte) 0x32;
        byteDeviceReq[53] = (byte) 0x37;
        byteDeviceReq[54] = (byte) 0x33;
        byteDeviceReq[55] = (byte) 0x38;
        byteDeviceReq[56] = (byte) 0x31;
        byteDeviceReq[57] = (byte) 0x34;
        // 060119
        byteDeviceReq[58] = (byte) 0x06;
        byteDeviceReq[59] = (byte) 0x01;
        byteDeviceReq[60] = (byte) 0x19;
        // 070104
        byteDeviceReq[61] = (byte) 0x07;
        byteDeviceReq[62] = (byte) 0x01;
        byteDeviceReq[63] = (byte) 0x04;
        //0802000f
        byteDeviceReq[64] = (byte) 0x08;
        byteDeviceReq[65] = (byte) 0x02;
        byteDeviceReq[66] = (byte) 0x00;
        byteDeviceReq[67] = (byte) 0x0F;
        // 0902000f
        byteDeviceReq[68] = (byte) 0x09;
        byteDeviceReq[69] = (byte) 0x02;
        byteDeviceReq[70] = (byte) 0x00;
        byteDeviceReq[71] = (byte) 0x0F;
        // DB
        byteDeviceReq[72] = (byte) 0xDB;

        return byteDeviceReq;
    }*/
    /**
     * 初始化：设备数据上报码流
     * @return
     */
    private static byte[] initDeviceReqByte() {
        // 本例入参：
        //01 0020 01
        byte[] byteDeviceReq = new byte[43];
        byteDeviceReq[0] = (byte) 0x01;
        byteDeviceReq[1] = (byte) 0x00;
        byteDeviceReq[2] = (byte) 0x20;
        byteDeviceReq[3] = (byte) 0x01;
        // 30 30 30 30 30 30 30 37 30 32 30 30 30 30 41 32
        byteDeviceReq[4] = (byte) 0x30;
        byteDeviceReq[5] = (byte) 0x30;
        byteDeviceReq[6] = (byte) 0x30;
        byteDeviceReq[7] = (byte) 0x30;
        byteDeviceReq[8] = (byte) 0x30;
        byteDeviceReq[9] = (byte) 0x30;
        byteDeviceReq[10] = (byte) 0x30;
        byteDeviceReq[11] = (byte) 0x37;
        byteDeviceReq[12] = (byte) 0x30;
        byteDeviceReq[13] = (byte) 0x32;
        byteDeviceReq[14] = (byte) 0x30;
        byteDeviceReq[15] = (byte) 0x30;
        byteDeviceReq[16] = (byte) 0x30;
        byteDeviceReq[17] = (byte) 0x30;
        byteDeviceReq[18] = (byte) 0x31;
        byteDeviceReq[19] = (byte) 0x32;
        //C5 14 38 39 38 36 30 33 31 37 34 35 32 30 34 33 32 37 33 38 31 34
        byteDeviceReq[20] = (byte) 0xC5;
        byteDeviceReq[21] = (byte) 0x14;
        byteDeviceReq[22] = (byte) 0x38;
        byteDeviceReq[23] = (byte) 0x39;
        byteDeviceReq[24] = (byte) 0x38;
        byteDeviceReq[25] = (byte) 0x36;
        byteDeviceReq[26] = (byte) 0x30;
        byteDeviceReq[27] = (byte) 0x33;
        byteDeviceReq[28] = (byte) 0x31;
        byteDeviceReq[29] = (byte) 0x37;
        byteDeviceReq[30] = (byte) 0x34;
        byteDeviceReq[31] = (byte) 0x35;
        byteDeviceReq[32] = (byte) 0x32;
        byteDeviceReq[33] = (byte) 0x30;
        byteDeviceReq[34] = (byte) 0x34;
        byteDeviceReq[35] = (byte) 0x33;
        byteDeviceReq[36] = (byte) 0x32;
        byteDeviceReq[37] = (byte) 0x37;
        byteDeviceReq[38] = (byte) 0x33;
        byteDeviceReq[39] = (byte) 0x38;
        byteDeviceReq[40] = (byte) 0x31;
        byteDeviceReq[41] = (byte) 0x34;
        // a3
        byteDeviceReq[42] = (byte) 0xa3;

        return byteDeviceReq;
    }

    /**
     * 初始化：平台向设备命令下发数据
     * @return
     */
    private static ObjectNode initCloudReqObjectNode() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudReqObjectNode = mapper.createObjectNode();
        ObjectNode paras = mapper.createObjectNode();
        paras.put("value",jsonObject());
        cloudReqObjectNode.put("identifier", "123");
        cloudReqObjectNode.put("msgType", "cloudReq");
        cloudReqObjectNode.put("serviceId", "General");
        cloudReqObjectNode.put("cmd", "SetData");
        cloudReqObjectNode.put("paras", paras);
        cloudReqObjectNode.put("hasMore", 0);
        cloudReqObjectNode.put("mid", 65535);
        return cloudReqObjectNode;


    }

    public static JsonNode jsonObject() throws Exception{
        String str = "{\"ver\":\"1.0\",\"name\":\"data\",\"type\":\"ntf\",\"data\":[{\"ndid\":\"00000007020000A2\"," +
                "\"time\":\"20190412104007\",\"channel\":[{\"chno\":0,\"vt\":\"int\",\"value\":0},{\"chno\":1," +
                "\"vt\":\"int\",\"value\":0},{\"chno\":2,\"vt\":\"int\",\"value\":235},{\"chno\":3,\"vt\":\"int\"," +
                "\"value\":0},{\"chno\":4,\"vt\":\"int\",\"value\":0},{\"chno\":5,\"vt\":\"str\"," +
                "\"value\":\"89860317452043273814\"},{\"chno\":6,\"vt\":\"int\",\"value\":25},{\"chno\":7," +
                "\"vt\":\"int\",\"value\":4},{\"chno\":8,\"vt\":\"int\",\"value\":15},{\"chno\":9,\"vt\":\"int\"," +
                "\"value\":15}]}]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(str);
        return  actualObj;
    }

    @Test
    public void node() throws  Exception{
        String str = "{\"msgType\":\"cloudReq\",\"serviceId\":\"General\",\"cmd\":\"SetData\"," +
                "\"paras\":{\"ver\":\"1.0\",\"name\":\"data\",\"type\":\"ntf\"," +
                "\"data\":[{\"ndid\":\"0000000203000000\",\"time\":\"20190419152217\",\"channel\":[{\"chno\":0," +
                "\"vt\":\"int\",\"value\":3624},{\"chno\":1,\"vt\":\"int\",\"value\":309},{\"chno\":2,\"vt\":\"int\"," +
                "\"value\":0},{\"chno\":3,\"vt\":\"int\",\"value\":0},{\"chno\":4,\"vt\":\"int\",\"value\":0}," +
                "{\"chno\":7,\"vt\":\"int\",\"value\":26},{\"chno\":8,\"vt\":\"int\",\"value\":15},{\"chno\":9," +
                "\"vt\":\"int\",\"value\":161}]}]},\"hasMore\":0,\"mid\":154}";
        ObjectNode node = (ObjectNode) new ObjectMapper().readTree(str);
        byte[] outputByte = protocolAdapter.encode(node);
        System.out.println("\ncloudReq output:" + parseByte2HexStr(outputByte));
    }

    /**
     * 初始化：设备对平台的响应码流
     * @return
     */
    private static byte[] initDeviceRspByte() {
        /*
         * 测试用例：有命令短mid 设备应答消息:AA7201000107E0
         */
        byte[] byteDeviceRsp = new byte[12];
        byteDeviceRsp[0] = (byte) 0xAA;
        byteDeviceRsp[1] = (byte) 0x72;
        byteDeviceRsp[2] = (byte) 0x01;
        byteDeviceRsp[3] = (byte) 0x00;
        byteDeviceRsp[4] = (byte) 0x01;
        byteDeviceRsp[5] = (byte) 0x07;
        byteDeviceRsp[6] = (byte) 0xE0;
        return byteDeviceRsp;
    }



    /**
     * 初始化：平台对设备的应答数据
     * @param device2CloudByte
     * @return
     */
    private static ObjectNode initCloudRspObjectNode(byte[] device2CloudByte) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudRspObjectNode = mapper.createObjectNode();
        cloudRspObjectNode.put("identifier", "123");
        cloudRspObjectNode.put("msgType", "cloudRsp");
        // 设备上报的码流
        cloudRspObjectNode.put("request", device2CloudByte);
        cloudRspObjectNode.put("errcode", 1);
        cloudRspObjectNode.put("hasMore", 0);
        return cloudRspObjectNode;
    }
}
