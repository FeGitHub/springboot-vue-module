package com.company.project.service.biz;

import com.company.project.exception.BizTipException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Locale;

/***
 *  文件上傳校驗
 */
@Service
public class UploadMultipartFileValidateService {
    /**
     * 文件后缀 支持的类型
     */
    private static final String[] FILE_SUFFIX_SUPPORT = {".xlsx", ".xls", ".doc", ".docx", ".txt", ".csv",
            ".jpg", ".jpeg", ".png"};
    /**
     * 文件名字 需要排除的字符
     */
    private static final String[] FILE_NAME_EXCLUDE = {
            "`", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "=", "_", "+",
            "~", "·", "！", "￥", "……", "（", "）", "——",
            "?", ",", "<", ">", ":", ";", "[", "]", "{", "}", "/", "\\", "|",
            "？", "，", "。", "《", "》", "：", "；", "【", "】", "、",
    };

    /**
     * 文件大小 5MB
     */
    private static final long FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 上传文件校验大小、名字、后缀
     *
     * @param multipartFile multipartFile
     */
    public static void uploadVerify(MultipartFile multipartFile) {
        // 校验文件是否为空
        if (multipartFile == null) {
            //文件不能為空！
            throw new BizTipException("文件不能为空！");
        }
        // 校验文件大小
        long size = multipartFile.getSize();
        if (size > FILE_SIZE) {
            //文件大小不能超過5MB!
            throw new BizTipException("文件大小不能超过5MB！");
        }
        // 校验文件名字
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            //文件名稱不能為空！
            throw new RuntimeException("文件名字不能为空！");
        }
        boolean nameFlag = false;
        for (String s : FILE_NAME_EXCLUDE) {
            if (originalFilename.contains(s)) {
                nameFlag = true;
                break;
            }
        }
        if (nameFlag) {
            // 文件名稱不允許出現{0}關鍵字！
            throw new BizTipException("文件名字不允许出现" + Arrays.toString(FILE_NAME_EXCLUDE) + "关键字！");
        }
        //后缀 类型 二选一 或者 都校验
        // 校验文件后缀
        if (!originalFilename.contains(".")) {
            //文件不能沒有後綴！
            throw new BizTipException("文件不能没有后缀！");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        // 获取文件类型
        String fileType = multipartFile.getContentType();
        if (null == fileType) {
            //獲取不到文件類型
            throw new BizTipException("获取不到文件类型！");
        }
        boolean flag = true;
        //
        //  System.out.println("fileType==>" + fileType.toLowerCase(Locale.ROOT));
        //  System.out.println("suffix==>" + suffix.toLowerCase(Locale.ROOT));
        for (String s : FILE_SUFFIX_SUPPORT) {
            if (s.equals(suffix.toLowerCase(Locale.ROOT))) {
                flag = false;
                break;
            }
        }
        if (flag) {
            // 文件格式僅限於{0}！
            throw new BizTipException("文件格式仅限于" + Arrays.toString(FILE_SUFFIX_SUPPORT) + "！");
        }
    }
}
