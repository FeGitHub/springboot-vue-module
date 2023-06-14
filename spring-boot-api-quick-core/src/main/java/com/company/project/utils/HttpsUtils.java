package com.company.project.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 外部接口调用Post
 */
@Slf4j
public class HttpsUtils {

    /**
     * 发送HttpClient请求
     * request header的Basic Authentication scheme
     *
     * @param requestUrl
     * @param params
     * @return
     */
    public static InputStream sendPostByBasicAuthGetStream(String requestUrl, String params, String Username, String password) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(requestUrl);
            // 设置请求头  Content-Type
            postMethod.setRequestHeader("Content-Type", "application/json");
            //Base64加密方式认证方式下的basic auth
            postMethod.setRequestHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((Username + ":" + password).getBytes()));
            // RequestEntity requestEntity = new StringRequestEntity(params, "application/json", "UTF-8");
            //  postMethod.setRequestEntity(requestEntity);
            httpClient.executeMethod(postMethod);// 执行请求
            inputStream = postMethod.getResponseBodyAsStream();// 获取返回的流
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }


}
