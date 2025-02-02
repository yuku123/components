package com.zifang.util.zex.bust.chapter11;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

import org.junit.Test;

public class TestInputStream extends JFrame {

    @Test
    public void testAudioInputStream() {
        File file;
        AudioInputStream ais = null;
        AudioFormat format;
        DataLine.Info info;
        SourceDataLine sdline = null;
        try {

            file = new File("/Users/zifang/Downloads/001.后会无期.wav");
            ais = AudioSystem.getAudioInputStream(file);
            format = ais.getFormat();
            info = new DataLine.Info(SourceDataLine.class, format);
            sdline = (SourceDataLine) AudioSystem.getLine(info);
            sdline.open(format);
            sdline.start();

            int nBytesRead = 0;
            byte[] abData = new byte[524288];
            while (nBytesRead != -1) {
                nBytesRead = ais.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    sdline.write(abData, 0, abData.length);
                }
            }


        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            try {
                ais.close();
                //auline.drain()和auline.close()用来保证该声音文件播放完毕，如果去掉会出现声音未播放完即结束的情况。
                sdline.drain();
                sdline.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void clipTest() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Test Sound Clip");
        this.setSize(300, 200);
        this.setVisible(true);

        try {

            // Open an audio input stream.
            // URL url = this.getClass().getResource("hello.wav");
            File file = new File("/Users/zifang/Downloads/001.后会无期.wav");

            // AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestInputStream().clipTest();
    }
}