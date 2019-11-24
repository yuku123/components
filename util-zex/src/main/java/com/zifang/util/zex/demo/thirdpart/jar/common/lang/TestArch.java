package com.zifang.util.zex.demo.thirdpart.jar.common.lang;

import org.apache.commons.lang3.ArchUtils;

public class TestArch {

    //ArchUtils 系统的信息导出
    public static void archUtils(){
        System.out.println(ArchUtils.getProcessor().getArch());
        System.out.println(ArchUtils.getProcessor().isX86());
        System.out.println(ArchUtils.getProcessor().getType());
        System.out.println(ArchUtils.getProcessor().isPPC());
    }

    /**
     *
     * 数组工具类
     *
     * add(boolean[] array, boolean element) 将给定的数据添加到指定的数组中，返回一个新的数组
     *
     * ArrayUtils.add(null, true)          = [true]
     * ArrayUtils.add([true], false)       = [true, false]
     * ArrayUtils.add([true, false], true) = [true, false, true]
     * add(boolean[] array, int index, boolean element) 将给定的数据添加到指定的数组下标中，返回一个新的数组。
     *
     * ArrayUtils.add(null, 0, true)          = [true]
     * ArrayUtils.add([true], 0, false)       = [false, true]
     * ArrayUtils.add([false], 1, true)       = [false, true]
     * ArrayUtils.add([true, false], 1, true) = [true, true, false]
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * addAll(boolean[] array1, boolean... array2) 将给定的多个数据添加到指定的数组中，返回一个新的数组
     *
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * clone(boolean[] array) 复制数组并返回 结果数组为空将返回空
     *
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * contains(boolean[] array, boolean valueToFind) 检查该数据在该数组中是否存在，返回一个boolean值
     *
     * byte, int, char, double, float, int, long ,short, Object 同理
     *
     * getLength(Object array) 返回该数组长度
     *
     * ArrayUtils.getLength(null)            = 0
     * ArrayUtils.getLength([])              = 0
     * ArrayUtils.getLength([null])          = 1
     * ArrayUtils.getLength([true, false])   = 2
     * ArrayUtils.getLength([1, 2, 3])       = 3
     * ArrayUtils.getLength(["a", "b", "c"]) = 3
     * hashCode(Object array) 返回该数组的哈希Code码
     *
     * indexOf(boolean[] array, boolean valueToFind) 从数组的第一位开始查询该数组中是否有指定的数值，存在返回index的数值，否则返回-1
     *
     * indexOf(boolean[] array, boolean valueToFind, int startIndex) 从数组的第startIndex位开始查询该数组中是否有指定的数值，存在返回index的数值，否则返回-1
     *
     * byte, int, char, double, float, int, long ,short 同理
     *
     * insert(int index, boolean[] array, boolean... values) 向指定的位置往该数组添加指定的元素，返回一个新的数组
     *
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * isEmpty(boolean[] array) 判断该数组是否为空，返回一个boolean值
     *
     * byte, int, char, double, float, int, long ,short, Object 同理
     *
     * isNotEmpty(boolean[] array) 判断该数组是否为空，而不是null
     *
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * isSameLength(boolean[] array1, boolean[] array2) 判断两个数组的长度是否一样，当数组为空视长度为0。返回一个boolean值
     *
     * isSameType(Object array1, Object array2) 判断两个数组的类型是否一样，返回一个boolean值
     *
     * isSorted(boolean[] array) 判断该数组是否按照自然排列顺序排序，返回一个boolean值
     *
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * isSorted(T[] array, Comparator<T> comparator) 判断该数组是否按照比较器排列顺序排序，返回一个boolean值
     *
     * lastIndexOf(boolean[] array, boolean valueToFind) 从数组的最后一位开始往前查询该数组中是否有指定的数值，存在返回index的数值，否则返回-1
     *
     * lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) 从数组的最后startIndex位开始往前查询该数组中是否有指定的数值，存在返回index的数值，否则返回-1
     *
     * byte, int, char, double, float, int, long ,short, Object 同理
     *
     * nullToEmpty(boolean[] array) 将null转换为空的数组,如果数组不为null,返回原数组,如果数组为null,返回一个空的数组
     *
     * byte, int, char, double, float, int, long ,short, Object, T 同理
     *
     * remove(boolean[] array, int index) 删除该数组指定位置上的元素，返回一个新的数组，所有后续元素左移（下标减1）
     *
     * ArrayUtils.remove([true], 0)              = []
     * ArrayUtils.remove([true, false], 0)       = [false]
     * ArrayUtils.remove([true, false], 1)       = [true]
     * ArrayUtils.remove([true, true, false], 1) = [true, false]
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * removeAll(boolean[] array, int... indices) 删除该数组多个指定位置上的元素，返回一个新的数组，所有后续元素左移（下标减1）
     *
     * ArrayUtils.removeAll([true, false, true], 0, 2) = [false]
     * ArrayUtils.removeAll([true, false, true], 1, 2) = [true]
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * removeAllOccurences(boolean[] array, boolean element) 从该数组中删除指定的元素，返回一个新的数组
     *
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * removeElement(boolean[] array, boolean element) 从该数组中删除指定的元素，返回一个新的数组
     *
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * removeElements(boolean[] array, boolean... values) 从该数组中删除指定数量的元素，返回一个新的数组
     *
     * ArrayUtils.removeElements(null, true, false)               = null
     * ArrayUtils.removeElements([], true, false)                 = []
     * ArrayUtils.removeElements([true], false, false)            = [true]
     * ArrayUtils.removeElements([true, false], true, true)       = [false]
     * ArrayUtils.removeElements([true, false, true], true)       = [false, true]
     * ArrayUtils.removeElements([true, false, true], true, true) = [false]
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * reverse(boolean[] array) 数组反转
     *
     * reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) 数组从指定位置区间进行反转
     *
     * byte, int, char, double, float, int, long ,short, Object 同理
     *
     * shuffle(boolean[] array) 把数组中的元素按随机顺序重新排列
     *
     * byte, int, char, double, float, int, long ,short, Object 同理
     *
     * subarray(boolean[] array, int startIndexInclusive, int endIndexExclusive) 截取数组，按指定位置区间截取并返回一个新的数组
     *
     * byte, int, char, double, float, int, long ,short, T[] 同理
     *
     * swap(boolean[] array, int offset1, int offset2) 指定该数组的两个位置的元素交换进行交换
     *
     * ArrayUtils.swap([1, 2, 3], 0, 2) -> [3, 2, 1]
     * ArrayUtils.swap([1, 2, 3], 0, 0) -> [1, 2, 3]
     * ArrayUtils.swap([1, 2, 3], 1, 0) -> [2, 1, 3]
     * ArrayUtils.swap([1, 2, 3], 0, 5) -> [1, 2, 3]
     * ArrayUtils.swap([1, 2, 3], -1, 1) -> [2, 1, 3]
     * byte, int, char, double, float, int, long ,short, Object 同理
     *
     * toArray(T... items) 创建数组
     *
     * String[] array = ArrayUtils.toArray("1", "2");
     * String[] emptyArray = ArrayUtils.<String>toArray();
     * toMap(Object[] array) 将二维数组转换成Map并返会Map
     *
     * Map colorMap = ArrayUtils.toMap(new String[][] {
     *     {"RED", "#FF0000"},
     *     {"GREEN", "#00FF00"},
     *     {"BLUE", "#0000FF"}}
     * );
     * toObject(boolean[] array) 将基本类型数组转换成对象类型数组并返回
     *
     * byte, int, char, double, float, int, long ,short 同理
     *
     * toPrimitive(Boolean[] array) 将对象类型数组转换成基本类型数组并返回
     *
     * byte, int, char, double, float, int, long ,short 同理
     *
     * toString(Object array) 将数组转换为string字符串并返回
     *
     * toStringArray(Object[] array) 将Object数组转换为String数组类型
     * */
    public static void arrayUtils(){

    }

