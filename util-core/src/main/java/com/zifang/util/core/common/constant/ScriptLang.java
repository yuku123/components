package com.zifang.util.core.common.constant;

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
