package com.zifang.util.core.parser.json.define;

/**
 * @author zifang
 *
 * json解析器接口
 */
public interface IJsonParser {

    IJsonObject parserJsonObject(String json);

    IJsonArray parserJsonArray(String json);

}
