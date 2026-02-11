//package com.zifang.llm;
//
//import io.qdrant.client.QdrantClient;
//import io.qdrant.client.QdrantGrpcClient;
//import io.qdrant.client.grpc.Collections;
//import io.qdrant.client.grpc.Points;
//import java.util.List;
//import java.util.Map;
//
///**
// * Qdrant v1.16.3 Java集成示例
// * 核心操作：创建集合、插入向量、检索向量、过滤检索、更新/删除向量
// */
//public class QdrantJavaIntegration {
//    // 配置参数（与Docker启动的Qdrant对应）
//    private static final String QDRANT_HOST = "localhost";
//    private static final int QDRANT_GRPC_PORT = 6334;
//    private static final String COLLECTION_NAME = "document_collection";
//    // 向量维度（适配BERT/LLM常用的768维，可根据自己的模型调整）
//    private static final int VECTOR_DIMENSION = 768;
//
//    public static void main(String[] args) {
//        // 1. 创建Qdrant客户端（gRPC连接，核心方式）
//        QdrantClient client = null;
//        try {
//            // 构建客户端（禁用TLS，本地部署无需加密）
//            client = new QdrantClient(
//                    QdrantGrpcClient.newBuilder(QDRANT_HOST, QDRANT_GRPC_PORT, false)
//            );
//            System.out.println("✅ Qdrant客户端连接成功");
//
//            // 2. 创建集合（若不存在）
//            createCollection(client);
//
//            // 3. 插入向量点（含元数据Payload）
//            insertVectors(client);
//
//            // 4. 基础向量检索（Top3）
//            searchVectors(client);
//
//            // 5. 带元数据过滤的检索（只查category=news的结果）
//            searchVectorsWithFilter(client);
//
//            // 6. 更新/删除向量点
//            updateAndDeleteVectors(client);
//
//        } catch (Exception e) {
//            System.err.println("❌ 操作失败：" + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            // 7. 关闭客户端（释放资源）
//            if (client != null) {
//                client.close();
//                System.out.println("🔌 Qdrant客户端已关闭");
//            }
//        }
//    }
//
//    /**
//     * 创建集合（配置向量维度、余弦相似度、HNSW索引）
//     */
//    private static void createCollection(QdrantClient client) {
//        // 检查集合是否已存在，避免重复创建
//        boolean collectionExists = client.collectionExists(COLLECTION_NAME);
//        if (collectionExists) {
//            System.out.println("ℹ️ 集合" + COLLECTION_NAME + "已存在，跳过创建");
//            return;
//        }
//
//        // 构建集合配置：向量维度768，余弦相似度，HNSW索引（Qdrant默认最优索引）
//        Collections.CreateCollectionParams createParams = Collections.CreateCollectionParams.newBuilder()
//                .setCollectionName(COLLECTION_NAME)
//                .setVectorsConfig(Collections.VectorsConfig.newBuilder()
//                        .setConfig(Collections.VectorParams.newBuilder()
//                                .setSize(VECTOR_DIMENSION)
//                                // 距离度量：Cosine（余弦）/Euclidean（欧氏）/Dot（内积）
//                                .setDistance(Collections.Distance.Cosine)
//                                // HNSW索引配置（调优检索性能/精度）
//                                .setHnswConfig(Collections.HnswConfig.newBuilder()
//                                        .setM(16)               // 每个节点的边数（默认16）
//                                        .setEfConstruct(100)    // 构建索引的搜索深度（默认100）
//                                        .build())
//                                .build())
//                        .build())
//                .build();
//
//        // 执行创建
//        client.createCollection(createParams);
//        System.out.println("✅ 集合" + COLLECTION_NAME + "创建成功");
//    }
//
//    /**
//     * 插入向量点（批量插入，含元数据Payload）
//     */
//    private static void insertVectors(QdrantClient client) {
//        // 示例向量（简化为3维，实际替换为768维的模型输出）
//        float[] vec1 = {0.1f, 0.2f, 0.3f};
//        float[] vec2 = {0.4f, 0.5f, 0.6f};
//        float[] vec3 = {0.7f, 0.8f, 0.9f};
//
//        // 构建批量插入的点
//        Points.Batch batch = Points.Batch.newBuilder()
//                // 点1：ID=1，向量，元数据（category=新闻，timestamp=时间戳）
//                .addPoints(Points.PointStruct.newBuilder()
//                        .setId(Points.PointId.newBuilder().setNum(1)) // 数字ID（也支持字符串ID：setStr("doc-1")）
//                        .addAllVectors(List.of(vec1))
//                        .putPayload("category", Points.Value.newBuilder().setStringValue("news").build())
//                        .putPayload("timestamp", Points.Value.newBuilder().setDoubleValue(1710000000).build())
//                        .build())
//                // 点2：ID=2，category=博客
//                .addPoints(Points.PointStruct.newBuilder()
//                        .setId(Points.PointId.newBuilder().setNum(2))
//                        .addAllVectors(List.of(vec2))
//                        .putPayload("category", Points.Value.newBuilder().setStringValue("blog").build())
//                        .putPayload("timestamp", Points.Value.newBuilder().setDoubleValue(1710100000).build())
//                        .build())
//                // 点3：ID=3，category=新闻
//                .addPoints(Points.PointStruct.newBuilder()
//                        .setId(Points.PointId.newBuilder().setNum(3))
//                        .addAllVectors(List.of(vec3))
//                        .putPayload("category", Points.Value.newBuilder().setStringValue("news").build())
//                        .putPayload("timestamp", Points.Value.newBuilder().setDoubleValue(1710200000).build())
//                        .build())
//                .build();
//
//        // 执行批量插入
//        client.upsertPoints(Points.UpsertPointsParams.newBuilder()
//                .setCollectionName(COLLECTION_NAME)
//                .setBatch(batch)
//                .build());
//        System.out.println("✅ 3个向量点插入成功");
//    }
//
//    /**
//     * 基础向量检索（Top3，返回相似度和元数据）
//     */
//    private static void searchVectors(QdrantClient client) {
//        // 查询向量（模拟用户输入的文本转换后的向量）
//        float[] queryVec = {0.15f, 0.25f, 0.35f};
//
//        // 构建检索参数
//        Points.SearchPointsParams searchParams = Points.SearchPointsParams.newBuilder()
//                .setCollectionName(COLLECTION_NAME)
//                .setVector(List.of(queryVec)) // 查询向量
//                .setLimit(3)                  // 返回Top3结果
//                .setWithPayload(true)         // 返回元数据（Payload）
//                .setWithVector(false)         // 不返回向量本身（节省带宽）
//                .build();
//
//        // 执行检索
//        Points.SearchResponse response = client.searchPoints(searchParams);
//
//        // 打印检索结果
//        System.out.println("\n📌 基础检索结果（Top3）：");
//        response.getResultList().forEach(hit -> {
//            System.out.printf("ID: %d, 相似度: %.4f, 类别: %s%n",
//                    hit.getId().getNum(),
//                    hit.getScore(), // 相似度分数（余弦相似度越接近1越匹配）
//                    hit.getPayloadOrThrow("category").getStringValue());
//        });
//    }
//
//    /**
//     * 带元数据过滤的检索（只查category=news的结果）
//     */
//    private static void searchVectorsWithFilter(QdrantClient client) {
//        float[] queryVec = {0.15f, 0.25f, 0.35f};
//
//        // 构建过滤条件：category == "news"
//        Points.Filter filter = Points.Filter.newBuilder()
//                .addMust(Points.Condition.newBuilder()
//                        .setField(Points.FieldCondition.newBuilder()
//                                .setKey("category")
//                                .setMatch(Points.MatchCondition.newBuilder()
//                                        .setKeyword(Points.KeywordMatch.newBuilder().setKey("news"))
//                                        .build())
//                                .build())
//                        .build())
//                .build();
//
//        // 构建检索参数（添加过滤条件）
//        Points.SearchPointsParams searchParams = Points.SearchPointsParams.newBuilder()
//                .setCollectionName(COLLECTION_NAME)
//                .setVector(List.of(queryVec))
//                .setLimit(2)
//                .setFilter(filter) // 应用过滤条件
//                .setWithPayload(true)
//                .build();
//
//        // 执行检索
//        Points.SearchResponse response = client.searchPoints(searchParams);
//
//        // 打印过滤后的结果
//        System.out.println("\n📌 过滤检索结果（仅news类别）：");
//        response.getResultList().forEach(hit -> {
//            System.out.printf("ID: %d, 相似度: %.4f, 类别: %s%n",
//                    hit.getId().getNum(),
//                    hit.getScore(),
//                    hit.getPayloadOrThrow("category").getStringValue());
//        });
//    }
//
//    /**
//     * 更新向量点的元数据 + 删除指定向量点
//     */
//    private static void updateAndDeleteVectors(QdrantClient client) {
//        // 1. 更新ID=1的向量元数据（修改category为updated_news）
//        client.setPayload(Points.SetPayloadParams.newBuilder()
//                .setCollectionName(COLLECTION_NAME)
//                .setPointId(Points.PointId.newBuilder().setNum(1))
//                .putPayload("category", Points.Value.newBuilder().setStringValue("updated_news").build())
//                .build());
//        System.out.println("\n✅ ID=1的向量元数据更新成功");
//
//        // 2. 删除ID=3的向量点
//        client.deletePoints(Points.DeletePointsParams.newBuilder()
//                .setCollectionName(COLLECTION_NAME)
//                .setFilter(Points.Filter.newBuilder()
//                        .addMust(Points.Condition.newBuilder()
//                                .setId(Points.IdCondition.newBuilder().addIds(Points.PointId.newBuilder().setNum(3)))
//                                .build())
//                        .build())
//                .build());
//        System.out.println("✅ ID=3的向量点删除成功");
//
//        // 验证删除结果（检索ID=3，应无结果）
//        Points.SearchPointsParams verifyParams = Points.SearchPointsParams.newBuilder()
//                .setCollectionName(COLLECTION_NAME)
//                .setVector(List.of(0.7f, 0.8f, 0.9f))
//                .setLimit(1)
//                .build();
//        Points.SearchResponse verifyResponse = client.searchPoints(verifyParams);
//        System.out.println("ℹ️ 删除验证：ID=3的向量" + (verifyResponse.getResultCount() == 0 ? "已删除" : "未删除"));
//    }
//}