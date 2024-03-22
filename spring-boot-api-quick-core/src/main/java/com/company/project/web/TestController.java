package com.company.project.web;

import com.company.project.utils.SplitDayUtils;
import com.company.project.vo.SplitDayUtilsVo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/****
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @ResponseBody
    @PostMapping(value = "/splitDayUtils")
    public List<String> splitDayUtils(HttpServletResponse response, @RequestBody SplitDayUtilsVo vo) {
        return SplitDayUtils.run(vo);
    }




   /* public static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private QrCodeService qrCodeService;



    *//**
     * 根据内容生成二维码
     *
     * @return
     *//*
    @ResponseBody
    @PostMapping(value = "/createQRCodeImage")
    public void createQRCodeImage(HttpServletResponse response, @RequestBody Map<String, Object> requestData) throws Exception {
        // String data = "11223344|440112345|3|20231017|900|20231019|20231017";
        String data = StringUtils.getStr(requestData.get("data"));
        byte[] content = qrCodeService.createQRCodeImage(data);
        FileUtils.writeFileToResponse(response, content);
    }


    *//***
     * 上传有二维码的图片
     * @param file
     * @return
     * @throws IOException
     *//*
    @PostMapping("/qrCodeImageUpload")
    @ResponseBody
    public QrCodeService.QrCodeDataVo qrCodeImageUpload(MultipartFile file) throws IOException {
        String url = QrCodeUtils.getQrCodeUrl(file.getBytes());
        QrCodeService.QrCodeDataVo qrCodeDataVo = qrCodeService.getUrlData(url);
        return qrCodeDataVo;
    }
*/

}
