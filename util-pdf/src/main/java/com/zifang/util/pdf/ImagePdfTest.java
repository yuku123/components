package com.zifang.util.pdf;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ImagePdfTest {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/zifang/Downloads/现代西班牙语1.pdf");
        PDDocument document = PDDocument.load(file);


        for (int i = 0; i < document.getPages().getCount(); i++) {

            // 图片内容
            PDPage page = document.getPage(i);
            PDResources resources = page.getResources();
            Iterable<COSName> cosNames = resources.getXObjectNames();
            if (cosNames != null) {
                Iterator<COSName> cosNamesIter = cosNames.iterator();
                while (cosNamesIter.hasNext()) {
                    COSName cosName = cosNamesIter.next();
                    if (resources.isImageXObject(cosName)) {
                        PDImageXObject Ipdmage = (PDImageXObject) resources.getXObject(cosName);
                        BufferedImage image = Ipdmage.getImage();
                        String path = "/Users/zifang/Downloads/现代西班牙语/#index.jpg";
                        path = path.replace("#index", i + "");
                        FileOutputStream out = new FileOutputStream(path);
                        try {
                            ImageIO.write(image, "jpg", out);
                        } catch (IOException e) {

                        } finally {
                            try {
                                out.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
        }
        document.close();

    }
}
