package com.zifang.util.zex.bust.chapter4.enumCase;

public enum HungryLevelEnum2 {
    HUNGRY_LEVEL_1("一成饱"),
    HUNGRY_LEVEL_2("五成饱"),
    HUNGRY_LEVEL_3("十成饱");

    private String description;
    HungryLevelEnum2(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
