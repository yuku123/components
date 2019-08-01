package com.zifang.util.core.cli;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

public class EnviomentVaribles {

    public static Map<String,String> getOSEnviomentVaribleMap(){
        Map<String, String> getenv = System.getenv();
        return getenv;
    }

    public static Map<String, String> getJVMVaribleMap() {
        Properties props = System.getProperties();
        Map<String, String> mp = new HashMap(props);
        return mp;
    }
}
