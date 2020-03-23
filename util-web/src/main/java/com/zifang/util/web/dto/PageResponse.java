package com.zifang.util.web.dto;

import com.zifang.util.web.ResultCode;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PageResponse extends CommonResponse {

    private Integer currentIndex;//当前页

    private Integer totalPage;//全量的页数

    //每页的数量
    private int pageSize;
    //当前页的数量
    private int size;

    private List<Map<String, Object>> data;

    public PageResponse(ResultCode resultCode, List<Map<String, Object>> data, Integer currentIndex, Integer totalPage) {
        super(resultCode);
        this.data = data;
    }

    public PageResponse(ResultCode resultCode, List<Map<String, Object>> data) {
        super(resultCode);
        this.data = data;
    }
}
