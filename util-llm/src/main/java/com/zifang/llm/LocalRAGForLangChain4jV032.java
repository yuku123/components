package com.zifang.llm;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 适配你的配置：Windows MD文档 + 局域网Ollama服务（192.168.31.250）
 * 解决：上下文超限 + 分块死循环 + Windows路径适配 + 远程Ollama访问
 */
public class LocalRAGForLangChain4jV032 {

    // 你的专属配置（已替换为你的路径和Ollama地址）
    private static final String LOCAL_DOC_PATH = "D:\\workplace\\dedao_target\\2025\\2025-1\\表层的真理.md";
    private static final String OLLAMA_BASE_URL = "http://192.168.31.250:11434";
    private static final String EMBEDDING_MODEL_NAME = "nomic-embed-text";
    // 适配模型上下文的安全配置
    private static final int CHUNK_SIZE = 256;    // 字符数（≈80-100 Token，远低于模型限制）
    private static final int CHUNK_OVERLAP = 32;  // 重叠字符数
    private static final int MAX_SAFE_LENGTH = 800; // 兜底长度阈值

    public static void main(String[] args) {
        try {
            // 前置检查：验证Ollama服务可达性（新增）
            if (!isOllamaReachable()) {
                System.err.println("❌ 无法连接到Ollama服务：" + OLLAMA_BASE_URL);
                System.err.println("请检查：1.Ollama服务已启动 2.IP/端口正确 3.防火墙允许访问");
                return;
            }

            // 1. 加载Windows下的MD文档
            Document document = loadLocalDocument(LOCAL_DOC_PATH);
            System.out.println("✅ 文档加载完成，原始文本长度：" + document.text().length() + " 字符");

            // 2. 安全分块（适配MD文档，避免超限）
            List<Document> documentChunks = splitDocumentIntoSafeChunks(document.text());
            System.out.println("✅ 文档Chunk化完成，共生成 " + documentChunks.size() + " 个文本块");

            // 3. 初始化远程Ollama嵌入模型
            EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl(OLLAMA_BASE_URL)
                    .modelName(EMBEDDING_MODEL_NAME)
                    .timeout(Duration.of(3, ChronoUnit.MINUTES)) // 增加超时时间（适配远程访问）
                    .build();

            // 4. 安全生成向量（单个块失败不影响整体）
            generateEmbeddingsSafely(documentChunks, embeddingModel);

        } catch (IOException | TikaException e) {
            System.err.println("❌ 文档处理失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 新增：验证Ollama服务是否可达（简单HTTP检测）
     */
    private static boolean isOllamaReachable() {
        try {
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(OLLAMA_BASE_URL + "/api/tags").openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int responseCode = conn.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 加载Windows下的MD文档（适配路径 + Tika解析Markdown）
     */
    private static Document loadLocalDocument(String docPath) throws IOException, TikaException {
        // 适配Windows路径：替换可能的转义问题（可选，你的路径写法已正确）
        File docFile = new File(docPath.replace("\\", File.separator));

        // 前置校验
        if (!docFile.exists()) {
            throw new IOException("文档不存在，请检查路径：" + docPath);
        }
        if (!docFile.canRead()) {
            throw new IOException("无文档读取权限：" + docPath);
        }

        // Tika自动解析Markdown为纯文本（无需额外配置）
        Tika tika = new Tika();
        String textContent = tika.parseToString(docFile);
        // 清理MD格式符号（可选，提升文本纯净度）
        textContent = textContent.replaceAll("#+ ", "") // 移除MD标题符号
                .replaceAll("\\*+", "") // 移除粗体/斜体符号
                .replaceAll("\\s+", " ")
                .trim();

        if (textContent.isEmpty()) {
            throw new IOException("MD文档解析后无有效文本");
        }

        // 封装元数据（记录Windows路径）
        Metadata metadata = new Metadata();
        metadata.add("file_path", docFile.getAbsolutePath());
        metadata.add("file_name", docFile.getName());
        metadata.add("file_type", "markdown");

        return Document.from(textContent, metadata);
    }

    /**
     * 安全分块逻辑（适配MD文本，无递归重复拆分）
     */
    private static List<Document> splitDocumentIntoSafeChunks(String text) {
        List<Document> chunks = new ArrayList<>();
        String[] mainSeparators = {"\n\n", "\n", "。", "！", "？", "；", "，", " "};

        // 第一步：按自然分隔符拆分基础块（优先MD文档的换行分隔）
        List<String> baseChunks = new ArrayList<>();
        splitBySeparators(text, mainSeparators, 0, baseChunks);

        // 第二步：超长块硬分割 + 兜底校验
        for (String baseChunk : baseChunks) {
            String trimmedChunk = baseChunk.trim();
            if (trimmedChunk.isEmpty()) continue;

            if (trimmedChunk.length() <= CHUNK_SIZE) {
                chunks.add(Document.from(trimmedChunk));
            } else {
                splitLongChunkHard(trimmedChunk, chunks);
            }
        }

        // 过滤超长块（避免模型报错）
        chunks.removeIf(chunk -> chunk.text().length() > MAX_SAFE_LENGTH);
        return chunks;
    }

    /**
     * 按自然分隔符拆分（非递归，适配MD文档）
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
            if (!chunk.isEmpty()) {
                chunks.add(Document.from(chunk));
            }

            if (end >= textLength) break;
            int nextStart = end - CHUNK_OVERLAP;
            start = Math.max(nextStart, start + 1);
        }
    }

    /**
     * 安全生成向量（适配远程Ollama服务，增加超时容错）
     */
    private static void generateEmbeddingsSafely(List<Document> chunks, EmbeddingModel model) {
        int successCount = 0;
        int failCount = 0;

        for (int i = 0; i < chunks.size(); i++) {
            Document chunk = chunks.get(i);
            try {
                // 调用远程Ollama生成向量
                Embedding embedding = model.embed(chunk.text()).content();
                float[] vector = embedding.vector();

                // 输出向量信息
                System.out.println("\n===== 文本块 " + (i + 1) + " =====");
                String preview = chunk.text().substring(0, Math.min(100, chunk.text().length())) + "...";
                System.out.println("文本片段：" + preview);
                System.out.println("向量维度：" + vector.length); // nomic-embed-text默认768维

                StringBuilder first3 = new StringBuilder("[");
                for (int j = 0; j < Math.min(3, vector.length); j++) {
                    first3.append(vector[j]);
                    if (j < 2) first3.append(", ");
                }
                first3.append("]");
                System.out.println("向量前3个值：" + first3);
                successCount++;

            } catch (Exception e) {
                System.err.println("\n❌ 文本块 " + (i + 1) + " 向量生成失败：" + e.getMessage());
                failCount++;
                continue;
            }
        }

        System.out.println("\n✅ 向量生成完成！成功：" + successCount + " 个，失败：" + failCount + " 个");
    }
}