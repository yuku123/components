package com.zifang.util.core.meta;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PaginationResponse<T> extends BaseResponse {
    private T data;//分页结果
    private int limit;//每页条数
    private int current;//当前页
    private int total;//总数
}
