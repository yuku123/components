package com.zifang.util.swing.manager.subpanels;


import lombok.Data;

@Data
public class UserObject {

    private Integer id;

    private String displayName;

    @Override
    public String toString(){
        return displayName;
    }
}
