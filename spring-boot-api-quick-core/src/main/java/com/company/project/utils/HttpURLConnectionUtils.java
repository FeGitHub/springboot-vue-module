package com.company.project.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.json.JsonMapper;

@Component
public class HttpURLConnectionUtils {


    private static Logger logger = LoggerFactory.getLogger(HttpURLConnectionUtils.class);


    @Autowired
    private static JsonMapper jsonMapper;

    private HttpURLConnectionUtils() {

    }

    static {
        try {
            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");// 第一个参数为协议,第二个参数为提供者(可以缺省)
            TrustManager[] tm = { new MyX509TrustManager() };
            sslcontext.init(null, tm, new SecureRandom());
            HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslsession) {
                    // System.out.println("WARNING: Hostname is not matched for cert.");
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static final int TIMEOUT_SECONDS = 60 * 1000;

    /**
     * 服务端需要用@RequestBody接收
     */
    public static String doPostByJson(String url, Object reqeustBody) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        HttpURLConnection conn = null;
        try {
            String params = "{}";
            if (reqeustBody != null) {
                params = jsonMapper.writeValueAsString(reqeustBody);
            }
            byte[] data = encode(params);

            URL urlCon = new URL(url);

            conn = (HttpURLConnection) urlCon.openConnection();

            conn.setDoOutput(true); // 允许向服务器输出数据
            conn.setDoInput(true); // 允许接收服务器数据
            conn.setRequestMethod("POST");
            conn.setUseCaches(false); // Post 请求不能使用缓存
            conn.setConnectTimeout(TIMEOUT_SECONDS);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.connect();
            outputStream = conn.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

            inputStream = conn.getInputStream();
            // 将输入流转换成字符串
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            String json = baos.toString();
            baos.close();
            inputStream.close();
            conn.disconnect();
            return json;

        } catch (Exception e) {
            closeStream(outputStream, inputStream, baos);
            conn.disconnect();
            throw new IllegalStateException(e);
        }
    }

    public static String doPostByFile(String url, Map<String, Object> reqeustBody) {
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        HttpURLConnection conn = null;

        try {
            String boundary = "----WebKitFormBoundaryBWY9rUwANhCMtzsG";

            URL urlCon = new URL(url);
            // 文件上传的connection的一些必须设置
            conn = (HttpURLConnection) urlCon.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(TIMEOUT_SECONDS); // 连接超时为10秒
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            conn.connect();

            outputStream = new DataOutputStream(conn.getOutputStream());

            // 文件
            Set<String> fileKeySet = reqeustBody.keySet();
            for (String name : fileKeySet) {
                Object value = reqeustBody.get(name);
                outputStream.writeBytes("--" + boundary + "\r\n");
                if (value instanceof File) {
                    outputStream.write(encode("Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
                            + ((File) value).getName() + "\"\r\n"));
                    outputStream.writeBytes("Content-Type: " + getContentType(((File) value)) + "\r\n");
                } else if (value instanceof byte[]) {
                    outputStream.write(encode(
                            "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + name + "\"\r\n"));
                    outputStream.writeBytes("Content-Type: " + "application/octet-stream" + "\r\n");
                } else {
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
                }

                outputStream.writeBytes("\r\n");
                if (value instanceof File) {
                    outputStream.write(FileCopyUtils.copyToByteArray((File) value));
                } else if (value instanceof byte[]) {
                    outputStream.write((byte[]) value);
                } else {
                    outputStream.write(encode(Objects.toString(value, "")));
                }
                outputStream.writeBytes("\r\n");
            }

            // 添加结尾数据
            outputStream.writeBytes("--" + boundary + "--" + "\r\n");
            outputStream.writeBytes("\r\n");
            outputStream.close();

            inputStream = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            String result = baos.toString();
            baos.close();
            inputStream.close();
            conn.disconnect();
            return result;
        } catch (Exception e) {
            closeStream(outputStream, inputStream, baos);
            conn.disconnect();
            throw new IllegalStateException(e);
        }
    }

    public static byte[] doPostForByte(String url, Map<String, Object> reqeustBody) {
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        HttpURLConnection conn = null;
        try {

            URL urlCon = new URL(url);
            // 文件上传的connection的一些必须设置
            conn = (HttpURLConnection) urlCon.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(TIMEOUT_SECONDS); // 连接超时为10秒
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();

            outputStream = new DataOutputStream(conn.getOutputStream());

            // 文件
            Set<String> fileKeySet = reqeustBody.keySet();
            StringBuilder sb = new StringBuilder();
            for (String name : fileKeySet) {
                Object value = reqeustBody.get(name);
                if (value instanceof Array) {
                    for (Object val : (Object[]) value) {
                        sb.append(name + "=" + Objects.toString(val, "") + "&");
                    }
                } else {
                    sb.append(name + "=" + Objects.toString(value, "") + "&");
                }
            }
            outputStream.write(encode(sb.toString()));

            inputStream = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            byte[] result = baos.toByteArray();
            baos.close();
            inputStream.close();
            conn.disconnect();
            return result;
        } catch (Exception e) {
            closeStream(outputStream, inputStream, baos);
            conn.disconnect();
            logger.error("url",url);
            logger.error("reqeustBody",reqeustBody.toString());
            throw new IllegalStateException(e);

        }
    }

    public static String doPostForString(String url, Map<String, Object> reqeustBody) {
        return new String(doPostForByte(url, reqeustBody));
    }

    // 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
    private static String getContentType(File f) throws Exception {

        // return "application/octet-stream"; // 此行不再细分是否为图片，全部作为application/octet-stream 类型
        ImageInputStream imagein = ImageIO.createImageInputStream(f);
        if (imagein == null) {
            return "application/octet-stream";
        }
        Iterator<ImageReader> it = ImageIO.getImageReaders(imagein);
        if (!it.hasNext()) {
            imagein.close();
            return "application/octet-stream";
        }
        imagein.close();
        return "image/" + it.next().getFormatName().toLowerCase();// 将FormatName返回的值转换成小写，默认为大写

    }

    // 对包含中文的字符串进行转码，此为UTF-8
    private static byte[] encode(String value) throws Exception {
        return value.getBytes("UTF-8");
    }

    private static void closeStream(OutputStream outputStream, InputStream inputStream, ByteArrayOutputStream baos) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e1) {
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e1) {
            }
        }
        if (baos != null) {
            try {
                baos.close();
            } catch (IOException e1) {
            }
        }
    }

    public static String doGet(String url) {
        try {
            URL urlCon = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlCon.openConnection();
            conn.setDoOutput(false); // Get请求不需要DoOutPut
            conn.setDoInput(true); // 允许接收服务器数据
            conn.setRequestMethod("GET");
            conn.setUseCaches(false); // Post 请求不能使用缓存
            conn.setConnectTimeout(TIMEOUT_SECONDS);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                // 将输入流转换成字符串
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                String json = baos.toString();
                baos.close();
                is.close();
                return json;
            } else {
                throw new RuntimeException("HttpURLConnection,error status code :" + conn.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static class MyX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
