package com.zifang.util.devops.nexus;

import com.zifang.util.json.JsonUtil;
import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Nexus组件管理器
 * <p>
 * 提供Nexus仓库组件的搜索、查询、删除等管理功能，
 * 支持GAV坐标查询和组件存在性检查。
 *
 * <p><b>凭据来源（2026-10-24 起）</b>：本类**不再内置任何 Nexus 地址 / 账号 / 口令**。
 * 三项配置只能经两条途径注入，缺任一项即**启动失败**（fail-fast）：
 * <ol>
 *     <li>构造器注入：{@code new NexusComponentManager(url, username, password)}；</li>
 *     <li>环境变量：{@link #fromEnvironment()} 读取 {@code NEXUS_URL} /
 *         {@code NEXUS_USERNAME} / {@code NEXUS_PASSWORD}。</li>
 * </ol>
 * <b>不留任何兜底默认值</b> —— 留默认值等于把凭据换个地方继续公开。
 *
 * @author zifang
 * @version 1.0.0
 */
public class NexusComponentManager {

    public static final String respository = "maven-releases";
    /**
     * 环境变量名：Nexus 访问地址
     */
    public static final String ENV_NEXUS_URL = "NEXUS_URL";
    /**
     * 环境变量名：Nexus 账号
     */
    public static final String ENV_NEXUS_USERNAME = "NEXUS_USERNAME";
    /**
     * 环境变量名：Nexus 口令
     */
    public static final String ENV_NEXUS_PASSWORD = "NEXUS_PASSWORD";

    /**
     * nexus访问地址（由调用方注入）
     */
    private final String nexusUrl;
    /**
     * nexus账号（由调用方注入）
     */
    private final String nexusUsername;
    /**
     * nexus口令（由调用方注入）
     */
    private final String nexusPassword;
    /**
     * 成功删除的组件数量
     */
    private int successDeleteTotal;

    /**
     * 复用的 OkHttp 客户端，OkHttp 推荐全局共用同一实例（内部已做连接池与线程池管理）。
     */
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    /**
     * 构造器注入三项配置。
     * <p>
     * **fail-fast**：任一项为 null / 空白 ⇒ 立即抛异常，**不做任何默认兜底**。
     *
     * @param nexusUrl      nexus 访问地址
     * @param nexusUsername nexus 账号
     * @param nexusPassword nexus 口令
     */
    public NexusComponentManager(String nexusUrl, String nexusUsername, String nexusPassword) {
        this.nexusUrl = requirePresent(nexusUrl, "nexusUrl");
        this.nexusUsername = requirePresent(nexusUsername, "nexusUsername");
        this.nexusPassword = requirePresent(nexusPassword, "nexusPassword");
    }

    /**
     * 从环境变量构造。
     * <p>
     * 读取 {@code NEXUS_URL} / {@code NEXUS_USERNAME} / {@code NEXUS_PASSWORD}；
     * 任一项缺失或为空白 ⇒ 抛 {@link IllegalStateException}（**fail-fast，不兜底**）。
     *
     * @return 配置齐备的管理器实例
     */
    public static NexusComponentManager fromEnvironment() {
        return fromEnvironment(System::getenv);
    }

    /**
     * 从任意「环境变量查询函数」构造（便于测试注入假的环境变量）。
     *
     * @param envLookup 环境变量查询函数
     * @return 配置齐备的管理器实例
     */
    static NexusComponentManager fromEnvironment(Function<String, String> envLookup) {
        return new NexusComponentManager(
                requireEnv(envLookup, ENV_NEXUS_URL),
                requireEnv(envLookup, ENV_NEXUS_USERNAME),
                requireEnv(envLookup, ENV_NEXUS_PASSWORD));
    }

    /**
     * 读取必需的环境变量；缺失或为空白 ⇒ 抛 {@link IllegalStateException}（**fail-fast，不兜底**）。
     *
     * @param envLookup 环境变量查询函数
     * @param name      环境变量名
     * @return 环境变量值
     */
    private static String requireEnv(Function<String, String> envLookup, String name) {
        String value = envLookup.apply(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "缺少必需的环境变量 " + name + "（fail-fast：不提供兜底默认值）");
        }
        return value;
    }

    /**
     * 校验配置项非空。
     *
     * @param value 配置值
     * @param name  配置名（用于报错信息）
     * @return 原值
     */
    private static String requirePresent(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Nexus 配置项 " + name + " 缺失或为空（fail-fast：不提供兜底默认值，"
                            + "请经构造器注入或环境变量提供）");
        }
        return value;
    }

    /**
     * 构造带 Basic Auth 头的 Request.Builder。
     */
    private Request.Builder authRequest(String url) {
        return new Request.Builder()
                .url(url)
                .header("Authorization", Credentials.basic(nexusUsername, nexusPassword));
    }

    /**
     * 搜索组件
     *
     * @param repository 仓库名称
     * @return
     */
    public List<Component> search(String repository) {
        final List<Component> list = new ArrayList<>();
        search(repository, list, null);
        return list;
    }

    /**
     * 搜索组件
     * <p>
     * 搜索组件时API会对匹配结果进行分页，所以这里采用递归搜索
     *
     * @param repository 仓库名称
     * @param list       存放查询结果的集合
     * @param token      token，由上一页查询结果中获取，第一次查询传null
     */
    private void search(String repository, List<Component> list, String token) {
        String url = String.format("%s/service/rest/v1/search?repository=%s", nexusUrl, repository);
        if (token != null) {
            url += "&continuationToken=" + token;
        }
        try (Response response = HTTP_CLIENT.newCall(authRequest(url).get().build()).execute()) {
            int status = response.code();
            String body = response.body() != null ? response.body().string() : "";
            if (status == 200) {
                JsonObject jsonObject = JsonUtil.parseObject(body);
                JsonArray itemsArray = jsonObject.getJsonArray("items");
                List<Component> temp = JsonUtil.fromJson(itemsArray.toString(), new TypeReference<List<Component>>() {});
                list.addAll(temp);
                if (jsonObject.get("continuationToken") != null) {
                    token = String.valueOf(jsonObject.get("continuationToken"));
                    //search(repository, groupId, artifactId, list, token);
                }
            } else {
                System.out.println(String.format("组件搜索出错，http响应代码：%d，错误信息：%s", status, response.message()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("组件搜索出错，IO 异常：" + e.getMessage());
        }
    }

    /**
     * 删除组件
     *
     * @param component 组件对象
     */
    public void delete(Component component) {
        String url = String.format("%s/service/rest/v1/components/%s", nexusUrl, component.getId());
        try (Response response = HTTP_CLIENT.newCall(authRequest(url).delete().build()).execute()) {
            int status = response.code();
            if (status == 204) {
                successDeleteTotal++;
            } else {
                System.out.println(String.format("组件【%s-%s-%s】删除失败，http响应代码：%d",
                        component.getRepository(), component.getGroup(), component.getName(), component.getVersion(), status));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(String.format("组件【%s-%s-%s】删除失败，IO 异常：%s",
                    component.getRepository(), component.getGroup(), component.getName(), component.getVersion(), e.getMessage()));
        }
    }

    /**
     * 根据 GAV 坐标查找组件
     *
     * @param groupId    组ID
     * @param artifactId 构件ID
     * @param version    版本号
     * @return 匹配的组件对象，未找到返回 null
     */
    public Component findByGav(String groupId, String artifactId, String version) {

        List<Component> list = search(respository);

        List<Component> filted = list.stream()
                .filter(e -> e.getGroup().equals(groupId) && e.getName().equals(artifactId) && e.getVersion().equals(version))
                .collect(Collectors.toList());

        if (filted.size() == 1) {
            return filted.get(0);
        }
        return null;
    }

    /**
     * 检查指定 GAV 坐标的组件是否存在
     *
     * @param groupId    组ID
     * @param artifactId 构件ID
     * @param version    版本号
     * @return 存在返回 true，不存在返回 false
     */
    public Boolean checkExistGav(String groupId, String artifactId, String version) {

        List<Component> list = search(respository);

        System.out.print(list);

        List<Component> filted = list.stream()
                .filter(e -> e.getGroup().equals(groupId) && e.getName().equals(artifactId) && e.getVersion().equals(version))
                .collect(Collectors.toList());
        if (filted.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

}
