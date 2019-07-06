package com.zifang.util.core.demo.thirdpart.jar.common.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import org.apache.commons.cli.*;


//https://www.jianshu.com/p/c3ae61787a42
public class ScpCmdTest {
    private static Options OPTIONS = new Options();
    private static CommandLine commandLine;
    private static String HELP_STRING = null;

    public static void main(String[] args) {
        initCliArgs(args);
    }

    /**
     * init args
     *
     * @param args args
     */
    private static void initCliArgs(String[] args) {
        CommandLineParser commandLineParser = new DefaultParser();
        // help
        OPTIONS.addOption("help","usage help");
        // host
        OPTIONS.addOption(Option.builder("h").argName("ipv4 or ipv6").required().hasArg(true).longOpt("host").type(String.class).desc("the host of remote server").build());
        // port
        OPTIONS.addOption(Option.builder("P").hasArg(true).longOpt("port").type(Short.TYPE).desc("the port of remote server").build());
        // user
        OPTIONS.addOption(Option.builder("u").required().hasArg(true).longOpt("user").type(String.class).desc("the user of remote server").build());
        // password
        OPTIONS.addOption(Option.builder("p").required().hasArg(true).longOpt("password").type(String.class).desc("the password of remote server").build());
        // srcPath
        OPTIONS.addOption(Option.builder("s").required().hasArg(true).longOpt("src_path").type(String.class).desc("the srcPath of local").build());
        // dstPath
        OPTIONS.addOption(Option.builder("d").required().hasArg(true).longOpt("dst_path").type(String.class).desc("the dstPath of remote").build());
        try {
            commandLine = commandLineParser.parse(OPTIONS, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage() + "\n" + getHelpString());
            System.exit(0);
        }

    }

    /**
     * get string of help usage
     *
     * @return help string
     */
    private static String getHelpString() {
        if (HELP_STRING == null) {
            HelpFormatter helpFormatter = new HelpFormatter();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
            helpFormatter.printHelp(printWriter, HelpFormatter.DEFAULT_WIDTH, "scp -help", null,
                    OPTIONS, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, null);
            printWriter.flush();
            HELP_STRING = new String(byteArrayOutputStream.toByteArray());
            printWriter.close();
        }
        return HELP_STRING;
    }
}