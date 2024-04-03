package com.zifang.util.json.define;

/**
 * @author zifang
 * <p>
 * json解析器接口
 */
public interface IJsonParser {

    IJsonObject parserJsonObject(String json);

    IJsonArray parserJsonArray(String json);

}
