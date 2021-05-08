package com.zifang.util.crawler.jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestJsoupParseHtml {
    public static void main(String[] args) {
        System.out.println("helloworld");
        String url = "http://hotels.ctrip.com/hotel/beijing1/location94#ctm_ref=hod_hp_sb_lst";
        String encoding = "utf-8";
        String html = getHTMLResourceByUrl(url, encoding);
        System.out.println(html);//输出html
        String fengzhuang = Fengzhuang(html, encoding);
        System.out.println(fengzhuang);
    }

    public static String Fengzhuang(String html, String encoding) {
        Document parse = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        //解析html，按照什么编码进行解析html
        parse = Jsoup.parse(html, encoding);
        Element elementById = parse.getElementById("hotel_list");
        Elements elementsByClass = elementById.getElementsByClass("searchresult_list");
        for (Element element : elementsByClass) {
            Map<String, String> map = new HashMap<String, String>();
            //获取酒店的图片
            String imgSrc = element.getElementsByTag("img").attr("src");
            //获取酒店title
            String title = element.getElementsByTag("ima").attr("alt");
            //获取酒店的描述信息
            String desc = element.getElementsByClass("searchresult_htladdress").text();
            map.put("imgSrc", imgSrc);
            map.put("title", title);
            map.put("desc", desc);
            list.add(map);
        }
        return list.toString();
    }

    //获取html
    public static String getHTMLResourceByUrl(String url, String encoding) {
        StringBuffer sb = new StringBuffer();
        URL urlObj = null;
        URLConnection openConnection = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            urlObj = new URL(url);
            openConnection = urlObj.openConnection();
            isr = new InputStreamReader(openConnection.getInputStream(), encoding);
            //建立文件缓冲流
            br = new BufferedReader(isr);
            //建立临时文件
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp + "\n");
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return sb.toString();
    }

}