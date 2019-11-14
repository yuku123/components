/**
 * windows接入hadoop系统之前的准备
 */
public class ConnectHadoopPrepare {
    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    public static void loadHadoopPrepareFile() {
        loadHadoopPrepareFile("C:/software/hadoop/hadoop-2.7.3/bin/hadoop.dll");
    }

    public static void loadHadoopPrepareFile(String filePath) {
        try {
            System.load(filePath);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }
}
