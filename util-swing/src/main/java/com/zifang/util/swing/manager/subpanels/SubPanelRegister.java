package com.zifang.util.swing.manager.subpanels;


import javax.swing.*;
import java.util.Map;

public class SubPanelRegister {

    private Map<Integer, JPanel> registedPanel;

    public SubPanelRegister register(Integer id, JPanel jPanel) {
        if (registedPanel.containsKey(id)) {
            throw new RuntimeException("id :" + id + " is dumplicate");
        }

        registedPanel.put(id, jPanel);
        return this;
    }
}
