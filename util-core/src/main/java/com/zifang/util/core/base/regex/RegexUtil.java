package com.zifang.util.core.base.regex;

public class RegexUtil {

    public static String number = "^[0-9]*$";       //0-9的数字
    public static String number_1 = "^\\d{n}$";       //n位的数字
    public static String number_2 = "^\\d{n,}$";       //至少n位的数字
    public static String number_3 = "^\\d{m,n}$";       //m-n位的数字
    public static String number_4 = "^(0|[1-9][0-9]*)$";       //零和非零开头的数字
//    public static String number = "^[0-9]*$";       //0-9的数字

//    1. 数字：^[0-9]*$
//
//2. n位的数字：^\d{n}$
//
//3. 至少n位的数字：^\d{n,}$
//
//4. m-n位的数字：^\d{m,n}$
//
//5. 零和非零开头的数字：^(0|[1-9][0-9]*)$
//
//6. 非零开头的最多带两位小数的数字：^([1-9][0-9]*)+(.[0-9]{1,2})?$
//
//7. 带1-2位小数的正数或负数：^(\-)?\d+(\.\d{1,2})?$
//
//8. 正数、负数、和小数：^(\-|\+)?\d+(\.\d+)?$
//
//9. 有两位小数的正实数：^[0-9]+(.[0-9]{2})?$
//
//10. 有1~3位小数的正实数：^[0-9]+(.[0-9]{1,3})?$
//
//11. 非零的正整数：^[1-9]\d*$ 或 ^([1-9][0-9]*){1,3}$ 或 ^\+?[1-9][0-9]*$
//
//12. 非零的负整数：^\-[1-9][]0-9"*$ 或 ^-[1-9]\d*$
//
//            13. 非负整数：^\d+$ 或 ^[1-9]\d*|0$
//
//14. 非正整数：^-[1-9]\d*|0$ 或 ^((-\d+)|(0+))$
//
//15. 非负浮点数：^\d+(\.\d+)?$ 或 ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$
//
//16. 非正浮点数：^((-\d+(\.\d+)?)|(0+(\.0+)?))$ 或 ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
//
//17. 正浮点数：^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$ 或 ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$
//
//18. 负浮点数：^-([1-9]\d*\.\d*|0\.\d*[1-9]\d*)$ 或 ^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$
//
//19. 浮点数：^(-?\d+)(\.\d+)?$ 或 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$

    public static String chinessWord = "^[\u4e00-\u9fa5]{0,}$";  //汉字

}
