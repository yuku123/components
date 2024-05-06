package com.zifang.util.core;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zifang
 */
public class Const {

    interface EnumCollectors<T> {
        List<T> getList();
    }

    public static final Map<String, List<String>> ascii = new HashMap<String, List<String>>() {
        {
            put("0", Arrays.asList("°", "₀", "۰"));
            put("1", Arrays.asList("¹", "₁", "۱"));
            put("2", Arrays.asList("²", "₂", "۲"));
            put("3", Arrays.asList("³", "₃", "۳"));
            put("4", Arrays.asList("⁴", "₄", "۴", "٤"));
            put("5", Arrays.asList("⁵", "₅", "۵", "٥"));
            put("6", Arrays.asList("⁶", "₆", "۶", "٦"));
            put("7", Arrays.asList("⁷", "₇", "۷"));
            put("8", Arrays.asList("⁸", "₈", "۸"));
            put("9", Arrays.asList("⁹", "₉", "۹"));
            put("a", Arrays.asList("à", "á", "ả", "ã", "ạ", "ă", "ắ", "ằ", "ẳ", "ẵ", "ặ", "â", "ấ", "ầ", "ẩ", "ẫ", "ậ",
                    "ā", "ą", "å", "α", "ά", "ἀ", "ἁ", "ἂ", "ἃ", "ἄ", "ἅ", "ἆ", "ἇ", "ᾀ", "ᾁ", "ᾂ", "ᾃ",
                    "ᾄ", "ᾅ", "ᾆ", "ᾇ", "ὰ", "ά", "ᾰ", "ᾱ", "ᾲ", "ᾳ", "ᾴ", "ᾶ", "ᾷ", "а", "أ", "အ", "ာ",
                    "ါ", "ǻ", "ǎ", "ª", "ა", "अ", "ا"));
            put("b", Arrays.asList("б", "β", "Ъ", "Ь", "ب", "ဗ", "ბ"));
            put("c", Arrays.asList("ç", "ć", "č", "ĉ", "ċ"));
            put("d", Arrays.asList("ď", "ð", "đ", "ƌ", "ȡ", "ɖ", "ɗ", "ᵭ", "ᶁ", "ᶑ", "д", "δ", "د", "ض", "ဍ", "ဒ", "დ"));
            put("e", Arrays.asList("é", "è", "ẻ", "ẽ", "ẹ", "ê", "ế", "ề", "ể", "ễ", "ệ", "ë", "ē", "ę", "ě", "ĕ", "ė",
                    "ε", "έ", "ἐ", "ἑ", "ἒ", "ἓ", "ἔ", "ἕ", "ὲ", "έ", "е", "ё", "э", "є", "ə", "ဧ", "ေ",
                    "ဲ", "ე", "ए", "إ", "ئ"));
            put("f", Arrays.asList("ф", "φ", "ف", "ƒ", "ფ"));
            put("g", Arrays.asList("ĝ", "ğ", "ġ", "ģ", "г", "ґ", "γ", "ဂ", "გ", "گ"));
            put("h", Arrays.asList("ĥ", "ħ", "η", "ή", "ح", "ه", "ဟ", "ှ", "ჰ"));
            put("i", Arrays.asList("í", "ì", "ỉ", "ĩ", "ị", "î", "ï", "ī", "ĭ", "į", "ı", "ι", "ί", "ϊ", "ΐ", "ἰ", "ἱ",
                    "ἲ", "ἳ", "ἴ", "ἵ", "ἶ", "ἷ", "ὶ", "ί", "ῐ", "ῑ", "ῒ", "ΐ", "ῖ", "ῗ", "і", "ї", "и",
                    "ဣ", "ိ", "ီ", "ည်", "ǐ", "ი", "इ", "ی"));
            put("j", Arrays.asList("ĵ", "ј", "Ј", "ჯ", "ج"));
            put("k", Arrays.asList("ķ", "ĸ", "к", "κ", "Ķ", "ق", "ك", "က", "კ", "ქ", "ک"));
            put("l", Arrays.asList("ł", "ľ", "ĺ", "ļ", "ŀ", "л", "λ", "ل", "လ", "ლ"));
            put("m", Arrays.asList("м", "μ", "م", "မ", "მ"));
            put("n", Arrays.asList("ñ", "ń", "ň", "ņ", "ŉ", "ŋ", "ν", "н", "ن", "န", "ნ"));
            put("o", Arrays.asList("ó", "ò", "ỏ", "õ", "ọ", "ô", "ố", "ồ", "ổ", "ỗ", "ộ", "ơ", "ớ", "ờ", "ở", "ỡ", "ợ",
                    "ø", "ō", "ő", "ŏ", "ο", "ὀ", "ὁ", "ὂ", "ὃ", "ὄ", "ὅ", "ὸ", "ό", "о", "و", "θ", "ို",
                    "ǒ", "ǿ", "º", "ო", "ओ"));
            put("p", Arrays.asList("п", "π", "ပ", "პ", "پ"));
            put("q", Arrays.asList("ყ"));
            put("r", Arrays.asList("ŕ", "ř", "ŗ", "р", "ρ", "ر", "რ"));
            put("s", Arrays.asList("ś", "š", "ş", "с", "σ", "ș", "ς", "س", "ص", "စ", "ſ", "ს"));
            put("t", Arrays.asList("ť", "ţ", "т", "τ", "ț", "ت", "ط", "ဋ", "တ", "ŧ", "თ", "ტ"));
            put("u", Arrays.asList("ú", "ù", "ủ", "ũ", "ụ", "ư", "ứ", "ừ", "ử", "ữ", "ự", "û", "ū", "ů", "ű", "ŭ", "ų",
                    "µ", "у", "ဉ", "ု", "ူ", "ǔ", "ǖ", "ǘ", "ǚ", "ǜ", "უ", "उ"));
            put("v", Arrays.asList("в", "ვ", "ϐ"));
            put("w", Arrays.asList("ŵ", "ω", "ώ", "ဝ", "ွ"));
            put("x", Arrays.asList("χ", "ξ"));
            put("y", Arrays.asList("ý", "ỳ", "ỷ", "ỹ", "ỵ", "ÿ", "ŷ", "й", "ы", "υ", "ϋ", "ύ", "ΰ", "ي", "ယ"));
            put("z", Arrays.asList("ź", "ž", "ż", "з", "ζ", "ز", "ဇ", "ზ"));
            put("aa", Arrays.asList("ع", "आ", "آ"));
            put("ae", Arrays.asList("ä", "æ", "ǽ"));
            put("ai", Arrays.asList("ऐ"));
            put("at", Arrays.asList("@"));
            put("ch", Arrays.asList("ч", "ჩ", "ჭ", "چ"));
            put("dj", Arrays.asList("ђ", "đ"));
            put("dz", Arrays.asList("џ", "ძ"));
            put("ei", Arrays.asList("ऍ"));
            put("gh", Arrays.asList("غ", "ღ"));
            put("ii", Arrays.asList("ई"));
            put("ij", Arrays.asList("ĳ"));
            put("kh", Arrays.asList("х", "خ", "ხ"));
            put("lj", Arrays.asList("љ"));
            put("nj", Arrays.asList("њ"));
            put("oe", Arrays.asList("ö", "œ", "ؤ"));
            put("oi", Arrays.asList("ऑ"));
            put("oii", Arrays.asList("ऒ"));
            put("ps", Arrays.asList("ψ"));
            put("sh", Arrays.asList("ш", "შ", "ش"));
            put("shch", Arrays.asList("щ"));
            put("ss", Arrays.asList("ß"));
            put("sx", Arrays.asList("ŝ"));
            put("th", Arrays.asList("þ", "ϑ", "ث", "ذ", "ظ"));
            put("ts", Arrays.asList("ц", "ც", "წ"));
            put("ue", Arrays.asList("ü"));
            put("uu", Arrays.asList("ऊ"));
            put("ya", Arrays.asList("я"));
            put("yu", Arrays.asList("ю"));
            put("zh", Arrays.asList("ж", "ჟ", "ژ"));
            put("(c)", Arrays.asList("©"));
            put("A", Arrays.asList("Á", "À", "Ả", "Ã", "Ạ", "Ă", "Ắ", "Ằ", "Ẳ", "Ẵ", "Ặ", "Â", "Ấ", "Ầ", "Ẩ", "Ẫ", "Ậ", "Å",
                    "Ā", "Ą", "Α", "Ά", "Ἀ", "Ἁ", "Ἂ", "Ἃ", "Ἄ", "Ἅ", "Ἆ", "Ἇ", "ᾈ", "ᾉ", "ᾊ", "ᾋ", "ᾌ", "ᾍ",
                    "ᾎ", "ᾏ", "Ᾰ", "Ᾱ", "Ὰ", "Ά", "ᾼ", "А", "Ǻ", "Ǎ"));
            put("B", Arrays.asList("Б", "Β", "ब"));
            put("C", Arrays.asList("Ç", "Ć", "Č", "Ĉ", "Ċ"));
            put("D", Arrays.asList("Ď", "Ð", "Đ", "Ɖ", "Ɗ", "Ƌ", "ᴅ", "ᴆ", "Д", "Δ"));
            put("E", Arrays.asList("É", "È", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ế", "Ề", "Ể", "Ễ", "Ệ", "Ë", "Ē", "Ę", "Ě", "Ĕ", "Ė", "Ε",
                    "Έ", "Ἐ", "Ἑ", "Ἒ", "Ἓ", "Ἔ", "Ἕ", "Έ", "Ὲ", "Е", "Ё", "Э", "Є", "Ə"));
            put("F", Arrays.asList("Ф", "Φ"));
            put("G", Arrays.asList("Ğ", "Ġ", "Ģ", "Г", "Ґ", "Γ"));
            put("H", Arrays.asList("Η", "Ή", "Ħ"));
            put("I", Arrays.asList("Í", "Ì", "Ỉ", "Ĩ", "Ị", "Î", "Ï", "Ī", "Ĭ", "Į", "İ", "Ι", "Ί", "Ϊ", "Ἰ", "Ἱ", "Ἳ", "Ἴ",
                    "Ἵ", "Ἶ", "Ἷ", "Ῐ", "Ῑ", "Ὶ", "Ί", "И", "І", "Ї", "Ǐ", "ϒ"));
            put("K", Arrays.asList("К", "Κ"));
            put("L", Arrays.asList("Ĺ", "Ł", "Л", "Λ", "Ļ", "Ľ", "Ŀ", "ल"));
            put("M", Arrays.asList("М", "Μ"));
            put("N", Arrays.asList("Ń", "Ñ", "Ň", "Ņ", "Ŋ", "Н", "Ν"));
            put("O", Arrays.asList("Ó", "Ò", "Ỏ", "Õ", "Ọ", "Ô", "Ố", "Ồ", "Ổ", "Ỗ", "Ộ", "Ơ", "Ớ", "Ờ", "Ở", "Ỡ", "Ợ", "Ø",
                    "Ō", "Ő", "Ŏ", "Ο", "Ό", "Ὀ", "Ὁ", "Ὂ", "Ὃ", "Ὄ", "Ὅ", "Ὸ", "Ό", "О", "Θ", "Ө", "Ǒ", "Ǿ"));
            put("P", Arrays.asList("П", "Π"));
            put("R", Arrays.asList("Ř", "Ŕ", "Р", "Ρ", "Ŗ"));
            put("S", Arrays.asList("Ş", "Ŝ", "Ș", "Š", "Ś", "С", "Σ"));
            put("T", Arrays.asList("Ť", "Ţ", "Ŧ", "Ț", "Т", "Τ"));
            put("U", Arrays.asList("Ú", "Ù", "Ủ", "Ũ", "Ụ", "Ư", "Ứ", "Ừ", "Ử", "Ữ", "Ự", "Û", "Ū", "Ů", "Ű", "Ŭ", "Ų", "У",
                    "Ǔ", "Ǖ", "Ǘ", "Ǚ", "Ǜ"));
            put("V", Arrays.asList("В"));
            put("W", Arrays.asList("Ω", "Ώ", "Ŵ"));
            put("X", Arrays.asList("Χ", "Ξ"));
            put("Y", Arrays.asList("Ý", "Ỳ", "Ỷ", "Ỹ", "Ỵ", "Ÿ", "Ῠ", "Ῡ", "Ὺ", "Ύ", "Ы", "Й", "Υ", "Ϋ", "Ŷ"));
            put("Z", Arrays.asList("Ź", "Ž", "Ż", "З", "Ζ"));
            put("AE", Arrays.asList("Ä", "Æ", "Ǽ"));
            put("CH", Arrays.asList("Ч"));
            put("DJ", Arrays.asList("Ђ"));
            put("DZ", Arrays.asList("Џ"));
            put("GX", Arrays.asList("Ĝ"));
            put("HX", Arrays.asList("Ĥ"));
            put("IJ", Arrays.asList("Ĳ"));
            put("JX", Arrays.asList("Ĵ"));
            put("KH", Arrays.asList("Х"));
            put("LJ", Arrays.asList("Љ"));
            put("NJ", Arrays.asList("Њ"));
            put("OE", Arrays.asList("Ö", "Œ"));
            put("PS", Arrays.asList("Ψ"));
            put("SH", Arrays.asList("Ш"));
            put("SHCH", Arrays.asList("Щ"));
            put("SS", Arrays.asList("ẞ"));
            put("TH", Arrays.asList("Þ"));
            put("TS", Arrays.asList("Ц"));
            put("UE", Arrays.asList("Ü"));
            put("YA", Arrays.asList("Я"));
            put("YU", Arrays.asList("Ю"));
            put("ZH", Arrays.asList("Ж"));
            put(" ", Arrays.asList("\\xC2\\xA0", "\\xE2\\x80\\x80", "\\xE2\\x80\\x81", "\\xE2\\x80\\x82", "\\xE2\\x80\\x83",
                    "\\xE2\\x80\\x84", "\\xE2\\x80\\x85", "\\xE2\\x80\\x86", "\\xE2\\x80\\x87", "\\xE2\\x80\\x88",
                    "\\xE2\\x80\\x89", "\\xE2\\x80\\x8A", "\\xE2\\x80\\xAF", "\\xE2\\x81\\x9F", "\\xE3\\x80\\x80"));
        }
    };

