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
        System.out.println("\ncloudReq output:" + Utilty.parseByte2HexStr(outputByte));
    }

    /**
     * 测试用例3：设备对平台命令的应答消息 有命令短id
     * 设备应答消息:AA7201000107E0
     * @throws Exception
     */
    @Test
    public void testDecodeDeviceResponseIoT() throws Exception {
        byte[] deviceRspByte = initDeviceRspByte();//initDeviceReqByte();//
       // System.out.println(Utilty.parseByte2HexStr(aaa()));
        ObjectNode objectNode = protocolAdapter.decode(deviceRspByte);
        String str = (objectNode == null) ? null:objectNode.toString();
        System.out.println("平台设备响应:" + str);
    }
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
        System.out.println("cloudRsp output:" + Utilty.parseByte2HexStr(outputByte2));
    }



    /**
     * 初始化：设备数据上报码流
     * @return
     */
    private static byte[] initDeviceReqByte() {
        // 本例入参：
        //01 0020 01
        byte[] byteDeviceReq = {(byte)0x01,(byte)0x00,(byte)0x20,(byte)0x01,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x37,(byte)0x30,(byte)0x32,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x41,(byte)0x31,(byte)0xC5,(byte)0x14,(byte)0x38,(byte)0x39,(byte)0x38,(byte)0x36,(byte)0x31,(byte)0x31,(byte)0x31,(byte)0x38,(byte)0x32,(byte)0x32,(byte)0x31,(byte)0x30,(byte)0x30,(byte)0x38,(byte)0x36,(byte)0x39,(byte)0x30,(byte)0x39,(byte)0x33,(byte)0x35,(byte)0x32};

        return byteDeviceReq;
    }


    /**
     * 初始化：设备对平台的响应码流
     * @return
     */
    private static byte[] initDeviceRspByte() {
        /*
         * 测试用例：有命令短mid 设备应答消息:AA7201000107E0
         */
        byte[] buf = {(byte)0x00,(byte)0x00,(byte)0x36,(byte)0x02,(byte)0x00,(byte)0x9A,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x38,(byte)0x30,(byte)0x31,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x00,(byte)0x01};
        return buf;
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
