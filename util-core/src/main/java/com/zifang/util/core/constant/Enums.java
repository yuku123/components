package com.zifang.util.core.constant;

import lombok.Data;

import java.util.List;

public class Enums {

    interface EnumCollectors<T> {
        List<T> getList();
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
}