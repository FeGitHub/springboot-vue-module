package com.company.project.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/***
 * ECC橢圓曲線加密演算法加密
 */
public class ECCUtil {

    private static final String EC_PROVIDER = "BC";

    private static final String EC_ALGORITHM = "EC";

    private static final String ECIES_ALGORITHM = "ECIES";

    private static final String SIGNATURE = "SHA256withECDSA";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws Exception {
        // 測試文本
        byte[] plain = "11223344|440112345|3|20231017|900|20231019|20231017".getBytes();
        // 生成秘鑰對
        //final KeyPair keyPair = generateEccKeyPair();
        // PublicKey publicKey = keyPair.getPublic();
        // PrivateKey privateKey = keyPair.getPrivate();
        //  String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
        //  String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
        // System.out.println("公鑰：" + publicKeyString);
        // System.out.println("私鑰：" + privateKeyString);
        //==========
        PublicKey publicKey = getPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEpWUK8fn6TtPb02MiVBWtoEbqfgNitjoaHvnD1fVRQOoJ3ZSfagE9a92D4xRv78/qzAlC8cyjrP4efaKHyXK1Iw==");
        PrivateKey privateKey = getPrivateKey("MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgO8W8taCO4M/EQRrJxopcnMgWQd0Y1KcDmTVvC9hrANuhRANCAASlZQrx+fpO09vTYyJUFa2gRup+A2K2Ohoe+cPV9VFA6gndlJ9qAT1r3YPjFG/vz+rMCULxzKOs/h59oofJcrUj");
        //======
        // 簽名驗證
        final byte[] sign = eccSign(privateKey, plain);
        System.out.println("簽名驗證：" + Base64.encodeBase64String(sign));
        final boolean verify = eccVerify(publicKey, plain, sign);
        System.out.println(verify);
        // 加解密
        final byte[] encrypt = eccEncrypt(publicKey, plain);
        final byte[] decrypt = eccDecrypt(privateKey, encrypt);
        System.out.println(new String(decrypt).equals(new String(plain)));

    }


    /**
     * 生成密钥对.
     *
     * @return KeyPair
     */
    public static KeyPair generateEccKeyPair() throws Exception {
        // 选择椭圆曲线参数
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("prime256v1");
        // 获取指定算法的密钥对生成器
        final KeyPairGenerator generator = KeyPairGenerator.getInstance(EC_ALGORITHM, EC_PROVIDER);
        // 初始化密钥对生成器
        generator.initialize(ecSpec, new SecureRandom());
        // 随机生成一对密钥（包含公钥和私钥）
        return generator.generateKeyPair();
    }


    /**
     * 私钥签名.
     *
     * @param privateKey 私钥
     * @param plain      原文
     * @return byte[] 签名
     */
    public static byte[] eccSign(PrivateKey privateKey, byte[] plain) throws Exception {
        final Signature signature = Signature.getInstance(SIGNATURE);
        signature.initSign(privateKey);
        signature.update(plain);
        return signature.sign();
    }

    /**
     * 公钥验签.
     *
     * @param publicKey 公钥
     * @param plain     原文
     * @param sign      签名
     * @return boolean 状态
     */
    public static boolean eccVerify(PublicKey publicKey, byte[] plain, byte[] sign) {
        try {
            final Signature signature = Signature.getInstance(SIGNATURE);
            signature.initVerify(publicKey);
            signature.update(plain);
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ECC 加密.
     *
     * @param publicKey 公钥
     * @param plain     原文
     * @return byte[] 密文
     */
    public static byte[] eccEncrypt(PublicKey publicKey, byte[] plain) throws Exception {
        final Cipher cipher = Cipher.getInstance(ECIES_ALGORITHM, EC_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plain);
    }

    /**
     * ECC 解密.
     *
     * @param privateKey 　私钥
     * @param encrypted  　密文
     * @return byte[] 原文
     */
    public static byte[] eccDecrypt(PrivateKey privateKey, byte[] encrypted) throws Exception {
        final Cipher cipher = Cipher.getInstance(ECIES_ALGORITHM, EC_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);
    }

    /***
     * 根據字符串獲取對應的公鑰
     * @param keyStr
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String keyStr) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EC_ALGORITHM, EC_PROVIDER);
            byte[] publicKeyBytes = Base64.decodeBase64(keyStr);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /****
     * 根據對應的字符串獲取對應的私鑰
     * @param keyStr
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String keyStr) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EC_ALGORITHM, EC_PROVIDER);
            byte[] privateKeyBytes = Base64.decodeBase64(keyStr);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
