package com.zifang.util.db.test0;


import com.zifang.util.db.define.Param;
import com.zifang.util.db.define.Select;
import com.zifang.util.db.respository.CrudRepository;

import java.util.List;

public interface ResourceItemRepository extends CrudRepository<ResourceItem, Long> {

    @Select("select * from resource_item where name = ${name}")
    List<ResourceItem> findByName(@Param("name")String name);
}
