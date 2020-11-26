package com.zifang.util.core.pattern.chain;


/**
 * 每次进行过滤的时候 都要有个 in 对应 -> out类型
 *
 * @author zifang
 *
 * */
public interface  IFilter<I,O> {

    O filter(I i);

}
