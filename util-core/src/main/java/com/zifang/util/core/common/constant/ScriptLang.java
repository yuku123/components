package com.zifang.util.core.common.constant;

/**
 * @author: Ares
 * @time: 2022-08-02 10:50:18
 * @description: 脚本语言
 * @version: JDK 1.8
 */
public enum ScriptLang {
    /**
     * detail
     */
    JS("js", ""),
    GROOVY("groovy", "");

    ScriptLang(String langName, String templatePath) {
        this.langName = langName;
        this.templatePath = templatePath;
    }

    private final String langName;
    private final String templatePath;

    public String getLangName() {
        return langName;
    }

    public String getTemplatePath() {
        return templatePath;
    }
}
