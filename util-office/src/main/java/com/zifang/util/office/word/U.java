package com.zifang.util.office.word;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public class U {

    public static Integer WIDTH = 9072;


    public static XWPFRun addText(XWPFParagraph p1, String s, int i) {
        XWPFRun titleParagraphRun = addText(p1, s);
        titleParagraphRun.setFontSize(i);
        return titleParagraphRun;
    }

    public static XWPFRun addText(XWPFParagraph titleParagraph, String s) {
        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraphRun.addCarriageReturn();
        titleParagraphRun.setText(s);
        return titleParagraphRun;
    }

    public static XWPFRun addTextCenter(XWPFParagraph titleParagraph, String s) {
        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        titleParagraphRun.setText(s);
        return titleParagraphRun;
    }

    public static XWPFRun addTextRight(XWPFParagraph titleParagraph, String s) {
        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraph.setAlignment(ParagraphAlignment.RIGHT);
        titleParagraphRun.setText(s);
        return titleParagraphRun;
    }

    public static XWPFRun addText(XWPFParagraph titleParagraph, List<String> textList) {
        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraphRun.addCarriageReturn();
        for (String text : textList) {
            titleParagraphRun.setText(text);
            titleParagraphRun.addCarriageReturn();
        }
        return titleParagraphRun;
    }

    public static void addLoopText(XWPFParagraph titleParagraph, String s, Integer loopTimes) {
        for (Integer i = 0; i < loopTimes; i++) {
            XWPFRun titleParagraphRun = titleParagraph.createRun();
            titleParagraphRun.addCarriageReturn();
            titleParagraphRun.setText(s);
        }
    }

    public static void createHeading1(XWPFParagraph paragraph, String title) {
        paragraph.setStyle("Heading 1");
        XWPFRun run = paragraph.createRun();
        run.setText(title);
        run.setBold(true);//标题加粗
    }

    public static void createHeading2(XWPFParagraph paragraph, String title) {
        paragraph.setStyle("Heading 2");
        XWPFRun run = paragraph.createRun();
        run.setText(title);
        run.setBold(true);//标题加粗
    }

    public static void createHeading3(XWPFParagraph paragraph, String title) {
        paragraph.setStyle("Heading 3");
        XWPFRun run = paragraph.createRun();
        run.setText(title);
        run.setBold(true);//标题加粗
    }


    public static void createHeading4(XWPFParagraph paragraph, String title) {
        paragraph.setStyle("Heading 4");
        XWPFRun run = paragraph.createRun();
        run.setText(title);
        run.setBold(true);//标题加粗
    }

    public static void createGraph(XWPFParagraph paragraph, String path) throws Throwable {
        if (!new File(path).exists()) {
            throw new Exception("文件不存在啊:" + path);
        }
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        try {
            run.addPicture(new FileInputStream(path), XWPFDocument.PICTURE_TYPE_JPEG, path, Units.toEMU(400), Units.toEMU(300));
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Throwable(e);
        }
    }

    public static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel)); // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull); // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull); // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle); // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    public static void addCustomHeadingStyle(XWPFDocument docxDocument, XWPFStyles styles, String strStyleId, int headingLevel, int pointSize, String hexColor) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);


        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
        size.setVal(new BigInteger(String.valueOf(pointSize)));

        CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
        size2.setVal(new BigInteger("24"));

        CTFonts fonts = CTFonts.Factory.newInstance();
        fonts.setAscii("Loma");


        CTRPr rpr = CTRPr.Factory.newInstance();
        rpr.setRFonts(fonts);
        rpr.setSz(size);
        rpr.setSzCs(size2);


        CTColor color = CTColor.Factory.newInstance();
        color.setVal(hexToBytes(hexColor));
        rpr.setColor(color);
        style.getCTStyle().setRPr(rpr);
        // is a null op if already defined

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    public static byte[] hexToBytes(String hexString) {
        HexBinaryAdapter adapter = new HexBinaryAdapter();
        byte[] bytes = adapter.unmarshal(hexString);
        return bytes;
    }

    public static void initialStyles(XWPFDocument document) {
        XWPFStyles styles = document.createStyles();
        //对document 进行处理
        U.addCustomHeadingStyle(document, styles, "Heading 1", 1, 44, "4288BC");
        U.addCustomHeadingStyle(document, styles, "Heading 2", 2, 32, "4288BC");
        U.addCustomHeadingStyle(document, styles, "Heading 3", 3, 32, "4288BC");
        U.addCustomHeadingStyle(document, styles, "Heading 4", 4, 32, "000000");
    }

    public static void addSimpleTable(XWPFDocument document, List<Tuple<String, String>> list, List<Map<String, Object>> dataMapList) {
        //基本信息表格
        XWPFTable infoTable = document.createTable();


        CTTblBorders borders = infoTable.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder hBorder = borders.addNewInsideH();
        hBorder.setVal(STBorder.Enum.forString("single"));
        hBorder.setSz(new BigInteger("1"));
        hBorder.setColor("000000");

        CTBorder vBorder = borders.addNewInsideV();
        vBorder.setVal(STBorder.Enum.forString("single"));
        vBorder.setSz(new BigInteger("1"));
        vBorder.setColor("000000");

        CTBorder lBorder = borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString("single"));
        lBorder.setSz(new BigInteger("1"));
        lBorder.setColor("000000");

        CTBorder rBorder = borders.addNewRight();
        rBorder.setVal(STBorder.Enum.forString("single"));
        rBorder.setSz(new BigInteger("1"));
        rBorder.setColor("000000");

        CTBorder tBorder = borders.addNewTop();
        tBorder.setVal(STBorder.Enum.forString("single"));
        tBorder.setSz(new BigInteger("1"));
        tBorder.setColor("000000");

        CTBorder bBorder = borders.addNewBottom();
        bBorder.setVal(STBorder.Enum.forString("single"));
        bBorder.setSz(new BigInteger("1"));
        bBorder.setColor("000000");

        CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
        infoTableWidth.setType(STTblWidth.DXA);
        infoTableWidth.setW(BigInteger.valueOf(9072));

        //header 处理“
        for (int i = 0; i < list.size(); i++) {
            XWPFTableRow xwpfTableRow = infoTable.getRow(0);
            if (i == 0) {
                xwpfTableRow.getCell(0).setText(list.get(i).getKey());
            } else {
                xwpfTableRow.addNewTableCell().setText(list.get(i).getKey());
            }
        }

        for (Map<String, Object> data : dataMapList) {
            XWPFTableRow xwpfTableRow = infoTable.createRow();

            for (int i = 0; i < list.size(); i++) {

                if (i == 0) {
                    xwpfTableRow.getCell(0).setText(data.get(list.get(i).getValue()).toString());
                } else {
                    try {
                        Object o = data.get(list.get(i).getValue());
                        if (String.valueOf(o).endsWith(".0")) {
                            xwpfTableRow.getCell(i).setText(String.valueOf(o).split("[.]")[0]);
                        } else if (o instanceof Double) {
                            xwpfTableRow.getCell(i).setText(String.valueOf(Double.valueOf(o.toString()).doubleValue()));
                        } else if (o instanceof Integer) {
                            xwpfTableRow.getCell(i).setText(String.valueOf(Integer.valueOf(o.toString())));
                        } else {
                            xwpfTableRow.getCell(i).setText(String.valueOf(data.get(list.get(i).getValue())));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /***
     *
     * 支持调节行内各自间距离
     *
     * */
    public static void addSimpleTable(XWPFDocument document, List<Tuple<String, String>> list, List<Map<String, Object>> dataMapList, Map<Integer, String> dimention) {
        //基本信息表格
        XWPFTable infoTable = document.createTable();

        CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
        //infoTableWidth.setType(STTblWidth.DXA);
        infoTableWidth.setW(BigInteger.valueOf(9072));

        //header 处理“
        for (int i = 0; i < list.size(); i++) {
            XWPFTableRow xwpfTableRow = infoTable.getRow(0);
            if (i == 0) {
                xwpfTableRow.getCell(0).setText(list.get(i).getKey());
                xwpfTableRow.getCell(0).setWidth(dimention.get(0));
            } else {
                XWPFTableCell cell = xwpfTableRow.addNewTableCell();
                cell.setText(list.get(i).getKey());
                cell.setWidth(dimention.get(i));
            }
        }

        for (Map<String, Object> data : dataMapList) {
            XWPFTableRow xwpfTableRow = infoTable.createRow();

            for (int i = 0; i < list.size(); i++) {

                if (i == 0) {
                    xwpfTableRow.getCell(0).setText(data.get(list.get(i).getValue()).toString());
                    xwpfTableRow.getCell(0).setWidth(dimention.get(i));
                } else {
                    xwpfTableRow.getCell(i).setText(data.get(list.get(i).getValue()).toString());
                    xwpfTableRow.getCell(i).setWidth(dimention.get(i));

                }
            }
        }
    }


    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {

        Integer start = null;
        Integer colNum = table.getRow(row).getTableCells().size();
        Integer toBe = U.WIDTH * (toCell - fromCell + 1) / colNum;
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);

            if (cellIndex == fromCell) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
                start = cellIndex;
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
        table.getRow(row).getCell(start).setWidth("" + toBe);
    }

    // word跨行并单元格
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    public static void appendingBody(XWPFTable t_1_5_1, List<Map<String, Object>> mapList) {
        for (Map<String, Object> map : mapList) {
            XWPFTableRow row = t_1_5_1.createRow();
            int i = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                row.getCell(i++).setText(entry.getValue().toString());
            }
        }
    }

    public static void do_1_5_1_SpecialHeader(XWPFTable infoTable) {

        CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
        infoTableWidth.setType(STTblWidth.DXA);//9072
        infoTableWidth.setW(BigInteger.valueOf(9072));

        infoTable.getRow(0).getCell(0).setText("书龄");

        infoTable.getRow(0).getCell(1).setText("销售");
        infoTable.getRow(0).getCell(4).setText("库存");

        infoTable.getRow(1).getCell(1).setText("品种(万种)");
        infoTable.getRow(1).getCell(2).setText("册数(万册)");
        infoTable.getRow(1).getCell(3).setText("码洋(万元)");
        infoTable.getRow(1).getCell(4).setText("品种(万种)");
        infoTable.getRow(1).getCell(5).setText("册数(万册)");
        infoTable.getRow(1).getCell(6).setText("码洋(万元)");

        mergeCellsHorizontal(infoTable, 0, 1, 3);
        mergeCellsHorizontal(infoTable, 0, 4, 6);
        mergeCellsVertically(infoTable, 0, 0, 1);
    }
}
