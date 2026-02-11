package com.zifang.llm;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;


public class OllamaRAGQuestionAnswerSystem {

    // 你的环境配置（无需修改）
    private static final String LOCAL_DOC_PATH = "D:\\workplace\\dedao_target\\2025\\2025-1\\表层的真理.md";
    private static final String OLLAMA_BASE_URL = "http://192.168.31.250:11434";
    private static final String EMBEDDING_MODEL = "nomic-embed-text";
    private static final String LLM_MODEL = "qwen2:7b"; // 替换为你已下载的模型（如qwen:7b）
    private static final int CHUNK_SIZE = 256;
    private static final int CHUNK_OVERLAP = 32;
    private static final int RETRIEVE_TOP_K = 3;

    // 核心组件：使用自定义内存向量存储
    private static EmbeddingModel embeddingModel;
    private static ChatLanguageModel chatModel;
    private static SimpleInMemoryEmbeddingStore embeddingStore = new SimpleInMemoryEmbeddingStore();

    public static void main(String[] args) {
        try {
            // 1. 前置检查：验证Ollama服务
            if (!isOllamaReachable()) {
                System.err.println("❌ 无法连接Ollama服务：" + OLLAMA_BASE_URL);
                return;
            }

            // 2. 初始化核心组件
            initComponents();
            System.out.println("✅ 所有组件初始化完成（自定义内存向量库）");

            // 3. 加载文档并向量化存储（仅首次运行）
            loadDocumentAndInitEmbeddingStore();
            System.out.println("✅ 文档向量化存储完成，向量库大小：" + embeddingStore.count());

            // 4. 问答示例
            answerQuestion("表层的真理主要讲了什么？");
            answerQuestion("文档中的核心观点有哪些？");

        } catch (Exception e) {
            System.err.println("❌ 系统运行失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 验证Ollama服务可达性
     */
    private static boolean isOllamaReachable() {
        try {
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)
                    new java.net.URL(OLLAMA_BASE_URL + "/api/tags").openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 初始化组件：适配0.32版本LLM/Embedding模型
     */
    private static void initComponents() {
        // 1. 嵌入模型（0.32版本无temperature参数）
        embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(OLLAMA_BASE_URL)
                .modelName(EMBEDDING_MODEL)
                .timeout(Duration.of(3, ChronoUnit.MINUTES)) // 增加超时时间（适配远程访问）
                .build();

        // 2. LLM模型（0.32版本OllamaChatModel适配）
        chatModel = OllamaChatModel.builder()
                .baseUrl(OLLAMA_BASE_URL)
                .modelName(LLM_MODEL)
                .temperature(0.1) // LLM专属参数，控制回答随机性
                .timeout(Duration.of(3, ChronoUnit.MINUTES)) // 增加超时时间（适配远程访问）
                .build();
    }

    /**
     * 加载MD文档并存储到自定义向量库
     */
    private static void loadDocumentAndInitEmbeddingStore() throws IOException, TikaException {
        // 加载并解析MD文档
        Document document = loadMarkdownDocument(LOCAL_DOC_PATH);
        // 分块
        List<Document> chunks = splitDocumentIntoChunks(document.text());

        // 向量化并存储
        for (Document chunk : chunks) {
            String text = chunk.text().trim();
            if (text.isEmpty()) continue;

            Embedding embedding = embeddingModel.embed(text).content();
            TextSegment segment = TextSegment.from(text);
            embeddingStore.add(embedding, segment);
        }
    }

    /**
     * 加载Windows MD文档
     */
    private static Document loadMarkdownDocument(String docPath) throws IOException, TikaException {
        File docFile = new File(docPath.replace("\\", File.separator));
        if (!docFile.exists()) throw new IOException("文档不存在：" + docPath);
        if (!docFile.canRead()) throw new IOException("无读取权限：" + docPath);

        // Tika解析MD文档为纯文本
        Tika tika = new Tika();
        String text = tika.parseToString(docFile)
                .replaceAll("#+ ", "") // 移除MD标题符号
                .replaceAll("\\*+", "") // 移除粗体/斜体符号
                .replaceAll("\\s+", " ")
                .trim();

        if (text.isEmpty()) throw new IOException("文档解析后无有效文本");

        Metadata metadata = new Metadata();
        metadata.add("file_path", docFile.getAbsolutePath());
        metadata.add("file_name", docFile.getName());
        return Document.from(text, metadata);
    }

    /**
     * 文档分块（适配0.32版本，无官方分块类）
     */
    private static List<Document> splitDocumentIntoChunks(String text) {
        List<Document> chunks = new ArrayList<>();
        String[] separators = {"\n\n", "\n", "。", "！", "？", "；", "，", " "};
        List<String> baseChunks = new ArrayList<>();
        splitBySeparators(text, separators, 0, baseChunks);

        for (String baseChunk : baseChunks) {
            String trimmed = baseChunk.trim();
            if (trimmed.isEmpty()) continue;

            if (trimmed.length() <= CHUNK_SIZE) {
                chunks.add(Document.from(trimmed));
            } else {
                splitLongChunkHard(trimmed, chunks);
            }
        }
        return chunks;
    }

    /**
     * 按自然分隔符拆分文本
     */
    private static void splitBySeparators(String text, String[] separators, int sepIndex, List<String> baseChunks) {
        if (sepIndex >= separators.length || text.length() <= CHUNK_SIZE) {
            baseChunks.add(text);
            return;
        }

        String separator = separators[sepIndex];
        String[] parts = text.split(separator);
        StringBuilder current = new StringBuilder();

        for (String part : parts) {
            if (current.length() + part.length() + separator.length() > CHUNK_SIZE) {
                if (current.length() > 0) {
                    baseChunks.add(current.toString());
                    current = new StringBuilder();
                }
            }
            if (current.length() > 0) current.append(separator);
            current.append(part);
        }

        if (current.length() > 0) {
            baseChunks.add(current.toString());
        }
    }

    /**
     * 硬分割超长块（无死循环）
     */
    private static void splitLongChunkHard(String longText, List<Document> chunks) {
        int start = 0;
        int textLength = longText.length();
        while (start < textLength) {
            int end = Math.min(start + CHUNK_SIZE, textLength);
            String chunk = longText.substring(start, end).trim();
            if (!chunk.isEmpty()) chunks.add(Document.from(chunk));
            if (end >= textLength) break;
            int nextStart = end - CHUNK_OVERLAP;
            start = Math.max(nextStart, start + 1);
        }
    }

    /**
     * 核心问答逻辑：检索+调用Ollama生成回答
     */
    private static void answerQuestion(String question) {
        System.out.println("\n======================");
        System.out.println("📌 问题：" + question);

        try {
            // 1. 生成问题向量
            Embedding questionEmbedding = embeddingModel.embed(question).content();

            // 2. 检索最相似的Top-K文档块
            List<SimpleInMemoryEmbeddingStore.EmbeddingMatch> matches = embeddingStore.findRelevant(questionEmbedding, RETRIEVE_TOP_K);
            if (matches.isEmpty()) {
                System.out.println("❌ 未检索到相关内容");
                return;
            }

            // 3. 拼接上下文
            StringBuilder context = new StringBuilder();
            for (int i = 0; i < matches.size(); i++) {
                SimpleInMemoryEmbeddingStore.EmbeddingMatch match = matches.get(i);
                context.append("【相关内容").append(i + 1).append("（相似度：").append(String.format("%.2f", match.score())).append("）】")
                        .append(match.embedded().text()).append("\n");
            }

            // 4. 构建提示词（适配中文问答）
            String prompt = String.format(
                    "请严格基于以下上下文回答问题，仅使用上下文提供的信息，不要编造内容，回答简洁明了。\n" +
                            "上下文：\n%s\n" +
                            "问题：%s", context.toString(), question
            );

            // 5. 调用Ollama LLM生成回答
            String answer = chatModel.generate(prompt);

            // 6. 输出结果
            System.out.println("✅ 回答：");
            System.out.println(answer);
        } catch (Exception e) {
            System.err.println("❌ 回答生成失败：" + e.getMessage());
        }
    }

    // ===================== 核心：自定义内存向量存储（替代官方缺失类） =====================
    /**
     * 极简内存向量存储：完全替代InMemoryEmbeddingStore，适配0.32版本
     * 功能：存储向量+文本段、检索相似向量、统计数量
     */
    static class SimpleInMemoryEmbeddingStore {
        // 存储向量和对应的文本段
        private final List<EmbeddingWithSegment> storage = new ArrayList<>();

        /**
         * 添加向量和文本段到存储
         */
        public void add(Embedding embedding, TextSegment segment) {
            storage.add(new EmbeddingWithSegment(embedding, segment));
        }

        /**
         * 检索与目标向量最相似的Top-K结果
         */
        public List<EmbeddingMatch> findRelevant(Embedding targetEmbedding, int topK) {
            // 计算每个向量与目标向量的余弦相似度
            List<EmbeddingMatch> matches = new ArrayList<>();
            for (EmbeddingWithSegment ews : storage) {
                double score = cosineSimilarity(targetEmbedding.vector(), ews.embedding.vector());
                matches.add(new EmbeddingMatch(score, ews.segment));
            }

            // 按相似度降序排序，取Top-K
            return matches.stream()
                    .sorted((m1, m2) -> Double.compare(m2.score(), m1.score()))
                    .limit(topK)
                    .collect(Collectors.toList());
        }

        /**
         * 统计存储的向量数量
         */
        public int count() {
            return storage.size();
        }

        /**
         * 余弦相似度计算（向量相似度核心算法）
         */
        private double cosineSimilarity(float[] vec1, float[] vec2) {
            if (vec1.length != vec2.length) return 0.0;

            double dotProduct = 0.0;
            double norm1 = 0.0;
            double norm2 = 0.0;

            for (int i = 0; i < vec1.length; i++) {
                dotProduct += vec1[i] * vec2[i];
                norm1 += Math.pow(vec1[i], 2);
                norm2 += Math.pow(vec2[i], 2);
            }

            if (norm1 == 0 || norm2 == 0) return 0.0;
            return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        }

        /**
         * 内部类：绑定向量和文本段
         */
        private static class EmbeddingWithSegment {
            final Embedding embedding;
            final TextSegment segment;

            EmbeddingWithSegment(Embedding embedding, TextSegment segment) {
                this.embedding = embedding;
                this.segment = segment;
            }
        }

        /**
         * 适配的EmbeddingMatch类（替代官方类，避免导入问题）
         */
        public static class EmbeddingMatch {
            private final double score;
            private final TextSegment segment;

            public EmbeddingMatch(double score, TextSegment segment) {
                this.score = score;
                this.segment = segment;
            }

            public double score() {
                return score;
            }

            public TextSegment embedded() {
                return segment;
            }
        }
    }
}