package com.company.project.service.biz;

import com.company.project.exception.BizTipException;
import com.company.project.utils.ECCUtil;
import com.company.project.utils.StringUtils;
import com.company.project.utils.UrlUtils;
import com.company.project.utils.qrcode.QrCodeUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.dubbo.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.PublicKey;
import java.util.Map;

/****
 *  二维码业务服务
 */
@Service
public class QrCodeService {


    public static final Logger logger = LoggerFactory.getLogger(QrCodeService.class);

    private static String urlPrefix = "https://uat-ga.safp.gov.mo";

    private static String key = "1";

    private static String publicKeyStr = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEpWUK8fn6TtPb02MiVBWtoEbqfgNitjoaHvnD1fVRQOoJ3ZSfagE9a92D4xRv78/qzAlC8cyjrP4efaKHyXK1Iw==";


    private static String privateKeyStr = "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgO8W8taCO4M/EQRrJxopcnMgWQd0Y1KcDmTVvC9hrANuhRANCAASlZQrx+fpO09vTYyJUFa2gRup+A2K2Ohoe+cPV9VFA6gndlJ9qAT1r3YPjFG/vz+rMCULxzKOs/h59oofJcrUj";


    /***
     * 校验生成数据
     * @param data
     */
    public void checkData(String data) {
        //如果是空的
        //如果分割不为7个
        String tip = "请确保生成的数据的格式为：x01|x02|x03|x04|x05|x06|x07";
        if (StringUtils.isEmpty(data)) {
            throw new RuntimeException(tip);
        } else {
            String[] split = data.split("\\|");
            if (split.length != 7) {
                throw new BizTipException(tip);
            }
        }
    }

    /***
     * 校验二维码的链接是否正确
     * @param url
     */
    public QrCodeDataVo checkQrCodeUrl(String url) throws UnsupportedEncodingException {
        QrCodeDataVo qrCodeDataVo = new QrCodeDataVo();
        if (StringUtils.isEmpty(url) || url.indexOf(urlPrefix) == -1) {
            throw new BizTipException("请检验图片是否是有效二维码信息");
        }
        qrCodeDataVo.setUrl(url);
        Map<String, String> result = UrlUtils.parseURLParams(url);
        String plain = result.get("data");
        String sign = result.get("sign");
        if (StringUtils.isEmpty(plain) || StringUtils.isEmpty(sign)) {
            throw new BizTipException("二维码并未包含有效数据信息");
        }
        qrCodeDataVo.setSign(sign);
        plain = URLDecoder.decode(plain, "UTF-8");
        qrCodeDataVo.setData(plain);
        return qrCodeDataVo;
    }


    /****
     * 创建二维码信息
     * @param data
     */
    public byte[] createQRCodeImage(String data) throws Exception {
        //封装的url的最终形式
        String qrCodeUrlTemplate = "%s/?key=%s&data=%s&sign=%s";
        //校验原生数据的合法性（业务层级）
        checkData(data); // data= x01|x02|x03|x04|x05|x06|x07
        //生成签名内容
        byte[] plain = data.getBytes();
        final byte[] signByteArr = ECCUtil.eccSign(ECCUtil.getPrivateKey(privateKeyStr), plain);
        String sign = Base64.encodeBase64String(signByteArr);
        String qrCodeUrl = String.format(qrCodeUrlTemplate, urlPrefix, key, URL.encode(data), sign);
        getUrlData(qrCodeUrl);
        byte[] content = QrCodeUtils.getQrCodeToByte(qrCodeUrl);
        return content;
    }


    /***
     * 验签并解析信息
     * @param url
     * @throws UnsupportedEncodingException
     */
    public QrCodeDataVo getUrlData(String url) throws UnsupportedEncodingException {
        String tip = "验签失败，请核查二维码信息是否正确";
        //检验链接
        checkQrCodeUrl(url);
        QrCodeDataVo qrCodeDataVo = checkQrCodeUrl(url);
        logger.info("plain==>" + qrCodeDataVo.getData());
        logger.info("sign==>" + qrCodeDataVo.getSign());
        PublicKey publicKey = ECCUtil.getPublicKey(publicKeyStr);
        try {
            boolean verify = ECCUtil.eccVerify(publicKey, qrCodeDataVo.getData().getBytes(), Base64.decodeBase64(qrCodeDataVo.getSign()));
            logger.info("verify==>" + verify);
            if (!verify) {
                throw new BizTipException(tip);
            }
        } catch (Exception e) {
            throw new BizTipException(tip);
        }
        return qrCodeDataVo;
    }


    /***
     *  二维码解析信息
     */
    public static class QrCodeDataVo {
        private String data;
        private String sign;
        private String url;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}
