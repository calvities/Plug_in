package com.jugan.tools;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据转换工具类
 */
public class Utilty {

    private static Utilty instance = new Utilty();

    public static Utilty getInstance() {
        return instance;
    }

    public static final int MIN_MID_VALUE = 1;

    public static final int MAX_MID_VALUE = 65535;


    /**
     * byte 转 int (????????????????)
     *
     * @param b
     * @param start
     * @param length
     * @return
     */
    public int bytes2Int(byte[] b, int start, int length) {
        int sum = 0;
        int end = start + length;
        for (int k = start; k < end; k++) {
            int n = ((int) b[k]) & 0xff;
            n <<= (--length) * 8;
            sum += n;
        }
        return sum;
    }


    /**
     * int 转 byte
     *
     * @param value  数值
     * @param length 转换成byte数组的长度
     * @return
     */
    public byte[] int2Bytes(int value, int length) {
        byte[] b = new byte[length];
        for (int k = 0; k < length; k++) {
            b[length - k - 1] = (byte) ((value >> 8 * k) & 0xff);
        }
        return b;
    }


    /**
     * 参数值是否在最大值和最小值之间
     *
     * @param mId
     * @return
     */
    public boolean isValidofMid(int mId) {
        if (mId < MIN_MID_VALUE || mId > MAX_MID_VALUE) {
            return false;
        }
        return true;
    }

    /**
     * 数值转正负数
     *
     * @param num 数值
     * @return 值
     */
    public static long num2Hex(long num, int len) {
        if (((num >> (len * 8 - 1)) & 0x01) == 0x01) {
            return (num - (0x01 << (len * 8)));
        }
        return num;
    }

    /**
     * byte[]转int
     *
     * @param bRefArr
     * @return
     */
    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;
        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }


    /**
     * int转16进制
     *
     * @param number
     * @return
     */
    public static String parseByte2HexStr(int number) {
        StringBuffer sb = new StringBuffer();
        String hex = Integer.toHexString(number & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        sb.append(hex.toUpperCase());
        return sb.toString();

    }

    /**
     * Long转16进制
     *
     * @param number
     * @return
     */
    public static String parseByte2HexStr(long number) {
        StringBuffer sb = new StringBuffer();
        String hex = Long.toHexString(number & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        sb.append(hex.toUpperCase());
        return sb.toString();

    }

    /**
     * byte[]转16进制
     *
     * @param buf
     * @return String
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


    /**
     * 16进制转ASCII
     *
     * @param hex 16进制的字符串
     * @return 转换后的字符串
     */
    public static String hex2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        //49204c6f7665204a617661 分成两个字符 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {
            //成对抓住十六进制
            String output = hex.substring(i, (i + 2));
            //将十六进制转换为十进制
            int decimal = Integer.parseInt(output, 16);
            //将十进制转换为字符
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    /**
     * byteASCII直接取String值
     *
     * @param buf byte[]
     * @return
     */
    public static String byteAsciiToString(byte[] buf) {
        char[] chars = new char[buf.length];
        for (int i = 0; i < chars.length; i++) {
            //由byte转char
            chars[i] = (char) buf[i];
        }
        return String.valueOf(chars);
    }

    /**
     * 获取ASCII码的数值
     *
     * @param str
     * @return
     */
    public static String toAscii(String str) {
        //转换成char[]
        char[] chars = str.toCharArray();
        byte[] buf = new byte[chars.length];
        for (int i = 0; i < buf.length; i++) {
            //由char转byte
            buf[i] = (byte) chars[i];
        }
        String ss = Utilty.parseByte2HexStr(buf);
        //System.out.println(ss);
        return ss;
    }


    /**
     * float转换成byte[]
     *
     * @param f
     * @return
     */
    public static byte[] float2byte(float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {

            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }


    /**
     * 获取系统当前时间
     *
     * @return string类型的时间
     */
    public static String obtainByTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }


    /**
     * 转二进制不满8位补零
     *
     * @param num 数值
     * @return String
     */
    public static String toBinaryString(int num) {
        //转二进制
        String str = Integer.toBinaryString(num);
        //转char数组
        char[] chars = str.toCharArray();
        if (chars.length != 8) {
            for (int i = 0; i < 8 - chars.length; i++) {
                //不满8位时补零
                str = '0' + str;
            }
        }
        return str;
    }

    /**
     * 转二进制不满8位补零
     *
     * @param num 数值
     * @return String
     */
    public static String toBinaryString(long num) {
        //转二进制
        String str = Long.toBinaryString(num);
        //转char数组
        char[] chars = str.toCharArray();
        if (chars.length != 8) {
            for (int i = 0; i < 8 - chars.length; i++) {
                //不满8位时补零
                str = '0' + str;
            }
        }
        return str;
    }


    /**
     * 获得通道号
     *
     * @param head   通道号
     * @param typeOf 类型
     * @return
     */
    public static int generatorHead(long head, String typeOf) {
        //取得二进制数
        String str = Utilty.toBinaryString(head);
        //除去前两个bit位
        String numStr = str.substring(2);
        StringBuilder sb = new StringBuilder();
        switch (typeOf) {
            case "int":
                sb.append("00");
                break;
            case "float":
                sb.append("01");
                break;
            case "octet":
                sb.append("10");
                break;
            case "str":
                sb.append("11");
                break;
        }
        sb.append(numStr);
        //转成10进制
        int num = Integer.parseInt(sb.toString(), 2);
        return num;
    }

    /**
     * 合并数组
     *
     * @param firstArray  第一个数组
     * @param secondArray 第二个数组
     * @return 合并后的数组
     */
    public static byte[] concat(byte[] firstArray, byte[] secondArray) {
        if (firstArray == null || secondArray == null) {
            return null;
        }
        byte[] bytes = new byte[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length, secondArray.length);

       /* System.out.println("合并后的数组:" + bytes.toString());
        System.out.println("合并之前数组长度:" + secondArray.length + "\t合并之后数组长度:" + bytes.length);*/
        return bytes;
    }


    /**
     * byte 转 float
     *
     * @param b byte数组
     * @return
     */
    public static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }


    /**
     * 字节转换为浮点
     *
     * @param b     字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float byte2float(byte[] b, int index) {
        int i;
        i = b[index + 0];
        i &= 0xff;
        i |= ((long) b[index + 1] << 8);
        i &= 0xffff;
        i |= ((long) b[index + 2] << 16);
        i &= 0xffffff;
        i |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(i);
    }

    /**
     * 取出该数组实际的数据部分
     * 此方法不可用
     *
     * @param data
     * @return
     */
    public static byte[] returnActualLength(byte[] data) {
        int i = 0;
        for (; i < data.length; i++) {
            if (data[i] == '\0') {
                break;
            }
        }
        byte[] bytes = new byte[i];
        for (int j = 0; j < bytes.length; j++) {
            bytes[j] = data[j];
        }
        return bytes;
    }


    /**
     * 字符串转byte[]
     * (此项目适用)
     *
     * @param str
     * @return
     */
    public static byte[] strToByte(String str) {
        byte[] bytes = new byte[str.length() / 2];
        int j = 0;
        for (int i = 0; i < bytes.length; i++) {
            int num = Integer.parseInt(str.substring(j, j + 2), 16);
            bytes[i] = (byte) num;
            j = j + 2;
        }
        return bytes;
    }


}