    /**
     *
     * 三、BooleanUtils
     * 布尔工具类
     *
     * and(boolean... array) 逻辑与
     *
     * BooleanUtils.and(true, true)         = true
     * BooleanUtils.and(false, false)       = false
     * BooleanUtils.and(true, false)        = false
     * BooleanUtils.and(true, true, false)  = false
     * BooleanUtils.and(true, true, true)   = true
     * compare(boolean x, boolean y) 比较两个布尔值并返回int类型 如果x == y返回0， !x && y 返回小于 0 ，x && !y 返回大于0
     *
     * isFalse(Boolean bool) 是否是假并返回boolean
     *
     * isTrue(Boolean bool) 是否是真并返回boolean
     *
     * negate(Boolean bool) 逻辑非
     *
     * BooleanUtils.negate(Boolean.TRUE)  = Boolean.FALSE;
     * BooleanUtils.negate(Boolean.FALSE) = Boolean.TRUE;
     * BooleanUtils.negate(null)          = null;
     * or(boolean... array) 逻辑或
     *
     * BooleanUtils.or(true, true)          = true
     * BooleanUtils.or(false, false)        = false
     * BooleanUtils.or(true, false)         = true
     * BooleanUtils.or(true, true, false)   = true
     * BooleanUtils.or(true, true, true)    = true
     * BooleanUtils.or(false, false, false) = false
     * toBoolean(Boolean bool) 将对象类型转换为基本数据类型并返回
     *
     * BooleanUtils.toBoolean(Boolean.TRUE)  = true
     * BooleanUtils.toBoolean(Boolean.FALSE) = false
     * BooleanUtils.toBoolean(null)          = false
     * toBoolean(int value) 将int类型转换为boolean类型并返回
     *
     * BooleanUtils.toBoolean(0) = false
     * BooleanUtils.toBoolean(1) = true
     * BooleanUtils.toBoolean(2) = true
     * toBoolean(String str) 将string类型转换为boolean类型并返回
     *
     * BooleanUtils.toBoolean(null)    = false
     * BooleanUtils.toBoolean("true")  = true
     * BooleanUtils.toBoolean("TRUE")  = true
     * BooleanUtils.toBoolean("tRUe")  = true
     * BooleanUtils.toBoolean("on")    = true
     * BooleanUtils.toBoolean("yes")   = true
     * BooleanUtils.toBoolean("false") = false
     * BooleanUtils.toBoolean("x gti") = false
     * BooleanUtils.toBooleanObject("y") = true
     * BooleanUtils.toBooleanObject("n") = false
     * BooleanUtils.toBooleanObject("t") = true
     * BooleanUtils.toBooleanObject("f") = false
     * toInteger(boolean bool) 将boolean类型数据转换为int类型并返回
     *
     * BooleanUtils.toInteger(true)  = 1
     * BooleanUtils.toInteger(false) = 0
     * toStringOnOff(boolean bool) 将boolean类型数据转换为String类型'on' or 'off'并返回
     *
     * BooleanUtils.toStringOnOff(true)   = "on"
     * BooleanUtils.toStringOnOff(false)  = "off"
     * toStringTrueFalse(Boolean bool) 将boolean类型数据转换为String类型''true' or 'false'并返回
     *
     * BooleanUtils.toStringTrueFalse(true)   = "true"
     * BooleanUtils.toStringTrueFalse(false)  = "false"
     * toStringYesNo(boolean bool) 将boolean类型数据转换为String类型'yes' or 'no'并返回
     *
     * BooleanUtils.toStringYesNo(true)   = "yes"
     * BooleanUtils.toStringYesNo(false)  = "no"
     * xor(boolean... array) 异或
     *
     * BooleanUtils.xor(true, true)   = false
     * BooleanUtils.xor(false, false) = false
     * BooleanUtils.xor(true, false)  = true
     * 四、ClassPathUtils
     * class路径工具
     *
     * toFullyQualifiedName(Class<?> context, String resourceName) 返回一个由class包名+resourceName拼接的字符串
     *
     * ClassPathUtils.toFullyQualifiedName(StringUtils.class, "StringUtils.properties") = "org.apache.commons.lang3.StringUtils.properties"
     * toFullyQualifiedName(Package context, String resourceName) 返回一个由class包名+resourceName拼接的字符串
     *
     * ClassPathUtils.toFullyQualifiedName(StringUtils.class.getPackage(), "StringUtils.properties") = "org.apache.commons.lang3.StringUtils.properties"
     * toFullyQualifiedPath(Class<?> context, String resourceName) 返回一个由class包名+resourceName拼接的字符串
     *
     * ClassPathUtils.toFullyQualifiedPath(StringUtils.class, "StringUtils.properties") = "org/apache/commons/lang3/StringUtils.properties"
     * toFullyQualifiedPath(Package context, String resourceName) 返回一个由class包名+resourceName拼接的字符串
     *
     * ClassPathUtils.toFullyQualifiedPath(StringUtils.class, "StringUtils.properties") = "org/apache/commons/lang3/StringUtils.properties"
     * 五、EnumUtils
     * 枚举工具类
     *
     * getEnum(Class<E> enumClass, String enumName) 通过类返回一个枚举，可能返回空
     *
     * getEnumList(Class<E> enumClass) 通过类返回一个枚举集合
     *
     * getEnumMap(Class<E> enumClass) 通过类返回一个枚举map
     *
     * isValidEnum(Class<E> enumClass, String enumName) 验证enumName是否在枚举中，返回true false
     *
     * demo
     *
     * 枚举类
     * public enum EnumDemo {
     *     AA("1"), BB("2");
     *     private String value;
     *
     *     EnumDemo(String value) {
     *         this.value = value;
     *     }
     *
     *     public String getValue() {
     *         return value;
     *     }
     * }
     *
     * 测试
     * EnumDemo enumDemo = EnumUtils.getEnum(EnumDemo.class, "");
     * System.out.println(enumDemo);
     * System.out.println("-----");
     *
     * List<EnumDemo> list = EnumUtils.getEnumList(EnumDemo.class);
     * for (EnumDemo a : list) {
     *     System.out.println(a + ":" + a.getValue());
     * }
     * System.out.println("-----");
     *
     * Map<String, EnumDemo> enumMap = EnumUtils.
     *
     *
     * */