    public static class Symbol{
        public static String AND = "&";
        public static String EQUALS = "=";
        public static String SPOT = ".";
        public static String REGEX_SPOT = "\\.";
        public static String COMMA = ",";
        public static String MINUS = "-";
        public static char MINUS_CHAR = '-';
        public static String PLUS = "+";
        public static char PLUS_CHAR = '+';
        public static String UNDERLINE = "_";
        public static String LEFT_SQ_BRACKET = "[";
        public static String RIGHT_SQ_BRACKET = "]";
        public static String SLASH = "/";
        public static String POUND = "#";
        public static String QUESTION_MARK = "?";
        public static String HYPHEN = "|";
        public static String DOUBLE_HYPHEN = "||";
        public static String COLON = ":";
        public static String SEMICOLON = ";";
        public static String ASTERISK = "*";
        public static String SINGLE_QUOTES = "'";
        public static String SPACE = " ";
        public static char SPACE_CHAR = ' ';
        public static String PER_CENT = "%";
        public static String PER_MILL = "‰";
        public static String RIGHT_CURLY_BRACE = "}";
        public static String LEFT_CURLY_BRACE = "{";
        public static String CURLY_BRACE = "{}";
        public static String AT = "@";
        public static String DOLLAR = "$";
        public static String LEFT_BRACKET = "(";
        public static String RIGHT_BRACKET = ")";
    }

