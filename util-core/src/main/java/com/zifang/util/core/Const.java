package com.zifang.util.core;

import lombok.Data;

import java.util.List;

/**
 * @author zifang
 */
public class Const {

    interface EnumCollectors<T> {
        List<T> getList();
    }

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

    @Data
    public static class JvmProperties{
        public static final String JVM_VERSION = "java.version";
        public static final String JVM_ENCODING = "file.encoding";
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
    String FUNCTION_PARAM = "$0";
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


    public static final String HEX_CHAR_STR = "0123456789ABCDEF";
}
