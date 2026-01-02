package com.zifang.util.http.dedao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zifang.util.core.io.FileUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DedaoC {


    public static String base = "D:\\workplace\\dedao";
    @Test
    public void test() throws IOException {

        int page = 0;
        int pageSize = 100;
        while(true){
            List<Map<String,Object>> books = getBooks(page, pageSize);
            System.out.println("开始获取第"+page+"页数据");
            if(books != null && books.size() > 0){
                page = page +1;
            } else{
                break;
            }

            for(Map<String,Object> book : books){
                try {

                    String aliasId = null;
                    if(book.get("alias_id") == null || String.valueOf(book.get("alias_id")).equals("")){
                        aliasId = ((List)book.get("odob_alias_list")).get(0).toString();
                    } else {
                        aliasId = book.get("alias_id").toString();
                    }


                    // 获得听书的描述信息
                    Map<String,Object> bookDescribeInfo = getBookDescribeInfo(aliasId);

                    String dd_article_token = bookDescribeInfo.get("dd_article_token").toString();
                    String urlEncode = URLEncoder.encode(dd_article_token, "utf-8");
                    Map<String, Object> bookDetail = getBookDetail(urlEncode);

                    String booName = String.valueOf(book.get("name")).split("\\|")[0].replace("《","").replace("》","")
                            .replace("：","")
                            .replace(".","_")
                            .replace("?","？")
                            .replace(" ","")
                            .replace("<","_")
                            .replace(">","_");
                    Map<String,Object> context = new HashMap<>();
                    context.put("book",book);
                    context.put("bookDescribeInfo",bookDescribeInfo);
                    context.put("bookDetail",bookDetail);

                    FileUtil.write(new File(base+"\\"+booName+".json"), JSON.toJSONString(context, SerializerFeature.PrettyFormat),"utf-8");
                    System.out.println("----开始写出+"+booName );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<String, Object> getBookDetail(String urlEncode) throws IOException {
        HttpGet httpGet = new HttpGet("https://www.dedao.cn/pc/odob/pc/audio/article/get?token="+urlEncode);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String res = EntityUtils.toString(response.getEntity());
        Map<String,Object> ress = (Map<String, Object>) JSON.parse(res.toString());
        return ress;
    }

    private Map<String, Object> getBookDescribeInfo(String aliasId) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("audio_alias_id", aliasId);

        HttpPost httpPost = new HttpPost("https://www.dedao.cn/pc/bauhinia/pc/article/info");
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        Map<String,Object> ress = (Map<String, Object>)JSONPath.read(res, "$.c");
        return ress;
    }

    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();


    private List<Map<String, Object>> getBooks(int page, int pageSize) throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("page_size", pageSize);
        params.put("sort_strategy", "NEW");
        params.put("product_types", "13,1013");


        HttpPost httpPost = new HttpPost("https://www.dedao.cn/pc/label/v2/algo/pc/product/list");
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        List<Map<String,Object>> ress = (List<Map<String, Object>>) JSONPath.read(res, "$.c.product_list");

        return ress;
    }
}
