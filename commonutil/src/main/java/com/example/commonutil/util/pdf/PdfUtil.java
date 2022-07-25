package com.example.commonutil.util.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * created by SunWanghe on 2022-06-01 13:31
 */
public class PdfUtil {

    public static String getTemplateContent(String templateDirectory, String templateName, Map<String, Object> paramMap) throws Exception {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            File file = new File(templateDirectory);
            configuration.setDirectoryForTemplateLoading(file);
        } catch (Exception e) {
            System.out.println("-- exception --");
        }

        Writer out = new StringWriter();
        Template template = configuration.getTemplate(templateName,"UTF-8");
        template.process(paramMap, out);
        out.flush();
        out.close();
        return out.toString();
    }


    public static byte[] html2Pdf(String content) throws IOException, DocumentException, URISyntaxException {
        ITextRenderer render = new ITextRenderer();
        ITextFontResolver fontResolver = render.getFontResolver();
        String FileDir =  System.getProperty("user.dir");
        InputStream inputStream = PdfUtil.class.getClassLoader().getResourceAsStream("static/fonts/simsun.ttf");
        File targetFile = new File(FileDir+"/resource/fonts/simsun.ttf");
        if(!targetFile.exists() && inputStream != null){
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
        }
        fontResolver.addFont(FileDir+"/resource/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        render.setDocumentFromString(content);
        render.layout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        render.createPDF(outputStream);
        render.finishPDF();
        return outputStream.toByteArray();
    }

    public byte[] createPdf(Map<String, Object> paramMap) throws IOException {
        String FileDir = System.getProperty("user.dir");
        InputStream inputStream = PdfUtil.class.getClassLoader().getResourceAsStream("templates/transferOrder.html");
        File targetFile = new File(FileDir + "/resource/templates/transferOrder.html");
        if (inputStream != null) {
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
        }
        String templateContent = null;
        try {
            templateContent = PdfUtil.getTemplateContent(FileDir + "/resource/templates/", "transferOrder.html", paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] bytes = new byte[0];
        try {
            bytes = PdfUtil.html2Pdf(templateContent);
        } catch (IOException | DocumentException | URISyntaxException e) {
            e.printStackTrace();
        }

        return bytes;
    }


}
