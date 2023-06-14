package com.company.project.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class OkHttpClientUtils {


    public static ResponseBody okHttpClientByAuth(String apiUrl, String Username, String password) throws IOException {
        // 指定接口地址
        // 创建 OkHttp 客户端对象
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 设置连接超时时间为15秒
                .readTimeout(60, TimeUnit.SECONDS) // 设置读取超时时间为15秒
                .build();
        //  MediaType type = MediaType.parse("application/json; charset=utf-8");
        // RequestBody body = RequestBody.create(type, jsonStr);
        // 创建请求对象
        Request request = new Request.Builder()
                .header("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((Username + ":" + password).getBytes()))
                //  .post(body)
                .url(apiUrl)
                .build();
        // 发起请求
        Response response = client.newCall(request).execute();
        // 获取响应体
        return response.body();
    }


    public static InputStream okHttpClientDownloadPdf(String apiUrl, String jsonStr, String Username, String password, boolean close) throws IOException {
        ResponseBody responseBody = okHttpClientByAuth(apiUrl, Username, password);
        if (close) {
            // 创建输出流
            FileOutputStream outputStream = new FileOutputStream("D:/example.pdf");
            // 将响应体的数据写入输出流，生成 PDF 文件
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = responseBody.byteStream().read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 关闭流
            responseBody.byteStream().close();
            outputStream.close();
        }
        return responseBody.byteStream();
    }
}