    @Data
    public static class OperateSystem implements EnumCollectors<OperateSystem> {

        private final String system;
        private final String lowerSystem;

        OperateSystem WINDOWS = new OperateSystem("Windows", "windows");
        OperateSystem Mac = new OperateSystem("Mac", "mac");
        OperateSystem Unix = new OperateSystem("Unix", "x11");
        OperateSystem LINUX = new OperateSystem("LINUX", "linux");
        OperateSystem Android = new OperateSystem("Android", "android");
        OperateSystem IPhone = new OperateSystem("IPhone", "iphone");
        OperateSystem UnKnown = new OperateSystem("UnKnown", "unKnown");
        OperateSystem LOCAL_OPERATE_SYSTEM = getOperateSystem(System.getProperty("os.name"));

        public OperateSystem(String system, String lowerSystem) {
            this.system = system;
            this.lowerSystem = lowerSystem;
        }

        public OperateSystem getOperateSystem(String info) {
            return getList().stream()
                    .filter(system -> info.toLowerCase().contains(system.getLowerSystem()))
                    .findFirst()
                    .orElse(UnKnown);
        }

        @Override
        public List<OperateSystem> getList() {
            return null;
        }
    }

    public class TimeFormat{
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm:ss";
    }

    byte BYTE_FALSE = (byte) 0;
    byte BYTE_TRUE = (byte) 1;
    byte FALSE = 0;
    byte TRUE = 1;

