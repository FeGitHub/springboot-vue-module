package com.company.project.web;

import com.company.project.master.model.Config;
import com.company.project.service.impl.ConfigServiceImpl;
import com.company.project.utils.FileUtils;
import com.company.project.utils.MailUtils;
import com.company.project.utils.QrCodeUtils;
import com.company.project.utils.ZipUtils;
import com.company.project.vo.util.mail.SendMailVo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/****
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private ConfigServiceImpl configServiceImpl;

    /**
     * 根据内容生成二维码
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/createQRCode")
    public String createQRCode() {
        String base64Str = "";
        try {
            String url = "https://www.baidu.com/";
            base64Str = QrCodeUtils.getQrCodeJumpBase64(url);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return base64Str;
    }

    /***
     * 上传有二维码的图片
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/qrCodeImageUpload")
    @ResponseBody
    public String qrCodeImageUpload(MultipartFile file) throws IOException {
        String url = QrCodeUtils.getQrCodeUrl(file.getBytes());
        System.out.println(url);
        return url;
    }


    @ApiOperation(value = "批量下载", notes = "下载ZIP压缩包")
    @RequestMapping(value = "/downloadZip", method = RequestMethod.GET)
    public ResponseEntity downloadZip(HttpServletResponse response) throws Exception {
        List<ZipUtils.DownloadFileVo> downloadFileDtoList = new ArrayList<>();
        // 这里的i的作用是保证文件名唯一，否则往ZIP中添加文件会报异常
        for (int i = 0; i <= 3; i++) {
            // 你的每一个文件字节流
            byte[] bytes = FileUtils.getTemplateFile("template/test.pdf");
            String fileName = i + ".pdf";
            ZipUtils.DownloadFileVo dto = new ZipUtils.DownloadFileVo();
            dto.setFileName(fileName);
            dto.setByteDataArr(bytes);
            downloadFileDtoList.add(dto);
            i++;
        }
        return ZipUtils.downloadZip("下载.zip", downloadFileDtoList, response);
    }


    /**
     * 邮件发送
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/sendMail")
    public void sendMail() {
        Config config = configServiceImpl.findBy("configtype", "mail");
        MailUtils.setSendMailConfig(config);
        MailUtils.sendMail(new SendMailVo("XXX@qq.com", "系统通知", "系统通知内容"));
    }

}
