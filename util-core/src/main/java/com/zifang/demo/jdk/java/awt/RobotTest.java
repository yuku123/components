package com.zifang.demo.jdk.java.awt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RobotTest {
    public static void main(String[] args) throws AWTException, InterruptedException, IOException {

        Robot robot = new Robot();
        //设置Robot产生一个动作后的休眠时间,否则执行过快
        robot.setAutoDelay(1000);

        //获取屏幕分辨率
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(d);
        //以屏幕的尺寸创建个矩形
        Rectangle screenRect = new Rectangle(d);
        //截图（截取整个屏幕图片）
        BufferedImage bufferedImage =  robot.createScreenCapture(screenRect);
        //保存截图
        File file = new File("screenRect.png");
        ImageIO.write(bufferedImage, "png", file);

        //移动鼠标
        robot.mouseMove(500, 500);

        //点击鼠标
        //鼠标左键
        System.out.println("单击");
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

        //鼠标右键
        System.out.println("右击");
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);

        //按下ESC，退出右键状态
        System.out.println("按下ESC");
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        //滚动鼠标滚轴
        System.out.println("滚轴");
        robot.mouseWheel(5);

        //按下Alt+TAB键（切换桌面窗口）
        robot.keyPress(KeyEvent.VK_ALT);
        for(int i=1;i<=2;i++)
        {
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        }
        robot.keyRelease(KeyEvent.VK_ALT);

    }

    public void keyboardString(final String text) {
        if (text != null) {
            try {
                final Robot robot = new Robot();
                for (int i = 0; i < text.length(); i++) {
                    final char ch = text.charAt(i);
                    final boolean upperCase = Character.isUpperCase(ch);
                    final int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
                    robot.delay(10);
                    if (upperCase) {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                    }
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    if (upperCase) {
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                    }
                }
            } catch (final Exception e) {
                System.out.println(e);
            }
        }
    }
}