    @Data
    public static class Statics{
        /** Unicode 基本汉字编码范围0x4e00~0x9fa5 共 20902个 */
        int CHINESE_CHARACTER_LENGTH = 20902;
        /** 汉字起始值 */
        int CHINESE_CHARACTER_START = 0x4e00;
    }

    @Data
    public static class Charset{
        public static final String US_ASCII = "US-ASCII";
        public static final String ISO_8859_1 = "ISO-8859-1";
        public static final String UTF_8 = "UTF-8";
        public static final String UTF_16BE = "UTF-16BE";
        public static final String UTF_16LE = "UTF-16LE";
        public static final String UTF_16 = "UTF-16";
        public static final String GBK = "GBK";
        public static final String GB2312 = "GB2312";
    }

    /**
     * script template suffix
     * 脚本模板后缀
     */
    String SCRIPT_TEMPLATE_SUFFIX = ".template";

    /**
     * calculate method name
     * 校验脚本方法名
     */
    String CALCULATE_METHOD_NAME = "calculate";

    /**
     * function param
     * 方法入参
     */
    String FUNCTION_PARAM = "$0";

    /**
     * simple function param
     * 简化 方法入参
     */
    String SIMPLE_FUNCTION_PARAM = "$";

    String INVOCABLE_KEY_FORMAT = "%s:%s:%s";


