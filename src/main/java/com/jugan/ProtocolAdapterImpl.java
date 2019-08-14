package com.jugan;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;
import com.jugan.tools.Utilty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @Author CL
 * @Date 2019/4/15-17:05
 */
public class ProtocolAdapterImpl implements IProtocolAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolAdapterImpl.class);
    // 厂商名称
    private static final String MANU_FACTURERID = "ZJJG";
    // 设备型号
    private static final String MODEL = "Common";

    @Override
    public String getManufacturerId() {
        return MANU_FACTURERID;
    }

    @Override
    public String getModel() {
        return MODEL;
    }

    public void activate() {
        logger.info("Codec demo HttpMessageHander activated.");
    }

    public void deactivate() {
        logger.info("Codec demo HttpMessageHander deactivated.");
    }

    @Override
    public byte[] encode(ObjectNode input) {

        logger.info("dynamic lrbtest " + input.toString());
        try {

            CmdProcess demo = new CmdProcess(input);
            return demo.toByte();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ObjectNode decode(byte[] binaryData) {

        try {

            ReportProcess lightProcess = new ReportProcess(binaryData);
            ObjectNode objectNode = lightProcess.toJsonNode();
            return objectNode;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
