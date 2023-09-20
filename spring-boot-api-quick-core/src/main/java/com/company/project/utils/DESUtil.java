package com.company.project.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Base64;

@Component
public class DESUtil {


    private static String encrptUsername = "safp";

    private static String sepCharForDL = "&";

    private static String secretKeyForDL = "ABCDEFGH";

    private static String desKeyForDL = "ESIzRFVmd4g=";


    public static void main(String[] args) {
        String keyNo = "2023072813";
        genBase64Creds(keyNo);
    }

    public static String genBase64Creds(String keyNo) {
        String authStr = encrptUsername + ":" + localGenDownloadKey(keyNo);
        System.out.println("authStr===>" + authStr);
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        System.out.println("base64Creds===>" + base64Creds);
        return base64Creds;
    }

    public static String localGenDownloadKey(String keyNo) {
        String key = "";
        StringBuilder keyNoSB = new StringBuilder();
        keyNoSB.append(keyNo);
        System.out.println("keyNoSB.append(keyNo)===>" + keyNoSB);
        keyNoSB = keyNoSB.reverse();
        System.out.println("keyNoSB.reverse()===>" + keyNoSB);
        key = keyNoSB + sepCharForDL + secretKeyForDL;
        System.out.println("keyNoSB + sepCharForDL + secretKeyForDL===>" + key);
        key = Base64.getEncoder().encodeToString(key.getBytes());
        System.out.println("key===>" + key);
        byte[] desByte = encrypt(key, Base64.getDecoder().decode(desKeyForDL));
        key = Base64.getEncoder().encodeToString(desByte);
        System.out.println("Base64.getEncoder().encodeToString===>" + key);
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
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String content, byte[] byteSymKey) {
        try {
            DESKeySpec desKey = new DESKeySpec(byteSymKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            return cipher.doFinal(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
