package com.zifang.util.core.demo.temp.tool;


public class Numeric2ChineseStr {

    public static void main(String[] args) throws Exception {
//        num2chinse("10000000");
        num2chinse("100000");
    }

    public static String num2chinse(String numStr) throws Exception {
        // 单位数组
        String[] units = new String[]{"十", "百", "千", "万", "十", "百", "千", "亿"};

        // 中文大写数字数组  
        String[] numeric = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        // 读文件  
        String res = "";

        // 遍历一行中所有数字
        for (int k = -1; numStr.length() > 0; k++) {
            // 解析最后一位
            int j = Integer.parseInt(numStr.substring(numStr.length() - 1, numStr.length()));
            String rtemp = numeric[j];

            // 数值不是0且不是个位 或者是万位或者是亿位 则去取单位
            if (j != 0 && k != -1 || k % 8 == 3 || k % 8 == 7) {
                rtemp += units[k % 8];
            }

            // 拼在之前的前面
            res = rtemp + res;

            // 去除最后一位
            numStr = numStr.substring(0, numStr.length() - 1);
        }

        // 去除后面连续的零零..
        while (res.endsWith(numeric[0])) {
            res = res.substring(0, res.lastIndexOf(numeric[0]));
        }

        res = res.replaceAll("零零零零万","");

        // 将零零替换成零
        while (res.indexOf(numeric[0] + numeric[0]) != -1) {
            res = res.replaceAll(numeric[0] + numeric[0], numeric[0]);
        }

        if(res.startsWith("一十")){
            res = res.substring(1);
        }

        // 将 零+某个单位 这样的窜替换成 该单位 去掉单位前面的零
        for (int m = 1; m < units.length; m++) {
            res = res.replaceAll(numeric[0] + units[m], units[m]);
        }

        // 这里打印一下 可以改成写文件
        System.out.println(res);

        return res;
    }
}  