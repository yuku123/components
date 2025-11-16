package com.zifang.util.http.base.helper;

import com.zifang.util.core.lang.annoations.AnnotationUtil;
import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.http.base.define.*;
import com.zifang.util.http.base.pojo.HttpRequestBody;
import com.zifang.util.http.base.pojo.HttpRequestDefinition;
import com.zifang.util.http.base.pojo.HttpRequestHeader;
import com.zifang.util.http.base.pojo.HttpRequestLine;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 方法解析出http的请求
 */
public class HttpDefinitionSolver implements IDefinitionSolver {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    private Object proxy;

    private Method method;

    private Object[] args;

    private Class<?> target;

    private Map<String,Object> contextParams;

    private List<ParameterValuePair> parameterValuePairList = new ArrayList<>();

    // 标准定义实例
    @Getter
    private HttpRequestDefinition httpRequestDefinition = new HttpRequestDefinition();

    public void set(Class<?> target, Object proxy, Method method, Object[] args, Map<String,Object> contextParams) {
        this.target = target;
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.contextParams = contextParams;
    }

    public void solve() {

        // 前处理
        pre();

        // 处理请求行
        handleHttpRequestLine();

        // 处理请求头
        handleHttpRequestHeader();

        // 处理请求体
        handleHttpRequestBody();
    }

    private void handleHttpRequestBody() {
        HttpRequestBody httpRequestBody = new HttpRequestBody();
        for (ParameterValuePair parameterValuePair : parameterValuePairList) {
            if (parameterValuePair.getParameter().isAnnotationPresent(RequestBody.class)) {
                byte[] bytes = GsonUtil.objectToJsonStr(parameterValuePair.getObj()).getBytes();
                httpRequestBody.setBody(bytes);
            }
        }
        httpRequestDefinition.setHttpRequestBody(httpRequestBody);
    }

    private void pre() {
        // 生成 parameter -> 值的对象列表
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            parameterValuePairList.add(new ParameterValuePair(parameters[i], args[i]));
        }
    }

    private void handleHttpRequestHeader() {

        HttpRequestHeader headers =  new HttpRequestHeader();

        // 先处理对象类上的注解
        RequestHeaders requestHeadersFromObject = AnnotationUtil.getAnnotation(target, RequestHeaders.class);
        if(requestHeadersFromObject != null){

        }

        // 再处理方法上的注解
        RequestHeaders requestHeadersFromMethod = AnnotationUtil.getAnnotation(method, RequestHeaders.class);
        if(requestHeadersFromMethod != null){

        }

        httpRequestDefinition.setHttpRequestHeader(headers);
    }

    private void handleHttpRequestLine() {


        // 得到根路径
        String basicPath = AnnotationUtil.getAnnotationValue(target, RestController.class);

        // 查看当前的方法体的调用方式是什么
        RequestMethod requestMethod = AnnotationUtil.getAnnotationValue(method, RequestMapping.class, "method");

        // 当前方法的调用地址是什么
        String requestPath = AnnotationUtil.getAnnotationValue(method, RequestMapping.class, "value");

        // 检验是否有路径参数
        // 检验@requestParam
        List<String> requestParams = new ArrayList<>();
        for (ParameterValuePair parameterValuePair : parameterValuePairList) {
            String key = AnnotationUtil.getAnnotationValue(parameterValuePair.getParameter(), RequestParam.class);
            String value = String.valueOf(parameterValuePair.getObj());
            if (key != null) {
                requestParams.add(String.format("%s=%s", key, value));
            }
        }

        String url = basicPath + requestPath;
        if (!requestParams.isEmpty()) {
            url = url + "?" + String.join("&", requestParams);
        }

        url = solveReplaceHolder(url, contextParams);

        // 存储值
        HttpRequestLine httpRequestLine = new HttpRequestLine(); // 请求行的实例
        httpRequestLine.setUrl(url);
        httpRequestLine.setRequestMethod(requestMethod);
        // 设入值
        httpRequestDefinition.setHttpRequestLine(httpRequestLine);
    }

    private String solveReplaceHolder(String originalText, Map<String, Object> contextParams) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(originalText);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // 获取占位符名称（如 ${aaa} → "aaa"）
            String placeholderName = matcher.group(1);
            // 从 Map 中获取替换值，无对应值则保留原占位符
            String replacement = contextParams.getOrDefault(placeholderName, matcher.group(0)).toString();
            // 替换并追加到结果
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
