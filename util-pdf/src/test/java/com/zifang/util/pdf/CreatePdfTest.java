package com.zifang.util.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreatePdfTest {
    public static void main(String[] args) throws IOException {
        String output = "/Users/zifang/Downloads/imagess.pdf";
        String imageFolder = "/Users/zifang/Downloads/imageFolder";

        PDDocument document = new PDDocument();

        List<File> fileList = Arrays.asList(new File(imageFolder).listFiles());
        fileList = fileList.stream().filter(e->!e.getName().startsWith(".")).collect(Collectors.toList());
        fileList.sort(Comparator.naturalOrder());
        for(File image : fileList){

            // 创建图片
            PDImageXObject pdImage = PDImageXObject.createFromFile(image.getAbsolutePath(),document);
            int weight = pdImage.getWidth();
            int height = pdImage.getHeight();

            // 设置页大小
            PDRectangle rectangle = new PDRectangle(weight,height);
            PDPage page = new PDPage(rectangle);

            // 填充
            PDPageContentStream contents = new PDPageContentStream(document, page);
            contents.drawImage(pdImage,0,0,weight,height);

            // 添加到文档
            document.addPage(page);
            contents.close();
        }

        document.save(output);
        document.close();


    }
}
