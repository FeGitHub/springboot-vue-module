package com.company.project.utils;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成PDF文件
 */
public class PdfFile {

    /**
     * 填充html模板
     *
     * @param templateFile 模板文件名
     * @param args         模板参数
     * @param pdfFile      生成文件路径
     */
    public static FileOutputStream template(String templateFile, Map<String, String> args, String pdfFile) {
        FileOutputStream output = null;
        try {
            // 读取模板文件,填充模板参数
            Configuration freemarkerCfg = new Configuration(Configuration.VERSION_2_3_28);
            freemarkerCfg.setTemplateLoader(new ClassTemplateLoader(PdfFile.class, "/template/"));
            Template template = freemarkerCfg.getTemplate(templateFile);
            StringWriter out = new StringWriter();
            if (args != null && args.size() > 0)
                template.process(args, out);
            String html = out.toString();
            // 设置字体以及字符编码
            ConverterProperties props = new ConverterProperties();
            FontProvider fontProvider = new FontProvider();
            fontProvider.addStandardPdfFonts();
            ClassPathResource simsunFpr = new ClassPathResource("template/simsun.ttf");
            fontProvider.addFont(simsunFpr.getPath());
            props.setFontProvider(fontProvider);
            props.setCharset("utf-8");
            output = new FileOutputStream(new File(pdfFile));
            if (!StringUtils.isEmpty(pdfFile)) {
                // 转换为PDF文档
                if (pdfFile.indexOf("/") > 0) {
                    File path = new File(pdfFile.substring(0, pdfFile.indexOf("/")));
                    if (!path.exists())
                        path.mkdirs();
                }
                PdfDocument pdf = new PdfDocument(new PdfWriter(output));
                pdf.setDefaultPageSize(PageSize.A4);
                Document document = HtmlConverter.convertToDocument(html, pdf, props);
                document.getRenderer().close();
                document.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    public static void main(String[] args) {
        Map<String, String> para = new HashMap<String, String>();
        para.put("name", "成功人士");
        template("template.html", para, "D:\\2333.pdf");
    }

}
