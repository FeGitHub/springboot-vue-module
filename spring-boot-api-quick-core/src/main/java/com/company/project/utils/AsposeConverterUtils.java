package com.company.project.utils;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.FileOutputStream;

public class AsposeConverterUtils {

    public static void main(String[] args) throws Exception {
        AsposeConverterUtils bean = new AsposeConverterUtils();
        bean.wordToPdf("D:\\wordTemplate.docx", "D:\\wordTemplate.pdf");
    }

    /**
     * 验证License
     * 若不验证则转化出的PDF文档会有水印产生
     *
     * @return
     */
    public boolean getLicense() {
        boolean result = false;
        try {
            License license = new License();
            license.setLicense(this.getClass().getClassLoader().getResourceAsStream("license.xml"));
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!result) {
                System.out.println("非法");
            }
            return result;
        }
    }

    /**
     * 基于Aspose.words实现Word转PDF
     *
     * @param wordPath Word输入路径
     * @param pdfPath  PDF输出路径
     * @throws Exception
     */
    public void wordToPdf(String wordPath, String pdfPath) throws Exception {
        if (!getLicense()) {
            return;
        }
        long start = System.currentTimeMillis();

        // 解决乱码
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            // Assume Mac OS
        } else if (osName.startsWith("Windows")) {
            // Assume Windows
        } else {
            // Assume Unix or Linux
            // 如果还有乱码，可以把/usr/share/fonts/chinese路径下的所有文件拷贝到有问题的环境。并且再执行：source /etc/profile
            new FontSettings().setFontsFolder("/usr/share/fonts/chinese", true);
        }

        // 创建Document，构造函数入参也可以是InputStream
        Document document = new Document(wordPath);
        // 全面支持DOC, DOCX, OOXML, RTF, HTML, OpenDocument, PDF, EPUB, XPS, SWF相互转换
        document.save(new FileOutputStream(pdfPath), SaveFormat.PDF);

        System.out.println("Aspose.words转换结束，共耗时：" + ((System.currentTimeMillis() - start) / 1000.0) + "秒");
    }
}
