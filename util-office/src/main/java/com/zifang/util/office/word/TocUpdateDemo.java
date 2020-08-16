package com.zifang.util.office.word;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.toc.TocGenerator;

public class TocUpdateDemo  {

    static boolean update = false;

    public static void main(String[] args) throws Exception{

        String input_DOCX = "/Users/zifang/Downloads/test.docx";

        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(
                new java.io.File(input_DOCX));
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();


        TocGenerator tocGenerator = new TocGenerator(wordMLPackage);

//        tocGenerator.setDocumentServicesEndpoint("http://192.168.2.16:9015/v1/00000000-0000-0000-0000-000000000000/convert");

//        	Toc.setTocHeadingText("Sumário");
        tocGenerator.updateToc( false); // true --> skip page numbering; its currently much faster

        wordMLPackage.save(new java.io.File("/Users/zifang/Downloads/test_out.docx") );

    }


}