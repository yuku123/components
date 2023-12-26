package com.zifang.util.core.parser.json;


import com.zifang.util.core.parser.json.parser.Parser;
import com.zifang.util.core.parser.json.tokenizer.CharReader;
import com.zifang.util.core.parser.json.tokenizer.TokenList;
import com.zifang.util.core.parser.json.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author zifang
 * json解析器
 */
public class JSONParser {

    private Tokenizer tokenizer = new Tokenizer();

    private Parser parser = new Parser();

    public Object fromJSON(String json) throws IOException {
        CharReader charReader = new CharReader(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return parser.parse(tokens);
    }
}
