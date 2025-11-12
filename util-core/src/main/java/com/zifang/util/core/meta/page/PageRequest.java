package com.zifang.util.core.meta.page;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zifang.util.core.meta.BaseRequest;
import com.zifang.util.core.meta.KeepLongSerializer;
import com.zifang.util.core.meta.SortField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 分页请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageRequest extends BaseRequest {
    private static final long serialVersionUID = -8059516751569851680L;

    @JsonSerialize(using = KeepLongSerializer.class)
    private Long current = 1L;

    @JsonSerialize(using = KeepLongSerializer.class)
    private Long size = 10L;

    private List<SortField> orders = new ArrayList<>();

    public static PageRequest of(Long current, Long size){
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrent(current);
        pageRequest.setSize(size);
        return pageRequest;
    }

    public static PageRequest of(Long current, Long size, List<SortField> orders){
        PageRequest pageRequest = of(current, size);
        pageRequest.setOrders(orders);
        return pageRequest;
    }

    /**
     * 添加新的排序条件
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public PageRequest addOrder(List<SortField> items) {
        orders.addAll(items);
        return this;
    }

    /**
     * 移除符合条件的条件
     *
     * @param filter 条件判断
     */
    public void removeOrder(Predicate<SortField> filter) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            if (filter.test(orders.get(i))) {
                orders.remove(i);
            }
        }
    }

    /**
     * 查找 order 中正序排序的字段数组
     *
     * @param filter 过滤器
     * @return 返回正序排列的字段数组
     */
    public String[] mapOrderToArray(Predicate<SortField> filter) {
        List<String> columns = new ArrayList<>(orders.size());
        orders.forEach(i -> {
            if (filter.test(i)) {
                columns.add(i.getColumn());
            }
        });
        return columns.toArray(new String[0]);
    }

}