    String ZERO = "0";
    String ONE = "1";
//    String TRUE = "true";
//    String FALSE = "false";
    String EMPTY = "";
    String[] EMPTY_ARRAY = new String[0];
    String DEFAULT = "default";
    String LINE_BREAK = "\n";

    /**
     * 空字符串
     */
    public static final String STR_EMPTY = "";

    /**
     * 未登陆
     */
    public final static String SYS_NOLOGIN = "Nologin";

    /**
     * 成功
     */
    public final static String SYS_SUCCESS = "Success";

    /**
     * 失败
     */
    public final static String SYS_ERROR = "Error";

    /**
     * 异常
     */
    public final static String SYS_EXCEPTION = "Exception";

    /**
     * 无记录
     */
    public final static String SYS_NORECORD = "NoRecord";

    /**
     * 业务访问
     */
    public final static String BUSINESS_ACCESS = "ACCESS";


    /**
     * 业务插入
     */
    public final static String BUSINESS_INSERT = "INSERT";


    /**
     * 业务更新
     */
    public final static String BUSINESS_UPDATE = "UPDATE";


    /**
     * 业务删除
     */
    public final static String BUSINESS_DELETE = "DELETE";


    /**
     * 业务文件上传
     */
    public final static String BUSINESS_UPLOAD = "UPLOAD";



    /**
     * JVM的版本
     */
    public static final String JVM_VERSION = "java.version";
    /**
     * JVM的编码
     */
    public static final String JVM_ENCODING = "file.encoding";
    /**
     * JVM默认的临时目录
     */
    public static final String JVM_TEMPDIR = "java.io.tmpdir";

    /**
     * http代理主机标识
     */
    public static final String HTTP_PROXY_HOST = "http.proxyHost";

    /**
     * http代理主机端口
     */
    public static final String HTTP_PROXY_PORT = "http.proxyPort";

    /**
     * http代理用户标识
     */
    public static final String HTTP_PROXY_USER = "http.proxyUser";

    /**
     * http代理用户密码标识
     */
    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";

    /**
     * 主机架构
     */
    public static final String SYS_OS_ARCH = "os.arch";
    /**
     * 主机类型
     */
    public static final String SYS_OS_NAME = "os.name";
    /**
     * 主机类型版本
     */
    public static final String SYS_OS_VERSION = "os.version";
    /**
     * 操作系统类型
     */
    public static final String SYS_SUN_DESKTOP = "sun.desktop";

    /**
     * 系统文件分隔符key
     */
    public static final String SYS_FILE_SEPARATOR = "file.separator";
    /**
     * 系统路径分隔符key
     */
    public static final String SYS_PATH_SEPARATOR = "path.separator";

    /**
     * 系统换行符key
     */
    public static final String SYS_LINE_SEPARATOR = "line.separator";

    /**
     * 十六进制字符串
     */
    public static final String HEX_CHAR_STR = "0123456789ABCDEF";

    /**
     * 常用的符号
     */
    public static final String[] SYMBOL_FH = new String[]{"░", "▒", "▣", "▤", "▥", "▦", "▧", "▨", "▩",
            "▪", "▫", "▬", "◆", "◇", "◈", "◎", "●", "◐", "◑", "☉", "☎", "☏", "☜", "☞", "☺", "☻", "☼",
            "♠", "♡", "♢", "♣", "♤", "♥", "♦", "♧", "♨", "♩", "♪", "♫", "♬", "♭", ".", "。", "，", "、", ";", "：", "？", "!",
            "ˉ", "ˇ", "¨", "`", "~", "々", "～", "‖", "∶", "'", "`", "|", "·", "…", "—", "～", "-",
            "〃", "‘", "’", "“", "”", "〝", "〞", "〔", "〕", "〈", "〉", "《", "》", "「", "」", "『", "』", "〖", "〗", "【", "】", "(", ")", "[",
            "]", "{", "｝", "︻", "︼", "﹄", "﹃",};

