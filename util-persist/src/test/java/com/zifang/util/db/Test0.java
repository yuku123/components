package com.zifang.util.db;

import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.db.context.DataSourceContext;
import com.zifang.util.db.context.PersistentContext;
import com.zifang.util.db.respository.RepositoryProxy;
import com.zifang.util.db.test0.MysqlDatasourceFactory;
import com.zifang.util.db.test0.ResourceItem;
import com.zifang.util.db.test0.ResourceItemRepository;
import com.zifang.util.db.transation.TranslationManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


@Slf4j
public class Test0 {

    @Before
    public void init(){

        // 数据库上下文
        DataSourceContext dataSourceContext = new DataSourceContext()
                .scanPackage("com.zifang.util.db")
                .transationManager(new TranslationManager())      //事务管理器
                .dataSourceFactory(new MysqlDatasourceFactory());

        // 注册数据库信息
        PersistentContext.registerDatasourceContext(PersistentContext.DEFAULT, dataSourceContext);
    }

    @Test
    public void test() {

        ResourceItemRepository resourceItemRepository = RepositoryProxy.proxy(ResourceItemRepository.class);

        List<ResourceItem> resourceItems = resourceItemRepository.findByName("aa");

        log.debug(GsonUtil.objectToJsonStr(resourceItems));

    }
}
