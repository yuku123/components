package com.zifang.util.office.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.File;
import java.io.IOException;

public class PdfTest {
    public static void main(String[] args) throws IOException {
        //Loading an existing document
        File file = new File("/Users/zifang/Downloads/现代西班牙语1.pdf");
        PDDocument documentRead = PDDocument.load(file);
        PDDocument documentWrite = new PDDocument();

        PDPageContentStream pdPageContentStream = new PDPageContentStream(documentRead, documentRead.getPage(1));
        System.out.println("PDF loaded");
        //Adding a blank page to the document
        documentWrite.addPage(new PDPage());
        //Saving the document
        documentWrite.save("/Users/zifang/Downloads/现代西班牙语1-DEMO.pdf");
        //Closing the document
        documentWrite.close();
    }
}
