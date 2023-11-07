package com.company.project.web;

import com.company.project.service.biz.QrCodeService;
import com.company.project.utils.FileUtils;
import com.company.project.utils.QrCodeUtils;
import com.company.project.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/****
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private QrCodeService qrCodeService;


    /**
     * 根据内容生成二维码
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/createQRCodeImage")
    public void createQRCodeImage(HttpServletResponse response, @RequestBody Map<String, Object> requestData) throws Exception {
        // String data = "11223344|440112345|3|20231017|900|20231019|20231017";
        String data = StringUtils.getStr(requestData.get("data"));
        byte[] content = qrCodeService.createQRCodeImage(data);
        FileUtils.writeFileToResponse(response, content);
    }


    /***
     * 上传有二维码的图片
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/qrCodeImageUpload")
    @ResponseBody
    public QrCodeService.QrCodeDataVo qrCodeImageUpload(MultipartFile file) throws IOException {
        String url = QrCodeUtils.getQrCodeUrl(file.getBytes());
        QrCodeService.QrCodeDataVo qrCodeDataVo = qrCodeService.getUrlData(url);
        return qrCodeDataVo;
    }


}
