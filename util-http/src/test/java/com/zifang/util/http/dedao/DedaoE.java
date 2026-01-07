package com.zifang.util.http.dedao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zifang.util.core.io.FileUtil;
import com.zifang.util.core.lang.DateUtil;
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
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DedaoE {

    public static String base = "D:\\workplace\\dedao";

    public static String baseTaget = "D:\\workplace\\dedao_target";
    @Test
    public void test() throws IOException {

        File[] files = new File(base).listFiles();

        for(File file :files){
            try {
                String content = FileUtil.getFileContent(file);
                Map<String, Object> stringObjectMap = JSON.parseObject(content);
                String timeStamp = JSONPath.read(content, "$.bookDetail.c.article.PublishTime").toString();
                String raw = JSONPath.read(content, "$.bookDetail.c.content").toString();
                List<Map<String,Object>> rawMap = (List<Map<String,Object>>)JSON.parse(raw);

                LocalDateTime localDateTime = DateUtil.timestampToLocalDateTime(Long.parseLong(timeStamp) * 1000);

                String year = localDateTime.getYear()+"";
                String month = localDateTime.getMonthValue()+"";

                String targetFolder = baseTaget+"\\"+year+"\\"+year+"-"+month;
                File targetFolderFile = new File(targetFolder);
                if(!targetFolderFile.exists()){
                    targetFolderFile.mkdirs();
                }

                String mdContent = "";
                mdContent = mdContent+"---\n" +
                        "title: " + stringObjectMap.get("title") + "\n" +
                        "date: " + DateUtil.format(new Date(), DateUtil.DATE_FORMAT_WHIFFLETREE_SECOND) + "\n" +
                        "tags: " + stringObjectMap.get("tags") + "\n" +
                        "---\n";
                for(Map<String,Object> item : rawMap){
                    if(item.containsKey("text")){
                        mdContent = mdContent+item.get("text").toString() + "\n";
                    }
                }

                File targetFile = new File(targetFolder+"\\"+file.getName().replace(".json","")+".md");
                FileUtil.write(targetFile, mdContent,"utf-8");
            }catch (Exception E){
                System.out.println(file.getName());
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
