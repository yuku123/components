package com.zifang.util.db;


import com.zifang.util.db.entity1.User;
import com.zifang.util.db.respository.CrudRepository;

import java.util.List;

public interface TestInterface extends CrudRepository<User,Long> {

    List<User> findByName(String name);

}
