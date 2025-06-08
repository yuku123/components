package com.zifang.util.office.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfUtil {

    public static List<File> fillImages(String pdfTargetFilePath , List<File> imageFiles, boolean errorFlag){

        List<File> su = new ArrayList<>();
        List<File> error = new ArrayList<>();

        PDDocument document  = new PDDocument();

        for (File image : imageFiles) {

            // 创建图片
            PDImageXObject pdImage = null;
            try {
                pdImage = PDImageXObject.createFromFile(image.getAbsolutePath(), document);
                int weight = pdImage.getWidth();
                int height = pdImage.getHeight();

                // 设置页大小
                PDRectangle rectangle = new PDRectangle(weight, height);
                PDPage page = new PDPage(rectangle);

                // 填充
                PDPageContentStream contents = new PDPageContentStream(document, page);
                contents.drawImage(pdImage, 0, 0, weight, height);

                // 添加到文档
                document.addPage(page);
                contents.close();
            } catch (Exception e){
                error.add(image);
                continue;
            }
            su.add(image);
        }

        try {
            document.save(pdfTargetFilePath);
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(errorFlag){
            return error;
        } else {
            return su;
        }
    }
}
