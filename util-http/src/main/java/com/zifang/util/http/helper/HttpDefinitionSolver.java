package com.zifang.util.http.helper;

import com.zifang.util.core.annoations.AnnotationUtil;
import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.http.common.ParameterValuePair;
import com.zifang.util.http.define.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法解析出http的请求
 * */
public class HttpDefinitionSolver implements IDefinitionSolver{

    private Object proxy;

    private Method method;

    private Object[] args;

    private Class target;

    private List<ParameterValuePair> parameterValuePairList = new ArrayList<>();

    // 标准定义实例
    private HttpRequestDefinition httpRequestDefinition = new HttpRequestDefinition();

    public void set(Class target, Object proxy, Method method, Object[] args) {
        this.target = target;
        this.proxy = proxy;
        this.method = method;
        this.args = args;
    }

    public void solve() {
        pre(); //前处理
        handleHttpRequestLine(); // 处理请求行
        handleHttpRequestHeader(); // 处理请求头
        handleHttpRequestBody();
    }

    private void handleHttpRequestBody() {
        HttpRequestBody httpRequestBody = new HttpRequestBody();
        for(ParameterValuePair parameterValuePair : parameterValuePairList){
            if(parameterValuePair.getParameter().isAnnotationPresent(RequestBody.class)){
                byte[] bytes = GsonUtil.objectToJsonStr(parameterValuePair.getObj()).getBytes();
                httpRequestBody.setBody(bytes);
            }
        }
        httpRequestDefinition.setHttpRequestBody(httpRequestBody);
    }

    private void pre() {
        // 生成 parameter -> 值的对象列表
        Parameter[] parameters = method.getParameters();
        for(int i=0; i<parameters.length;i++){
            parameterValuePairList.add(new ParameterValuePair(parameters[i],args[i]));
        }

    }

    private void handleHttpRequestHeader() {

    }

    private void handleHttpRequestLine() {

        HttpRequestLine httpRequestLine = new HttpRequestLine(); // 请求行的实例

        // 得到根路径
        String basicPath = AnnotationUtil.getAnnotationValue(target, RestController.class);

        // 查看当前的方法体的调用方式是什么
        RequestMethod requestMethod = AnnotationUtil.getAnnotationValue(method, RequestMapping.class,"method");

        // 当前方法的调用地址是什么
        String requestPath = AnnotationUtil.getAnnotationValue(method, RequestMapping.class,"value");

        // 检验是否有路径参数
        // 检验@requestParam
        List<String> requestParams = new ArrayList<>();
        for(ParameterValuePair parameterValuePair : parameterValuePairList){
            String key = AnnotationUtil.getAnnotationValue(parameterValuePair.getParameter(), RequestParam.class);
            String value = String.valueOf(parameterValuePair.getObj());
            if(key != null){
                requestParams.add(String.format("%s=%s",key,value));
            }
        }

        String url = basicPath + requestPath;
        if(requestParams.size()>0){
            url = url + "?" + String.join("&",requestParams);
        }

        // 存储值
        httpRequestLine.setUrl(url);
        httpRequestLine.setRequestMethod(requestMethod);
        // 设入值
        httpRequestDefinition.setHttpRequestLine(httpRequestLine);
    }


    public HttpRequestDefinition getHttpRequestDefinition() {
        return this.httpRequestDefinition;
    }
}
