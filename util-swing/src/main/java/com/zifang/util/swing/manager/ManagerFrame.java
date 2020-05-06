package com.zifang.util.swing.manager;

import javax.swing.*;
import java.awt.*;

public class ManagerFrame extends JFrame {
    public static void main(String[] args) {
        ManagerFrame managerFrame = new ManagerFrame();
        managerFrame.setSize(new Dimension(500,500));
        managerFrame.setLocation(500,500);
        managerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        managerFrame.setVisible(true);
    }
}
