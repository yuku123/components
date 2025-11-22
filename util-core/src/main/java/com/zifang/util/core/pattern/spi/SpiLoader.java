package com.zifang.util.core.pattern.spi;

public class SpiLoader <T>{

    private Class<T> clazz;

    public static <T> SpiLoader<T> getSpiLoader(Class<T> clazz){
        SpiLoader<T> spiLoader = new SpiLoader<>();
        spiLoader.setClazz(clazz);
        return spiLoader;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

}
