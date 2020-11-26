package com.zifang.util.core.pattern.memento;

import java.util.LinkedList;
import java.util.List;

/**
 * 备忘录的上下文
 *
 * @author zifang
 *
 * */
public class MementoContext<T> {

    /**
     * 状态存储列表
     * */
    private List<T> mementoList = new LinkedList<>();

    /**
     * 状态指针
     * */
    private Integer pointer = -1;

    // 备忘录上下文的构造器
    public MementoContext(){

    }

    /**
     * 保存当前状态
     * */
    public void save(T t){
        pointer = pointer + 1;
        mementoList.set(pointer,t);
    }

    /**
     * 恢复 下一步的操作
     * */
    public T next(){
        pointer = pointer + 1;
        return mementoList.get(pointer);
    }

    /**
     * 恢复 上一步操作
     * */
    public T resume(){
        pointer = pointer - 1;
        return mementoList.get(pointer);
    }
}
