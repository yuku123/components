package com.zifang.util.zex.bust.chapter4.enumCase;

public enum HungryLevelEnum3{
    HUNGRY_LEVEL_1("一成饱"){
        @Override
        void eat(){
            System.out.println("饿死了，赶紧吃");
        }
    },
    HUNGRY_LEVEL_2("五成饱"){
        @Override
        void eat(){
            System.out.println("不是很饿，但是吃得下");
        }
    },
    HUNGRY_LEVEL_3("十成饱"){
        @Override
        void eat(){
            System.out.println("要吐了");
        }
    };

    private String description;
    HungryLevelEnum3(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    abstract void eat();
}
