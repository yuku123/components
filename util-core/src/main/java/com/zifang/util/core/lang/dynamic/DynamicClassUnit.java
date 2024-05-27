package com.zifang.util.core.lang.dynamic;

import java.util.ArrayList;
import java.util.List;

public class DynamicClassUnit {

    private DynamicClass main;

    private List<DynamicClassUnit> sub = new ArrayList<>();

    public DynamicClass getMain() {
        return main;
    }

    public void setMain(DynamicClass main) {
        this.main = main;
    }

    public List<DynamicClassUnit> getSub() {
        return sub;
    }

    public void setSub(List<DynamicClassUnit> sub) {
        this.sub = sub;
    }

    public List<DynamicClass> collect() {
        List<DynamicClass> dynamicClasses = new ArrayList<>();
        dynamicClasses.add(main);
        for(DynamicClassUnit dynamicClassUnit : sub){
            dynamicClasses.addAll(dynamicClassUnit.collect());
        }
        return dynamicClasses;
    }
}