    /**
     * 六、ObjectUtils
     * Object工具类
     *
     * allNotNull(Object... values) 检查所有元素是否为空,返回一个boolean
     *
     * 如果有一个元素为空返回false，所有元素不为空或元素为empty返回true
     *
     * ObjectUtils.allNotNull(*)             = true
     * ObjectUtils.allNotNull(*, *)          = true
     * ObjectUtils.allNotNull(null)          = false
     * ObjectUtils.allNotNull(null, null)    = false
     * ObjectUtils.allNotNull(null, *)       = false
     * ObjectUtils.allNotNull(*, null)       = false
     * ObjectUtils.allNotNull(*, *, null, *) = false
     * anyNotNull(Object... values) 检查元素是否为空,返回一个boolean
     *
     * 如果有一个元素不为空返回true
     *
     * ObjectUtils.anyNotNull(*)                = true
     * ObjectUtils.anyNotNull(*, null)          = true
     * ObjectUtils.anyNotNull(null, *)          = true
     * ObjectUtils.anyNotNull(null, null, *, *) = true
     * ObjectUtils.anyNotNull(null)             = false
     * ObjectUtils.anyNotNull(null, null)       = false
     * clone(T obj) 拷贝一个对象并返回
     *
     * compare(T c1, T c2) 比较两个对象,返回一个int值
     *
     * defaultIfNull(T object, T defaultValue) 如果对象为空返回一个默认值
     *
     * firstNonNull(T... values) 返回数组中第一个不为空的值
     *
     * notEqual(Object object1, Object object2) 判断两个对象不相等，返回一个boolean
     *
     * 七、RandomUtils
     * 随机工具类
     *
     * nextBoolean() 返回一个随机boolean值
     *
     * nextBytes(int count) 返回一个指定大小的随机byte数组
     *
     * nextDouble() 返回一个随机double值
     *
     * nextDouble(double startInclusive, double endInclusive) 返回一个指定范围的随机double值
     *
     * nextFloat() 返回一个随机float值
     *
     * nextFloat(float startInclusive, float endInclusive) 返回一个指定范围的随机float值
     *
     * nextInt() 返回一个随机int值
     *
     * nextInt(int startInclusive, int endExclusive) 返回一个指定范围的随机int值
     *
     * nextLong() 返回一个随机long值
     *
     * nextLong(long startInclusive, long endExclusive) 返回一个指定范围的随机long值
     *
     * 八、SystemUtils
     * 操作系统工具类
     *
     * FILE_ENCODING 返回系统编码
     *
     * IS_JAVA_1_1、...、IS_JAVA_1_8、IS_JAVA_10、IS_JAVA_9 判断java版本,返回一个boolean
     *
     * IS_OS_LINUX 判断系统是否是linux,返回一个boolean
     *
     * IS_OS_MAC 判断系统是否是mac,返回一个boolean
     *
     * IS_OS_WINDOWS、IS_OS_WINDOWS_10、 IS_OS_WINDOWS_2000、IS_OS_WINDOWS_2003、IS_OS_WINDOWS_2008、IS_OS_WINDOWS_7、 IS_OS_WINDOWS_8、 IS_OS_WINDOWS_95、 IS_OS_WINDOWS_98、 IS_OS_WINDOWS_XP 判断系统是否是windows,返回一个boolean
     *
     * JAVA_CLASS_PATH 返回系统CLASS_PATH值
     *
     * JAVA_CLASS_VERSION 返回系统java版本
     *
     * JAVA_HOME 返回系统java home
     *
     * JAVA_RUNTIME_VERSION 返回java运行版本
     *
     * JAVA_VERSION 返回java版本
     *
     * OS_NAME 返回系统名
     *
     * OS_VERSION 返回系统版本
     *
     * USER_COUNTRY 返回用户国家编号
     *
     * USER_DIR 返回项目文件夹
     *
     * USER_HOME 返回系统用户主文件夹
     *
     * USER_LANGUAGE 返回系统用户语言
     *
     * USER_NAME 返回系统用户名
     *
     * 九、StringUtils
     * 字符串工具类
     *
     * abbreviate(String str, int maxWidth) 返回一个指定长度加省略号的字符串，maxWidth必须大于3
     *
     * StringUtils.abbreviate(null, *)      = null
     * StringUtils.abbreviate("", 4)        = ""
     * StringUtils.abbreviate("abcdefg", 6) = "abc..."
     * StringUtils.abbreviate("abcdefg", 7) = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 8) = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 4) = "a..."
     * StringUtils.abbreviate("abcdefg", 3) = IllegalArgumentException
     * abbreviate(String str, int offset, int maxWidth) 返回一个指定长度加省略号的字符串，maxWidth必须大于3
     *
     * abbreviate(String str, String abbrevMarker, int maxWidth) 返回一个自定义省略号的指定长度字符串，maxWidth必须大于3
     *
     * StringUtils.abbreviate(null, "...", *)      = null
     * StringUtils.abbreviate("abcdefg", null, *)  = "abcdefg"
     * StringUtils.abbreviate("", "...", 4)        = ""
     * StringUtils.abbreviate("abcdefg", ".", 5)   = "abcd."
     * StringUtils.abbreviate("abcdefg", ".", 7)   = "abcdefg"
     * StringUtils.abbreviate("abcdefg", ".", 8)   = "abcdefg"
     * StringUtils.abbreviate("abcdefg", "..", 4)  = "ab.."
     * StringUtils.abbreviate("abcdefg", "..", 3)  = "a.."
     * StringUtils.abbreviate("abcdefg", "..", 2)  = IllegalArgumentException
     * StringUtils.abbreviate("abcdefg", "...", 3) = IllegalArgumentException
     * abbreviateMiddle(String str, String abbrevMarker, int maxWidth) 将字符串缩短到指定长度（length），字符串的中间部分用替换字符串（middle）显示
     *
     * StringUtils.abbreviateMiddle("abc", null, 0)      = "abc"
     * StringUtils.abbreviateMiddle("abc", ".", 0)      = "abc"
     * StringUtils.abbreviateMiddle("abc", ".", 3)      = "abc"
     * StringUtils.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
     * appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) 如果str不是以任何suffixes结尾，将字符串suffix拼接到字符串str后面
     *
     * StringUtils.appendIfMissing(null, null) = null
     * StringUtils.appendIfMissing("abc", null) = "abc"
     * StringUtils.appendIfMissing("", "xyz") = "xyz"
     * StringUtils.appendIfMissing("abc", "xyz") = "abcxyz"
     * StringUtils.appendIfMissing("abcxyz", "xyz") = "abcxyz"
     * StringUtils.appendIfMissing("abcXYZ", "xyz") = "abcXYZxyz"
     * appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) 同上 不区分大小写
     *
     * capitalize(String str) 将字符串第一个字符大写并返回
     *
     * center(String str, int size) 用空格字符填充使字符串str位于长度为size的大字符串中间
     *
     * StringUtils.center(null, *)   = null
     * StringUtils.center("", 4)     = "    "
     * StringUtils.center("ab", -1)  = "ab"
     * StringUtils.center("ab", 4)   = " ab "
     * StringUtils.center("abcd", 2) = "abcd"
     * StringUtils.center("a", 4)    = " a  "
     * center(String str, int size, char padChar) 用指定字符填充使字符串str位于长度为size的大字符串中间
     *
     * chomp(String str) 删除字符串末尾的一个换行符,返回一个新的字符串（换行符"n", "r", or "rn"）
     *
     * StringUtils.chomp(null)          = null
     * StringUtils.chomp("")            = ""
     * StringUtils.chomp("abc \r")      = "abc "
     * StringUtils.chomp("abc\n")       = "abc"
     * StringUtils.chomp("abc\r\n")     = "abc"
     * StringUtils.chomp("abc\r\n\r\n") = "abc\r\n"
     * StringUtils.chomp("abc\n\r")     = "abc\n"
     * StringUtils.chomp("abc\n\rabc")  = "abc\n\rabc"
     * StringUtils.chomp("\r")          = ""
     * StringUtils.chomp("\n")          = ""
     * StringUtils.chomp("\r\n")        = ""
     * chop(String str) 删除字符串末尾的一个字符，返回一个新的字符串
     *
     * StringUtils.chop(null)          = null
     * StringUtils.chop("")            = ""
     * StringUtils.chop("abc \r")      = "abc "
     * StringUtils.chop("abc\n")       = "abc"
     * StringUtils.chop("abc\r\n")     = "abc"
     * StringUtils.chop("abc")         = "ab"
     * StringUtils.chop("abc\nabc")    = "abc\nab"
     * StringUtils.chop("a")           = ""
     * StringUtils.chop("\r")          = ""
     * StringUtils.chop("\n")          = ""
     * StringUtils.chop("\r\n")        = ""
     * compare(String str1, String str2) 比较两个字符串，返回一个int值
     *
     * str1等于str2（或都为空）返回0
     * str1小于str2返回小于0
     * str1大于str2返回大于0
     *
     * StringUtils.compare(null, null)   = 0
     * StringUtils.compare(null , "a")   < 0
     * StringUtils.compare("a", null)    > 0
     * StringUtils.compare("abc", "abc") = 0
     * StringUtils.compare("a", "b")     < 0
     * StringUtils.compare("b", "a")     > 0
     * StringUtils.compare("a", "B")     > 0
     * StringUtils.compare("ab", "abc")  < 0
     * contains(CharSequence seq, CharSequence searchSeq) 检查字符串中是否包含指定字符，返回boolean
     *
     * StringUtils.contains(null, *)     = false
     * StringUtils.contains(*, null)     = false
     * StringUtils.contains("", "")      = true
     * StringUtils.contains("abc", "")   = true
     * StringUtils.contains("abc", "a")  = true
     * StringUtils.contains("abc", "z")  = false
     * containsAny(CharSequence cs, CharSequence... searchCharSequences) 检查字符串中是否包含任一字符，返回boolean
     *
     * StringUtils.containsAny(null, *)            = false
     * StringUtils.containsAny("", *)              = false
     * StringUtils.containsAny(*, null)            = false
     * StringUtils.containsAny(*, [])              = false
     * StringUtils.containsAny("abcd", "ab", null) = true
     * StringUtils.containsAny("abcd", "ab", "cd") = true
     * StringUtils.containsAny("abc", "d", "abc")  = true
     * containsNone(CharSequence cs, String invalidChars) 检查字符串不包含指定字符，返回boolean
     *
     * StringUtils.containsNone(null, *)       = true
     * StringUtils.containsNone(*, null)       = true
     * StringUtils.containsNone("", *)         = true
     * StringUtils.containsNone("ab", "")      = true
     * StringUtils.containsNone("abab", "xyz") = true
     * StringUtils.containsNone("ab1", "xyz")  = true
     * StringUtils.containsNone("abz", "xyz")  = false
     * containsOnly(CharSequence cs, String validChars) 检查字符串只包含特定的字符，返回boolean
     *
     * StringUtils.containsOnly(null, *)       = false
     * StringUtils.containsOnly(*, null)       = false
     * StringUtils.containsOnly("", *)         = true
     * StringUtils.containsOnly("ab", "")      = false
     * StringUtils.containsOnly("abab", "abc") = true
     * StringUtils.containsOnly("ab1", "abc")  = false
     * StringUtils.containsOnly("abz", "abc")  = false
     * containsWhitespace(CharSequence seq) 检查字符串中是否包含空格字符，返回boolean
     *
     * countMatches(CharSequence str, CharSequence sub) 检查字符串中出现指定字符的次数，返回一个int值
     *
     * StringUtils.countMatches(null, *)       = 0
     * StringUtils.countMatches("", *)         = 0
     * StringUtils.countMatches("abba", null)  = 0
     * StringUtils.countMatches("abba", "")    = 0
     * StringUtils.countMatches("abba", "a")   = 2
     * StringUtils.countMatches("abba", "ab")  = 1
     * StringUtils.countMatches("abba", "xxx") = 0
     * defaultIfBlank(T str, T defaultStr) 如果字符串为null、空（""），或全是空格，将返回指定字符串，否则返回原值
     *
     * StringUtils.defaultIfBlank(null, "NULL")  = "NULL"
     * StringUtils.defaultIfBlank("", "NULL")    = "NULL"
     * StringUtils.defaultIfBlank(" ", "NULL")   = "NULL"
     * StringUtils.defaultIfBlank("bat", "NULL") = "bat"
     * StringUtils.defaultIfBlank("", null)      = null
     * defaultIfEmpty(T str, T defaultStr) 如果字符串为null、空（""），将返回指定字符串，否则返回原值
     *
     * StringUtils.defaultIfEmpty(null, "NULL")  = "NULL"
     * StringUtils.defaultIfEmpty("", "NULL")    = "NULL"
     * StringUtils.defaultIfEmpty(" ", "NULL")   = " "
     * StringUtils.defaultIfEmpty("bat", "NULL") = "bat"
     * StringUtils.defaultIfEmpty("", null)      = null
     * defaultString(String str) 如果字符串为null，将返回空的字符串（""），否则返回原值
     *
     * StringUtils.defaultString(null)  = ""
     * StringUtils.defaultString("")    = ""
     * StringUtils.defaultString("bat") = "bat"
     * defaultString(String str, String defaultStr) 如果字符串为null，将返回指定字符，否则返回原值
     *
     * StringUtils.defaultString(null, "NULL")  = "NULL"
     * StringUtils.defaultString("", "NULL")    = ""
     * StringUtils.defaultString("bat", "NULL") = "bat"
     * deleteWhitespace(String str) 删除字符串中的空格字符，并返回新的字符串
     *
     * StringUtils.deleteWhitespace(null)         = null
     * StringUtils.deleteWhitespace("")           = ""
     * StringUtils.deleteWhitespace("abc")        = "abc"
     * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
     * difference(String str1, String str2) 比较两个字符串差异，并返回差异的字符，返回第二个字符串的剩余部分，这意味着“ABC”和“AB”之间的区别是空字符串而不是“C”。
     *
     * StringUtils.difference(null, null) = null
     * StringUtils.difference("", "") = ""
     * StringUtils.difference("", "abc") = "abc"
     * StringUtils.difference("abc", "") = ""
     * StringUtils.difference("abc", "abc") = ""
     * StringUtils.difference("abc", "ab") = ""
     * StringUtils.difference("ab", "abxyz") = "xyz"
     * StringUtils.difference("abcde", "abxyz") = "xyz"
     * StringUtils.difference("abcde", "xyz") = "xyz"
     * endsWith(CharSequence str, CharSequence suffix) 检查字符串是否以指定字符结尾，返回一个boolean
     *
     * StringUtils.endsWith(null, null)      = true
     * StringUtils.endsWith(null, "def")     = false
     * StringUtils.endsWith("abcdef", null)  = false
     * StringUtils.endsWith("abcdef", "def") = true
     * StringUtils.endsWith("ABCDEF", "def") = false
     * StringUtils.endsWith("ABCDEF", "cde") = false
     * StringUtils.endsWith("ABCDEF", "")    = true
     * endsWithAny(CharSequence sequence, CharSequence... searchStrings) 检查字符串是否以指定字符数组结尾，返回一个boolean
     *
     * StringUtils.endsWithAny(null, null)      = false
     * StringUtils.endsWithAny(null, new String[] {"abc"})  = false
     * StringUtils.endsWithAny("abcxyz", null)     = false
     * StringUtils.endsWithAny("abcxyz", new String[] {""}) = true
     * StringUtils.endsWithAny("abcxyz", new String[] {"xyz"}) = true
     * StringUtils.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
     * StringUtils.endsWithAny("abcXYZ", "def", "XYZ") = true
     * StringUtils.endsWithAny("abcXYZ", "def", "xyz") = false
     * endsWithIgnoreCase(CharSequence str, CharSequence suffix) 检查字符串是否以指定字符（不区分大小写）结尾，返回一个boolean
     *
     * StringUtils.endsWithIgnoreCase(null, null)      = true
     * StringUtils.endsWithIgnoreCase(null, "def")     = false
     * StringUtils.endsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.endsWithIgnoreCase("abcdef", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
     * equals(CharSequence cs1, CharSequence cs2) 比较两个字符串是否相等，返回一个boolean
     *
     * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
     * equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) 比较两个字符串是否相等（不区分大小写），返回一个boolean
     *
     * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
     * equalsAny(CharSequence string, CharSequence... searchStrings) 比较字符串是否与指定字符串数组中某一值相等，返回一个boolean
     *
     * StringUtils.equalsAny(null, (CharSequence[]) null) = false
     * StringUtils.equalsAny(null, null, null)    = true
     * StringUtils.equalsAny(null, "abc", "def")  = false
     * StringUtils.equalsAny("abc", null, "def")  = false
     * StringUtils.equalsAny("abc", "abc", "def") = true
     * StringUtils.equalsAny("abc", "ABC", "DEF") = false
     * equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) 比较字符串是否与指定字符串数组中某一值相等（不区分大小写），返回一个boolean
     *
     * StringUtils.equalsAnyIgnoreCase(null, (CharSequence[]) null) = false
     * StringUtils.equalsAnyIgnoreCase(null, null, null)    = true
     * StringUtils.equalsAnyIgnoreCase(null, "abc", "def")  = false
     * StringUtils.equalsAnyIgnoreCase("abc", null, "def")  = false
     * StringUtils.equalsAnyIgnoreCase("abc", "abc", "def") = true
     * StringUtils.equalsAnyIgnoreCase("abc", "ABC", "DEF") = true
     * getCommonPrefix(String... strs) 获取字符串数组元素公共字符，返回string
     *
     * StringUtils.getCommonPrefix(null) = ""
     * StringUtils.getCommonPrefix(new String[] {}) = ""
     * StringUtils.getCommonPrefix(new String[] {"abc"}) = "abc"
     * StringUtils.getCommonPrefix(new String[] {null, null}) = ""
     * StringUtils.getCommonPrefix(new String[] {"", ""}) = ""
     * StringUtils.getCommonPrefix(new String[] {"", null}) = ""
     * StringUtils.getCommonPrefix(new String[] {"abc", null, null}) = ""
     * StringUtils.getCommonPrefix(new String[] {null, null, "abc"}) = ""
     * StringUtils.getCommonPrefix(new String[] {"", "abc"}) = ""
     * StringUtils.getCommonPrefix(new String[] {"abc", ""}) = ""
     * StringUtils.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
     * StringUtils.getCommonPrefix(new String[] {"abc", "a"}) = "a"
     * StringUtils.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
     * StringUtils.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
     * StringUtils.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
     * StringUtils.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
     * StringUtils.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
     * indexOf(CharSequence seq, CharSequence searchSeq) 检查指定字符在字符串中出现的位置，返回一个int值
     *
     * StringUtils.indexOf(null, *)          = -1
     * StringUtils.indexOf(*, null)          = -1
     * StringUtils.indexOf("", "")           = 0
     * StringUtils.indexOf("", *)            = -1 (except when * = "")
     * StringUtils.indexOf("aabaabaa", "a")  = 0
     * StringUtils.indexOf("aabaabaa", "b")  = 2
     * StringUtils.indexOf("aabaabaa", "ab") = 1
     * StringUtils.indexOf("aabaabaa", "")   = 0
     * indexOfIgnoreCase(CharSequence seq, CharSequence searchSeq) 检查指定字符在字符串中出现的位置（不区分大小写），返回一个int值
     *
     * isAllBlank(CharSequence... css) 检查数组所有字符是否为null、empty、或全是空格字符，返回一个boolean
     *
     * StringUtils.isAllBlank(null)             = true
     * StringUtils.isAllBlank(null, "foo")      = false
     * StringUtils.isAllBlank(null, null)       = true
     * StringUtils.isAllBlank("", "bar")        = false
     * StringUtils.isAllBlank("bob", "")        = false
     * StringUtils.isAllBlank("  bob  ", null)  = false
     * StringUtils.isAllBlank(" ", "bar")       = false
     * StringUtils.isAllBlank("foo", "bar")     = false
     * StringUtils.isAllBlank(new String[] {})  = true
     * isAllEmpty(CharSequence... css) 检查数组所有字符是否为null、empty，返回一个boolean
     *
     * StringUtils.isAllEmpty(null)             = true
     * StringUtils.isAllEmpty(null, "")         = true
     * StringUtils.isAllEmpty(new String[] {})  = true
     * StringUtils.isAllEmpty(null, "foo")      = false
     * StringUtils.isAllEmpty("", "bar")        = false
     * StringUtils.isAllEmpty("bob", "")        = false
     * StringUtils.isAllEmpty("  bob  ", null)  = false
     * StringUtils.isAllEmpty(" ", "bar")       = false
     * StringUtils.isAllEmpty("foo", "bar")     = false
     * isAllLowerCase(CharSequence cs) 检查字符串中所有字符是否是小写，返回一个boolean
     *
     * isAllUpperCase(CharSequence cs) 检查字符串中所有字符是否是大写，返回一个boolean
     *
     * isAnyBlank(CharSequence... css) 检查数组中字符串是否有一个为null、empty或全是空格字符，返回一个boolean
     *
     * StringUtils.isAnyBlank(null)             = true
     * StringUtils.isAnyBlank(null, "foo")      = true
     * StringUtils.isAnyBlank(null, null)       = true
     * StringUtils.isAnyBlank("", "bar")        = true
     * StringUtils.isAnyBlank("bob", "")        = true
     * StringUtils.isAnyBlank("  bob  ", null)  = true
     * StringUtils.isAnyBlank(" ", "bar")       = true
     * StringUtils.isAnyBlank(new String[] {})  = false
     * StringUtils.isAnyBlank(new String[]{""}) = true
     * StringUtils.isAnyBlank("foo", "bar")     = false
     * isAnyEmpty(CharSequence... css) 检查数组中字符串是否有一个为null、empty，返回一个boolean
     *
     * StringUtils.isAnyEmpty(null)             = true
     * StringUtils.isAnyEmpty(null, "foo")      = true
     * StringUtils.isAnyEmpty("", "bar")        = true
     * StringUtils.isAnyEmpty("bob", "")        = true
     * StringUtils.isAnyEmpty("  bob  ", null)  = true
     * StringUtils.isAnyEmpty(" ", "bar")       = false
     * StringUtils.isAnyEmpty("foo", "bar")     = false
     * StringUtils.isAnyEmpty(new String[]{})   = false
     * StringUtils.isAnyEmpty(new String[]{""}) = true
     * isBlank(CharSequence cs) 检查字符串是否为null、empty或空格字符，返回一个boolean
     *
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * isEmpty(CharSequence cs) 检查字符串是否为null、empty，返回一个boolean
     *
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * isNotBlank(CharSequence cs) 检查字符串是否不为null、empty或空格字符，返回一个boolean
     *
     * isNotEmpty(CharSequence cs) 检查字符串是否不为null、empty，返回一个boolean
     *
     * isNumeric(CharSequence cs) 检查字符串是否是数字，返回一个boolean
     *
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = false
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("१२३")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
     * StringUtils.isNumeric("-123") = false
     * StringUtils.isNumeric("+123") = false
     * isWhitespace(CharSequence cs) 检查字符串是否是空格字符，返回一个boolean
     *
     * StringUtils.isWhitespace(null)   = false
     * StringUtils.isWhitespace("")     = true
     * StringUtils.isWhitespace("  ")   = true
     * StringUtils.isWhitespace("abc")  = false
     * StringUtils.isWhitespace("ab2c") = false
     * StringUtils.isWhitespace("ab-c") = false
     * join(byte[] array, char separator) 将字节数组转换成string，以指定字符分隔
     *
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
     * char、double、float、int、long、short、object、T同理
     *
     * joinWith(String separator, Object... objects) 将多个元素已指定字符分隔拼接成String
     *
     * StringUtils.joinWith(",", {"a", "b"})        = "a,b"
     * StringUtils.joinWith(",", {"a", "b",""})     = "a,b,"
     * StringUtils.joinWith(",", {"a", null, "b"})  = "a,,b"
     * StringUtils.joinWith(null, {"a", "b"})       = "ab"
     * lastIndexOf(CharSequence seq, CharSequence searchSeq) 获取指定字符在字符串中的最后一个索引位置
     *
     * StringUtils.lastIndexOf(null, *)          = -1
     * StringUtils.lastIndexOf(*, null)          = -1
     * StringUtils.lastIndexOf("", "")           = 0
     * StringUtils.lastIndexOf("aabaabaa", "a")  = 7
     * StringUtils.lastIndexOf("aabaabaa", "b")  = 5
     * StringUtils.lastIndexOf("aabaabaa", "ab") = 4
     * StringUtils.lastIndexOf("aabaabaa", "")   = 8
     * left(String str, int len) 返回从左边开始指定大小的字符串
     *
     * StringUtils.left(null, *)    = null
     * StringUtils.left(*, -ve)     = ""
     * StringUtils.left("", *)      = ""
     * StringUtils.left("abc", 0)   = ""
     * StringUtils.left("abc", 2)   = "ab"
     * StringUtils.left("abc", 4)   = "abc"
     * right(String str, int len) 同上相反
     *
     * length(CharSequence cs) 获取字符串大小，返回一个int
     *
     * lowerCase(String str) 将字符串转换为小写，返回一个string
     *
     * StringUtils.lowerCase(null)  = null
     * StringUtils.lowerCase("")    = ""
     * StringUtils.lowerCase("aBc") = "abc"
     * upperCase(String str) 同上相反
     *
     * mid(String str, int pos, int len) 获取字符串指定位置区间的字符，返回一个string
     *
     * StringUtils.mid(null, *, *)    = null
     * StringUtils.mid(*, *, -ve)     = ""
     * StringUtils.mid("", 0, *)      = ""
     * StringUtils.mid("abc", 0, 2)   = "ab"
     * StringUtils.mid("abc", 0, 4)   = "abc"
     * StringUtils.mid("abc", 2, 4)   = "c"
     * StringUtils.mid("abc", 4, 2)   = ""
     * StringUtils.mid("abc", -2, 2)  = "ab"
     * overlay(String str, String overlay, int start, int end) 在字符串位置区间插入指定字符，返回一个string
     *
     * StringUtils.overlay(null, *, *, *)            = null
     * StringUtils.overlay("", "abc", 0, 0)          = "abc"
     * StringUtils.overlay("abcdef", null, 2, 4)     = "abef"
     * StringUtils.overlay("abcdef", "", 2, 4)       = "abef"
     * StringUtils.overlay("abcdef", "", 4, 2)       = "abef"
     * StringUtils.overlay("abcdef", "zzzz", 2, 4)   = "abzzzzef"
     * StringUtils.overlay("abcdef", "zzzz", 4, 2)   = "abzzzzef"
     * StringUtils.overlay("abcdef", "zzzz", -1, 4)  = "zzzzef"
     * StringUtils.overlay("abcdef", "zzzz", 2, 8)   = "abzzzz"
     * StringUtils.overlay("abcdef", "zzzz", -2, -3) = "zzzzabcdef"
     * StringUtils.overlay("abcdef", "zzzz", 8, 10)  = "abcdefzzzz"
     * prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) 在字符串最左边插入指定字符，如果已存在，将不插入，返回一个string
     *
     * StringUtils.prependIfMissing(null, null) = null
     * StringUtils.prependIfMissing("abc", null) = "abc"
     * StringUtils.prependIfMissing("", "xyz") = "xyz"
     * StringUtils.prependIfMissing("abc", "xyz") = "xyzabc"
     * StringUtils.prependIfMissing("xyzabc", "xyz") = "xyzabc"
     * StringUtils.prependIfMissing("XYZabc", "xyz") = "xyzXYZabc"
     * prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) 同上，只是不区分大小写
     *
     * remove(String str, char remove) 删除字符串中指定字符，返回一个string
     *
     * StringUtils.remove(null, *)       = null
     * StringUtils.remove("", *)         = ""
     * StringUtils.remove("queued", 'u') = "qeed"
     * StringUtils.remove("queued", 'z') = "queued"
     * removeIgnoreCase(String str, String remove) 同上，只是不区分大小写
     *
     * removeAll(String text, String regex) 根据匹配规则删除所有字符，返回一个string
     *
     * StringUtils.removeAll(null, *)      = null
     * StringUtils.removeAll("any", null)  = "any"
     * StringUtils.removeAll("any", "")    = "any"
     * StringUtils.removeAll("any", ".*")  = ""
     * StringUtils.removeAll("any", ".+")  = ""
     * StringUtils.removeAll("abc", ".?")  = ""
     * StringUtils.removeAll("A<__>\n<__>B", "<.*>")      = "A\nB"
     * StringUtils.removeAll("A<__>\n<__>B", "(?s)<.*>")  = "AB"
     * StringUtils.removeAll("ABCabc123abc", "[a-z]")     = "ABC123"
     * removeEnd(String str, String remove) 删除字符串结尾指定字符，返回一个string
     *
     * StringUtils.removeEnd(null, *)      = null
     * StringUtils.removeEnd("", *)        = ""
     * StringUtils.removeEnd(*, null)      = *
     * StringUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
     * StringUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
     * StringUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeEnd("abc", "")    = "abc"
     * removeStart(String str, String remove) 同上相反
     *
     * removeEndIgnoreCase(String str, String remove) 同上，只是不区分大小写
     *
     * removeFirst(String text, String regex) 根据匹配规则删除第一次出现的字符，返回一个string
     *
     * StringUtils.removeFirst(null, *)      = null
     * StringUtils.removeFirst("any", null)  = "any"
     * StringUtils.removeFirst("any", "")    = "any"
     * StringUtils.removeFirst("any", ".*")  = ""
     * StringUtils.removeFirst("any", ".+")  = ""
     * StringUtils.removeFirst("abc", ".?")  = "bc"
     * StringUtils.removeFirst("A<__>\n<__>B", "<.*>")      = "A\n<__>B"
     * StringUtils.removeFirst("A<__>\n<__>B", "(?s)<.*>")  = "AB"
     * StringUtils.removeFirst("ABCabc123", "[a-z]")          = "ABCbc123"
     * StringUtils.removeFirst("ABCabc123abc", "[a-z]+")      = "ABC123abc"
     * repeat(String str, int repeat) 将字符重复指定次数拼接成新的字符串，返回一个string
     *
     * StringUtils.repeat(null, 2) = null
     * StringUtils.repeat("", 0)   = ""
     * StringUtils.repeat("", 2)   = ""
     * StringUtils.repeat("a", 3)  = "aaa"
     * StringUtils.repeat("ab", 2) = "abab"
     * StringUtils.repeat("a", -2) = ""
     * replace(String text, String searchString, String replacement) 用replacement替换字符串中的所有searchString，返回一个string
     *
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * reverse(String str) 将字符串反转，返回一个string
     *
     * StringUtils.reverse(null)  = null
     * StringUtils.reverse("")    = ""
     * StringUtils.reverse("bat") = "tab"
     * reverseDelimited(String str, char separatorChar) 将字符串指定分隔符出的字符反转
     *
     *  StringUtils.reverseDelimited(null, *)      = null
     *  StringUtils.reverseDelimited("", *)        = ""
     *  StringUtils.reverseDelimited("a.b.c", 'x') = "a.b.c"
     *  StringUtils.reverseDelimited("a.b.c", ".") = "c.b.a"
     * split(String str, String separatorChars) 将字符串以指定字符分隔，返回数组
     *
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * substring(String str, int start) 将字符串从指定位置区间截取，返回string
     *
     * swapCase(String str) 将字符串大小写互转，返回一个string
     *
     * StringUtils.swapCase(null)                 = null
     * StringUtils.swapCase("")                   = ""
     * StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
     * toEncodedString(byte[] bytes, Charset charset) 将字符串转为指定编码格式，返回一个string
     *
     * trim(String str) 去除字符串空格
     *
     * trimToEmpty(String str) 去除字符串空格，null转为empty，返回一个string
     *
     * StringUtils.trimToEmpty(null)          = ""
     * StringUtils.trimToEmpty("")            = ""
     * StringUtils.trimToEmpty("     ")       = ""
     * StringUtils.trimToEmpty("abc")         = "abc"
     * StringUtils.trimToEmpty("    abc    ") = "abc"
     * */

    public static void main(String[] args) {
        archUtils();
    }
}
