package com.yyp.socket.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by yyp on 2016/12/7.
 */
public class NetDataTransformUtils {

    /**
     * int 转为 ByteArray
     * 将int转为低字节在前，高字节在后的byte数组
     * @param n
     * @return
     */
    public static byte[] intToByteArray(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * ByteArray 转为 int
     * @param bArr
     * @return
     */
    public static int byteArrayToInt(byte[] bArr) {
        int n = 0;
        for(int i=0;i<bArr.length&&i<4;i++){
            int left = i*8;
            n+= (bArr[i] << left);
        }
        return n;
    }

    /**
     * ByteArray 转为 String
     * @param valArr
     * @param maxLen
     * @return
     */
    public static String byteArrayToString(byte[] valArr,int maxLen) {
        String result=null;
        int index = 0;
        while(index < valArr.length && index < maxLen) {
            if(valArr[index] == 0) {
                break;
            }
            index++;
        }
        byte[] temp = new byte[index];
        System.arraycopy(valArr, 0, temp, 0, index);
        try {
            result= new String(temp,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * String 转为 ByteArray
     * @param str
     * @return
     */
    public static byte[] stringToByteArray(String str){
        byte[] temp = null;
        try {
            temp = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
