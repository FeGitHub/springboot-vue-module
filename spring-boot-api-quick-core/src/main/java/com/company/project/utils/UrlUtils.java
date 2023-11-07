package com.company.project.utils;

import org.apache.commons.codec.binary.Base64;

import java.net.URL;
import java.net.URLDecoder;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {


    public static void main(String[] args) throws Exception {
        String url = "https://uat-ga.safp.gov.mo/?key=1&data=11223344%7C440112345%7C3%7C20231017%7C900%7C20231019%7C20231017&sign=MEUCIHwxicOIbcKWTICL+ScBchCEHVsj8U1lyUL9awPdXdboAiEAuXBEZ/yzQiC3Yg9sHfnRtSaPIDedzICmnP3jU+3BM3s=";
        Map<String, String> result = parseURLParams(url);
        PublicKey publicKey = ECCUtil.getPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEpWUK8fn6TtPb02MiVBWtoEbqfgNitjoaHvnD1fVRQOoJ3ZSfagE9a92D4xRv78/qzAlC8cyjrP4efaKHyXK1Iw==");
        String plain = result.get("data");
        String sign = result.get("sign");
        plain = URLDecoder.decode(plain, "UTF-8");
        System.out.println("plain==>" + plain);
        System.out.println("sign==>" + sign);
        boolean verify = ECCUtil.eccVerify(publicKey, plain.getBytes(), Base64.decodeBase64(sign));
        System.out.println("verify==>" + verify);
    }


    /***
     * 获取url字符串的参数
     * @param urlString
     * @return
     */
    public static Map<String, String> parseURLParams(String urlString) {
        Map<String, String> params = new HashMap<>();
        try {
            URL url = new URL(urlString);
            String query = url.getQuery();
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    String key = pair.substring(0, idx);
                    String value = pair.substring(idx + 1);
                    params.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

}
