package com.company.project.utils;

import com.company.project.constant.ApplicationProperties;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/****
 * pdf生成工具
 */
public class PdfUtils {

    /****
     * 设置pdf下载
     * @throws UnsupportedEncodingException
     */
    public static void setPdfDownloadType(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/pdf;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName + ".pdf", "UTF-8"));  //为了下载的东西不乱码而设置前三个格式
    }


    /**
     * 填充html模板
     *
     * @param templateFile 模板文件名
     * @param args         模板参数
     * @param pdfFile      生成文件路径（如果为空则不生成对应的文件，可以直接利用文件的二进制流）
     */
    public static byte[] createPdfByHtmlTemplate(String templateFile, Map<String, String> args, String pdfFile) {
        FileOutputStream output = null;
        File pdfRes = null;
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
            String newPdfFile = StringUtils.isEmpty(pdfFile) ? ApplicationProperties.downloadPath + "\\" + UuidUtils.getUuid() + ".pdf" : pdfFile;
            pdfRes = new File(newPdfFile);
            output = new FileOutputStream(pdfRes);
            // 转换为PDF文档
            if (newPdfFile.indexOf("/") > 0) {
                File path = new File(newPdfFile.substring(0, newPdfFile.indexOf("/")));
                if (!path.exists())
                    path.mkdirs();
            }
            PdfDocument pdf = new PdfDocument(new PdfWriter(output));
            pdf.setDefaultPageSize(PageSize.A4);
            Document document = HtmlConverter.convertToDocument(html, pdf, props);
            document.getRenderer().close();
            document.close();
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
        byte[] content = FileUtils.getBytesByFile(pdfRes);
        if (StringUtils.isEmpty(pdfFile)) {
            pdfRes.delete();
        }
        return content;
    }


    /**
     * @param fields
     * @param data
     * @throws IOException
     * @throws DocumentException
     */
    private static void fillDataToPdfTemplate(AcroFields fields, Map<String, String> data) throws IOException, DocumentException {
        List<String> keys = new ArrayList<String>();
        Map<String, Item> formFields = fields.getFields();
        for (String key : data.keySet()) {
            if (formFields.containsKey(key)) {
                String value = data.get(key);
                fields.setField(key, value); // 为字段赋值,注意字段名称是区分大小写的
                keys.add(key);
            }
        }
        Iterator<String> itemsKey = formFields.keySet().iterator();
        while (itemsKey.hasNext()) {
            String itemKey = itemsKey.next();
            if (!keys.contains(itemKey)) {
                fields.setField(itemKey, " ");
            }
        }
    }

    /**
     * @param templatePdfPath 模板pdf路径
     * @param generatePdfPath 生成pdf路径
     * @param data            数据
     */
    public static byte[] createPdfByPdfTemplate(String templatePdfPath, String generatePdfPath, Map<String, String> data) {
        OutputStream fos = null;
        ByteArrayOutputStream bos = null;
        try {
            PdfReader reader = new PdfReader(templatePdfPath);
            bos = new ByteArrayOutputStream();
            /* 将要生成的目标PDF文件名称 */
            PdfStamper ps = new PdfStamper(reader, bos);
            ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
            ClassPathResource calibriFpr = new ClassPathResource("template/calibri.ttf");
            BaseFont calibriBaseFont = BaseFont.createFont(calibriFpr.getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontList.add(calibriBaseFont);
            /* 使用中文字体 */
            // BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // fontList.add(bf);
            ClassPathResource simsunFpr = new ClassPathResource("template/simsun.ttf");
            BaseFont simsunBaseFont = BaseFont.createFont(simsunFpr.getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontList.add(simsunBaseFont);
            /* 取出报表模板中的所有字段 */
            AcroFields fields = ps.getAcroFields();
            fields.setSubstitutionFonts(fontList);
            fillDataToPdfTemplate(fields, data);
            /* 必须要调用这个，否则文档不会生成的  如果为false那么生成的PDF文件还能编辑，一定要设为true*/
            ps.setFormFlattening(true);
            ps.close();
            if (!StringUtils.isEmpty(generatePdfPath)) {
                fos = new FileOutputStream(generatePdfPath);
                fos.write(bos.toByteArray());
                fos.flush();
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        ClassPathResource fpr = new ClassPathResource("template/pdfTemplate.pdf");
        Map<String, String> data = new HashMap<>();
        //key为pdf模板的form表单的名字，value为需要填充的值
        data.put("aText", "AAA123não123");
        data.put("bText", "中文中文中");
        createPdfByPdfTemplate(fpr.getPath(),
                "D:\\pdfTemplateChange.pdf", data);

      /*  Map<String, String> para = new HashMap<String, String>();
        para.put("name", "成功人士");
        createPdfByHtmlTemplate("template.html", para, "D:\\6666.pdf");*/

    }
}
