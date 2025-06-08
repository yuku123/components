package com.zifang.util.core.meta.page;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zifang.util.core.meta.BaseRequest;
import com.zifang.util.core.meta.KeepLongSerializer;
import com.zifang.util.core.meta.SortField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PageRequest extends BaseRequest {

    private static final long serialVersionUID = -8059516751569851680L;

    @JsonSerialize(using = KeepLongSerializer.class)
    private Long current = 1L;

    @JsonSerialize(using = KeepLongSerializer.class)
    private Long size = 10L;
    private List<SortField> orders = new ArrayList<>();

    public PageRequest() {
    }

    public PageRequest(Long current, Long size) {
        this.size = size;
        this.current = current;
    }

    public PageRequest(Long current, Long size, List<SortField> orders) {
        this.size = size;
        this.current = current;
        this.orders = orders;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public List<SortField> getOrders() {
        return orders;
    }

    public void setOrders(List<SortField> orders) {
        this.orders = orders;
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

    public List<SortField> orders() {
        return getOrders();
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
