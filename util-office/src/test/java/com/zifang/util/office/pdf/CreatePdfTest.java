package com.zifang.util.office.pdf;

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
import java.util.stream.Collectors;

public class CreatePdfTest {

    public static void transform(String output , String imageFolder) throws IOException {
        PDDocument document = new PDDocument();

        List<File> fileList = Arrays.asList(new File(imageFolder).listFiles());
        fileList = fileList.stream().filter(e -> !e.getName().startsWith("."))
                .filter(e -> !e.getName().equals("Thumbs.db") && !e.getName().endsWith(".txt") && !e.getName().toLowerCase().endsWith(".url")
                        && !e.getName().endsWith(".html"))
                .collect(Collectors.toList());
        fileList.sort(Comparator.naturalOrder());
        for (File image : fileList) {

            // 创建图片
            PDImageXObject pdImage = PDImageXObject.createFromFile(image.getAbsolutePath(), document);
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
        }

        document.save(output);
        document.close();

    }

    public static void main(String[] args) throws IOException {
        String output = "/Volumes/zifang/工口/超电磁炮/(C78) [たくみなむち (たくみなむち)] 超電磁砲のまもりかた　上 (とある魔術の禁書目録) [無修正].pdf";
        String imageFolder = "/Volumes/zifang/工口/超电磁炮/(C78) [たくみなむち (たくみなむち)] 超電磁砲のまもりかた　上 (とある魔術の禁書目録) [無修正]";
        for(File file : new File("/Volumes/zifang/工口/COMIC").listFiles()){
            if(file.isDirectory()){
                String folder = file.getAbsolutePath();
                String pdf = folder+".pdf";
                try {
                    transform(pdf, folder);
                    deleteFolder(folder);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static void deleteFolder(String folder) {
        for(File file :new File(folder).listFiles()){
            file.delete();
        }
        new File(folder).delete();
    }
}
