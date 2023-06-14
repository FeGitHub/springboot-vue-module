package com.company.project.utils;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;


public class DESEncoderUtils {
    /**
     * @Description： 对参数进行DES和Base64加密
     */
    public static String encryptParam(String sourceParam, String key) throws Exception {
        byte[] byteContent = sourceParam.getBytes("UTF-8");
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        //密钥
        SecretKey securekey = keyFactory.generateSecret(desKey);
        //向量
        AlgorithmParameterSpec iv = new IvParameterSpec(key.getBytes());
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
        return Base64.getEncoder().encodeToString(cipher.doFinal(byteContent));
    }

    //对base64或16进制以及DES加密后数据进行解密
    public static String decryptParam(String sourceParam, String key) throws Exception {
        byte[] byteContent = Base64.getDecoder().decode(sourceParam);
        //向量
        AlgorithmParameterSpec iv = new IvParameterSpec(key.getBytes());
        //密钥
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
        return new String(cipher.doFinal(byteContent), "UTF-8");
    }

    /**
     * byte[]转换为16进制字符串
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     * @author cyl
     * @date 2018年8月23日 上午9:07:40
     */
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    /**
     * 16进制字符串转成byte数组
     *
     * @param hexString 字符串
     * @return 转后的字符串
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        int hexLength = length;
        while (hexLength % 8 != 0) {
            hexLength++;
        }
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[hexLength];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * @param c 字符
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 测试函数
     *
     * @param args 参数
     * @date 2015年11月30日 下午3:02:07
     */
    public static void main(String[] args) throws Exception {
        String key = "IJKLMNOP";
        String originStr = "18250237&5&ABCDEFGH";
        String base64Str = Base64.getEncoder().encodeToString(originStr.getBytes());
        System.out.println("Base64后的字符串 = " + base64Str);
        // des 加密
        String encryptStr = encryptParam(base64Str, key);
        System.out.println("加密后的字符串 = " + encryptStr);
        //  des 解密
        String decryptStr = decryptParam(encryptStr, key);
        System.out.println("解密后的字符串 = " + decryptStr);

        System.out.println("解密后的字符串反向base64 = " + new String(Base64.getDecoder().decode(decryptStr), "UTF-8"));
    }


}
