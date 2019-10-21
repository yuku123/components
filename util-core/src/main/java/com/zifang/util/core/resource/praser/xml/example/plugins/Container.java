package com.zifang.util.core.resource.praser.xml.example.plugins;

import com.zifang.util.praser.xml.example.plugins.Widget;

import java.util.LinkedList;
import java.util.List;

public class Container
    implements com.zifang.util.praser.xml.example.plugins.Widget
{
    private LinkedList<com.zifang.util.praser.xml.example.plugins.Widget> children = new LinkedList<com.zifang.util.praser.xml.example.plugins.Widget>();

    public Container()
    {
    }

    public void addChild( com.zifang.util.praser.xml.example.plugins.Widget child )
    {
        children.add( child );
    }

    public List<Widget> getChildren()
    {
        return children;
    }
}