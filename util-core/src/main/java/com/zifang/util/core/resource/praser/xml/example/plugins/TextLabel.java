package com.zifang.util.core.resource.praser.xml.example.plugins;

import com.zifang.util.praser.xml.example.plugins.Widget;

public class TextLabel
    implements Widget
{
    private String id = "anonymous";

    private String label = "nolabel";

    public TextLabel()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel( String label )
    {
        this.label = label;
    }
}