package com.company.project.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Base64;

public class ProDemo {


    private static String encrptUsername = "safp";

    private static String sepCharForDL = "&";

    private static String secretKeyForDL = "YUr73ruC";

    private static String desKeyForDL = "qnmZoqW8hnk=";


    public static void main(String[] args) {
        //byte[] desKeyForDLStr = {(byte) 0xAA, (byte) 0x79, (byte) 0x99, (byte) 0xA2, (byte) 0xA5, (byte) 0xBC, (byte) 0x86, (byte) 0x79};
        // byte[] desKeyForDLStr = {(byte) 0xAA, 0x79, (byte) 0x99, (byte) 0xA2, (byte) 0xA5, (byte) 0xBC, (byte) 0x86, 0x79};
        // System.out.println(Base64.getEncoder().encodeToString(desKeyForDLStr));
        getMCInfoByKeyNo("0126484503");//curl -X GET "https://www.ssm.informac.gov.mo/ESICKAPI/api/MedicalCertificate/GetByKeyNo" -H "accept: */*" -H "Authorization: Basic c2FmcDpkNlI5VnhLUzlGaXRzVEVhUlFaQ3dhTnRXdlpUMHRvMUdyVFhkUDlwVnhJPQ=="
    }

    byte[] desKeyForDLStr = {(byte) 0xAA, 0x79, (byte) 0x99, (byte) 0xA2, (byte) 0xA5, (byte) 0xBC, (byte) 0x86, 0x79};


    public void getMCInfo(Integer idType, String idCardNo) {
        String authStr = encrptUsername + ":" + localGenKey(idType, idCardNo);
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
    }


    public String localGenKey(Integer idType, String idCardNo) {
        String key = "";
        key = idCardNo + sepCharForDL + idType.toString() + sepCharForDL + secretKeyForDL;
        System.out.println("key : " + key);
        key = Base64.getEncoder().encodeToString(key.getBytes());
        System.out.println("Base64 key : " + key);
        byte[] desByte = encrypt(key, Base64.getDecoder().decode(desKeyForDL));
        key = Base64.getEncoder().encodeToString(desByte);
        System.out.println("encodeToString : " + key);
        return key;
    }


    public static void getMCInfoByKeyNo(String keyNo) {
        String authStr = encrptUsername + ":" + localGenDownloadKey(keyNo);
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        System.out.println("base64Creds:" + base64Creds);
    }

    public static String localGenDownloadKey(String keyNo) {
        String key = "";
        StringBuilder keyNoSB = new StringBuilder();
        keyNoSB.append(keyNo);
        keyNoSB = keyNoSB.reverse();
        key = keyNoSB + sepCharForDL + secretKeyForDL;
        key = Base64.getEncoder().encodeToString(key.getBytes());
        //byte[] desByte = encrypt(key,SSM_DL_BYTESMYKEY);
        byte[] desByte = encrypt(key, Base64.getDecoder().decode(desKeyForDL));
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
            byte[] result = cipher.doFinal(content.getBytes());
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
