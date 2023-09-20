package com.zifang.util.core.meta;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GenericResponse<T> extends BaseResponse {
    private T data;
}
