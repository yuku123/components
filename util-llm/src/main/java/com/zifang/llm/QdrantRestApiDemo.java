package com.zifang.llm;
import okhttp3.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修复版：Qdrant REST API集成示例
 * 解决：1. 向量维度不匹配 2. 过滤条件JSON格式错误
 */
public class QdrantRestApiDemo {
    private static final String QDRANT_BASE_URL = "http://192.168.31.250:6333";
    private static final String COLLECTION_NAME = "test2";
    // 统一向量维度（与创建集合时的768维一致）
    private static final int VECTOR_DIMENSION = 768;
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public static void main(String[] args) {
        try {
            // 1. 先删除旧集合（避免维度冲突，可选）
            deleteCollection();
            // 2. 创建集合（768维向量，余弦相似度）
            createCollection();
            // 3. 插入向量点（768维，与集合配置一致）
            insertVectors();
            // 4. 基础向量检索（768维查询向量）
            searchVectors();
            // 5. 带过滤条件的检索（修复JSON格式）
            searchVectorsWithFilter();

        } catch (IOException e) {
            System.err.println("操作失败：" + e.getMessage());
        }
    }

    /**
     * 删除旧集合（避免维度冲突，可选）
     */
    private static void deleteCollection() throws IOException {
        Request request = new Request.Builder()
                .url(QDRANT_BASE_URL + "/collections/" + COLLECTION_NAME)
                .delete()
                .build();
        client.newCall(request).execute(); // 忽略结果，仅清理旧数据
    }

    /**
     * 创建集合（768维向量，余弦相似度）
     */
    private static void createCollection() throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", COLLECTION_NAME);
        Map<String, Object> vectorsConfig = new HashMap<>();
        vectorsConfig.put("size", VECTOR_DIMENSION); // 768维
        vectorsConfig.put("distance", "Cosine");
        requestBody.put("vectors", vectorsConfig);

        Request request = new Request.Builder()
                .url(QDRANT_BASE_URL + "/collections/" + COLLECTION_NAME)
                .method("PUT", RequestBody.create(
                        JSON.toJSONString(requestBody),
                        MediaType.parse("application/json")
                ))
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("✅ 集合创建成功：" + response.body().string());
        } else {
            String errorMsg = response.body().string();
            if (errorMsg.contains("already exists")) {
                System.out.println("ℹ️ 集合已存在，跳过创建");
            } else {
                System.err.println("❌ 集合创建失败：" + errorMsg);
            }
        }
    }

    /**
     * 插入向量点（修复：使用768维向量）
     */
    private static void insertVectors() throws IOException {
        List<Map<String, Object>> points = new ArrayList<>();

        // 点1：768维向量（示例：前3位有值，其余填充0）
        Map<String, Object> point1 = new HashMap<>();
        point1.put("id", 1);
        point1.put("vector", generate768DimVector(0.1f, 0.2f, 0.3f)); // 生成768维向量
        Map<String, Object> payload1 = new HashMap<>();
        payload1.put("category", "news");
        payload1.put("timestamp", 1710000000.0);
        point1.put("payload", payload1);
        points.add(point1);

        // 点2：768维向量
        Map<String, Object> point2 = new HashMap<>();
        point2.put("id", 2);
        point2.put("vector", generate768DimVector(0.4f, 0.5f, 0.6f));
        Map<String, Object> payload2 = new HashMap<>();
        payload2.put("category", "blog");
        payload2.put("timestamp", 1710100000.0);
        point2.put("payload", payload2);
        points.add(point2);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("points", points);

        Request request = new Request.Builder()
                .url(QDRANT_BASE_URL + "/collections/" + COLLECTION_NAME + "/points")
                .method("PUT", RequestBody.create(
                        JSON.toJSONString(requestBody),
                        MediaType.parse("application/json")
                ))
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("✅ 向量插入成功：" + response.body().string());
        } else {
            System.err.println("❌ 向量插入失败：" + response.body().string());
        }
    }

    /**
     * 基础向量检索（修复：768维查询向量）
     */
    private static void searchVectors() throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vector", generate768DimVector(0.15f, 0.25f, 0.35f)); // 768维查询向量
        requestBody.put("limit", 3);
        requestBody.put("with_payload", true);

        Request request = new Request.Builder()
                .url(QDRANT_BASE_URL + "/collections/" + COLLECTION_NAME + "/points/search")
                .method("POST", RequestBody.create(
                        JSON.toJSONString(requestBody),
                        MediaType.parse("application/json")
                ))
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            JSONObject jsonResult = JSON.parseObject(result);
            System.out.println("\n📌 基础检索结果：");
            jsonResult.getJSONArray("result").forEach(item -> {
                JSONObject hit = (JSONObject) item;
                System.out.printf("ID: %d, 相似度: %.4f, 类别: %s%n",
                        hit.getInteger("id"),
                        hit.getDouble("score"),
                        hit.getJSONObject("payload").getString("category"));
            });
        } else {
            System.err.println("❌ 检索失败：" + response.body().string());
        }
    }

    /**
     * 带过滤条件的检索（修复：JSON格式错误）
     */
    private static void searchVectorsWithFilter() throws IOException {
        // 修复：严格按照Qdrant的过滤语法构建（关键修改点）
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vector", generate768DimVector(0.15f, 0.25f, 0.35f));
        requestBody.put("limit", 2);
        requestBody.put("with_payload", true);

        // 正确的过滤条件结构（Qdrant要求的格式）
        Map<String, Object> filter = new HashMap<>();
        List<Map<String, Object>> mustList = new ArrayList<>();
        Map<String, Object> fieldCondition = new HashMap<>();

        Map<String, Object> field = new HashMap<>();
        field.put("key", "category");
        Map<String, Object> match = new HashMap<>();
        match.put("keyword", "news");
        field.put("match", match);

        fieldCondition.put("field", field); // 核心：条件必须包裹在"field"中
        mustList.add(fieldCondition);
        filter.put("must", mustList);

        requestBody.put("filter", filter); // 把过滤条件放入requestBody

        // 手动序列化（避免Fastjson2的结构问题，可选）
        String jsonBody = JSONObject.toJSONString(requestBody);
        System.out.println("\n📝 过滤检索请求体：" + jsonBody); // 可查看序列化后的JSON

        Request request = new Request.Builder()
                .url(QDRANT_BASE_URL + "/collections/" + COLLECTION_NAME + "/points/search")
                .method("POST", RequestBody.create(
                        jsonBody,
                        MediaType.parse("application/json; charset=utf-8")
                ))
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            JSONObject jsonResult = JSON.parseObject(result);
            System.out.println("\n📌 过滤检索结果（仅news类别）：");
            jsonResult.getJSONArray("result").forEach(item -> {
                JSONObject hit = (JSONObject) item;
                System.out.printf("ID: %d, 相似度: %.4f, 类别: %s%n",
                        hit.getInteger("id"),
                        hit.getDouble("score"),
                        hit.getJSONObject("payload").getString("category"));
            });
        } else {
            System.err.println("❌ 过滤检索失败：" + response.body().string());
        }
    }

    /**
     * 生成768维向量（前3位为指定值，其余填充0，仅用于示例）
     */
    private static float[] generate768DimVector(float v1, float v2, float v3) {
        float[] vector = new float[VECTOR_DIMENSION];
        vector[0] = v1;
        vector[1] = v2;
        vector[2] = v3;
        // 其余维度默认0
        return vector;
    }
}