    /**
     * 常用的数学符号
     */
    public static final String[] SYMBOL_MATH = new String[]{"+", "-", "×", "÷", "﹢", "﹣", "±", "/", "=", "∥", "∠", "≌", "∽",
            "≦", "≧", "≒", "﹤", "﹥", "≈", "≡", "≠", "=", "≤", "≥", "<", ">", "≮", "≯", "∷", "∶", "∫", "∮",
            "∝", "∞", "∧", "∨", "∑", "∏", "∪", "∩", "∈", "∵", "∴", "⊥", "∥", "∠", "⌒", "⊙", "√", "∟", "⊿", "㏒", "㏑", "%", "‰"};


    /**
     * 计量符号
     */
    public static final String[] SYMBOL_UNIT = new String[]{"㎎", "㎏", "㎜", "㎝", "㎞", "㎡", "㏄", "㏎", "㏑",
            "㏒", "㏕", "℡", "%", "‰", "℃", "℉", "°", "′", "″", "$", "￡", "￥", "￠", "♂", "♀", "℅"};

    /**
     * 常用的数学符号
     */
    public static final String[] SYMBOL_NUMBER = new String[]{
            "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩",
            "㈠", "㈡", "㈢", "㈣", "㈤", "㈥", "㈦", "㈧", "㈨", "㈩",
            "⑴", "⑵", "⑶", "⑷", "⑸", "⑹", "⑺", "⑻", "⑼", "⑽",
            "⑾", "⑿", "⒀", "⒁", "⒂", "⒃", "⒄", "⒅", "⒆", "⒇",
            "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ", "Ⅵ", "Ⅶ", "Ⅷ", "Ⅸ", "Ⅹ",
            "Ⅺ", "Ⅻ", "ⅰ", "ⅱ", "ⅲ", "ⅳ", "ⅴ", "ⅵ", "ⅶ", "ⅷ", "ⅸ", "ⅹ"};

    /**
     * 一些常用的无意义的符号
     */
    public static final char[] SYMBOL_UNMEANING =
            ("·ˉˇ¨〃々—～‖…「」『』〖〗【】±+-×÷∧∨∑∏∪∩∈√⊥∥∠⌒⊙∫∮≡≌≈∽∝≠≮≯≤≥∞∶ ∵∴∷♂♀°′" +
                    "″℃$¤￠￡‰§№☆★〇○●◎◇◆ 回□■△▽⊿▲▼◣◤◢◥▁▂▃▄▅▆▇█▉▊▋▌▍▎▏▓※→←↑↓↖↗↘↙〓!￥^`{|｝ぁあぃいぅうぇえぉおかがきぎ" +
                    "くぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴ" +
                    "ふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをんァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセ" +
                    "ゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵ" +
                    "ヶΓΔΛΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθ ικλμνξοπρστυφχψ ω︵︶︹︺︿﹀︽︾﹁﹂﹃﹄︻︼︷︸АБВГДЕЁЖЗИЙКЛМНОПРСТУ" +
                    "ФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыь эюāáǎàēéěèī íǐìōóǒòūúǔùǖǘǚǜüêɑńňɡㄅㄆㄇㄈㄉㄊㄋㄌ" +
                    "ㄍㄎㄏㄐㄑㄒㄓㄔㄕㄖㄗㄘㄙㄚㄛㄜㄝㄞㄟㄠㄡㄢㄣㄤㄥㄦㄧㄨㄩ︱︳︴﹏﹋﹌─━│┃┄┅┆┇┈┉┊┋┌┍┎┏┐┑┒┓└" +
                    "┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄ ╅╆╇╈╉╊╋㊣㈱曱甴" +
                    "∟┅﹍╭╮╰╯^︵^`√卐℡ぁ〝〞ミ灬№*ㄨ≮≯∝≌∽≦≧≒じぷ┗┛￥￡§я-―‥…‰′″℅℉№℡∕∝" +
                    "∣═║╒╓╔╕╖╗╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬╱╲╳▔▕〆〒〡〢〣〤〥〦〧〨〩︰﹍﹎------").toCharArray();

}
