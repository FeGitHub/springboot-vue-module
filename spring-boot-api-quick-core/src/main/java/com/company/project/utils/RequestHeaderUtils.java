package com.company.project.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/***
 *  请求头工具类
 */
public class RequestHeaderUtils {


    /****
     * 获取 Authorization
     * @return
     */
    public static String getAuthorization() {
        String decodedAuth = "";
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String auth = StringUtils.getStr(request.getHeader("Authorization"));
        if ((auth != null) && (auth.length() > 6)) {
            auth = auth.substring(6);
            decodedAuth = new String(Base64.getDecoder().decode(auth));
            System.out.println("auth decoded from base64 is " + decodedAuth);//Username:password
        }
        return decodedAuth;
    }

    /****
     * 获取 Authorization的Username
     * @return
     */
    public static String getUsernameByAuth(String decodedAuth) {
        if (StringUtils.isNotEmpty(decodedAuth) && decodedAuth.indexOf(":") > -1) {
            String[] decodedAuthArr = decodedAuth.split(":");
            return decodedAuthArr.length == 2 ? decodedAuthArr[0] : "";
        }
        return null;
    }

    /****
     * 获取 Authorization的password
     * @return
     */
    public static String getPasswordByAuth(String decodedAuth) {
        if (StringUtils.isNotEmpty(decodedAuth) && decodedAuth.indexOf(":") > -1) {
            String[] decodedAuthArr = decodedAuth.split(":");
            return decodedAuthArr.length == 2 ? decodedAuthArr[1] : "";
        }
        return null;
    }
}
