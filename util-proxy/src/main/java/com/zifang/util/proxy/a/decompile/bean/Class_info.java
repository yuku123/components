package com.zifang.util.proxy.a.decompile.bean;

import com.zifang.util.proxy.a.decompile.bean.attribute.Attribute_info;
import com.zifang.util.proxy.a.decompile.bean.constant.Constant_X_info;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Class_info {
    private String magic;
    private String minor_version;
    private String major_version;
    private int cp_count;
    private Map<Integer, Constant_X_info> constant_pool_Map;
    private String access_flag;
    private int this_class_index;
    private int super_class_index;
    private int interfaces_count;
    private List<Integer> interfacesList;
    private int fields_count;
    private List<Fields_info> fields_info_List;
    private int Methods_count;
    private List<Methods_info> methods_info_List;
    private int attributes_count;
    private List<Attribute_info> attributes;
}
