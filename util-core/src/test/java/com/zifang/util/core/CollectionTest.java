package com.zifang.util.core;

import com.zifang.util.core.collections.MemorySqlExcutor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionTest {
    @Test
    public void a(){

        List<Map<String,Object>> aa = new ArrayList<>();

        List<Map<String,Object>> bb = new ArrayList<>();



        new MemorySqlExcutor()
        .setData(aa,"aa")
        .setData(bb,"bb")
        .select("select aa.*,bb.a from aa left join bb on aa.id = bb.id");
    }
}
