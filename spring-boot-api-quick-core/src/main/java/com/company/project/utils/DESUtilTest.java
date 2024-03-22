package com.company.project.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Base64;

@Component
public class DESUtilTest {


    private static final Logger logger = LoggerFactory.getLogger(DESUtil.class);

    private static DesConfig desConfig;

    public static void main(String[] args) {
        String keyNo = "XX";
        desConfig = new DesConfig();
        desConfig.setUsername("XX");
        desConfig.setSepChar("&");
        desConfig.setSecretKey("XX");
        desConfig.setDesKey("XX");
        System.out.println(genBase64Creds(keyNo));
    }


    public static String genBase64Creds(String keyNo) {
        logger.info("Username:" + desConfig.getUsername());
        String authStr = desConfig.getUsername() + ":" + localGenDownloadKey(keyNo);
        return Base64.getEncoder().encodeToString(authStr.getBytes());
    }

    /***
     * 根据keyNo 获取key
     * @param keyNo
     * @return
     */
    public static String localGenDownloadKey(String keyNo) {
        String key;
        StringBuilder keyNoSB = new StringBuilder();
        keyNoSB.append(keyNo);
        keyNoSB.reverse();
        //反向keyNo + & + secretKey(ABCDEFGH)
        key = keyNoSB + desConfig.getSepChar() + desConfig.getSecretKey();
        key = Base64.getEncoder().encodeToString(key.getBytes());
        byte[] desByte = encrypt(key, Base64.getDecoder().decode(desConfig.getDesKey()));
        key = Base64.getEncoder().encodeToString(desByte);
        return key;
    }

    public static byte[] encrypt(String content, byte[] byteSymKey) {
        try {
            DESKeySpec desKey = new DESKeySpec(byteSymKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            return cipher.doFinal(content.getBytes());
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /***
     * 根据证件号 和 证件类型 获取key
     * @param idType
     * @param idCardNo
     * @return
     */
    public static String localGenKey(Integer idType, String idCardNo) {
        String key = "";
        key = idCardNo + desConfig.getSepChar() + idType.toString() + desConfig.getSepChar() + desConfig.getSecretKey();
        key = Base64.getEncoder().encodeToString(key.getBytes());
        byte[] desByte = encrypt(key, Base64.getDecoder().decode(desConfig.getDesKey()));
        key = Base64.getEncoder().encodeToString(desByte);
        return key;
    }


    public static String localGenTypeNoKey(Integer idType, String idCardNo) {
        logger.info("Username:" + desConfig.getUsername());
        String authStr = desConfig.getUsername() + ":" + localGenKey(idType, idCardNo);
        return Base64.getEncoder().encodeToString(authStr.getBytes());
    }
}
