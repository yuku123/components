package com.zifang.util.pdf;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

public class AnalysisPdf {
    public static void main(String[] args) throws IOException {
        String output = "/Users/zifang/Downloads/t.pdf";

        PDDocument document;
        PDFParser parser = new PDFParser(new RandomAccessBuffer(new FileInputStream(output)));
        parser.parse();
        document = parser.getPDDocument();

        PDFTextStripper stripper = new PDFTextStripper();
        //设置输出顺序（是否排序）
        stripper.setSortByPosition(true);
        stripper.setStartPage(1);
        stripper.setEndPage(document.getPages().getCount());
        //文本内容
        String text = stripper.getText(document);
        System.out.println(text);

        System.out.println("'");

    }
}
