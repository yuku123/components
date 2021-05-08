package com.zifang.util.core.lang.properties;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

/**
 * @author zifang
 */
@Data
public class JvmProperties {

    private String awtToolkit;
    private String fileEncoding;
    private String fileEncodingPkg;
    private String fileSeparator;
    private String gopherProxySet;
    private String ideaTestCyclicBufferSize;
    private String javaAwtGraphicsenv;
    private String javaAwtPrinterjob;
    private String javaClassPath;
    private String javaClassVersion;
    private String javaEndorsedDirs;
    private String javaExtDirs;
    private String javaHome;
    private String javaIoTmpdir;
    private String javaLibraryPath;
    private String javaRuntimeName;
    private String javaRuntimeVersion;
    private String javaSpecificationName;
    private String javaSpecificationVendor;
    private String javaSpecificationVersion;
    private String javaVendor;
    private String javaVendorUrl;
    private String javaVendorUrlBug;
    private String javaVersion;
    private String javaVmInfo;
    private String javaVmName;
    private String javaVmSpecificationName;
    private String javaVmSpecificationVendor;
    private String javaVmSpecificationVersion;
    private String javaVmVendor;
    private String javaVmVersion;
    private String lineSeparator;
    private String osArch;
    private String osName;
    private String osVersion;
    private String pathSeparator;
    private String sunArchDataModel;
    private String sunBootClassPath;
    private String sunBootLibraryPath;
    private String sunCpuEndian;
    private String sunCpuIsalist;
    private String sunIoUnicodeEncoding;
    private String sunJavaCommand;
    private String sunJavaLauncher;
    private String sunJnuEncoding;
    private String sunManagementCompiler;
    private String sunOsPatchLevel;
    private String userCountry;
    private String userCountryFormat;
    private String userDir;
    private String userHome;
    private String userLanguage;
    private String userName;
    private String userTimezone;
    private String visualvmId;

    private Properties properties;

    public JvmProperties() {
        this.properties = System.getProperties();
    }

    public void init() {
        for (Map.Entry<String, String> entry : JvmPropertiesDefine.defineMap.entrySet()) {
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, properties.getProperty(entry.getValue()));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

