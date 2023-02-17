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
import java.util.Map;


@Slf4j
public class Test0 {

    @Before
    public void init() {

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

        List<ResourceItem> r1 = resourceItemRepository.findByNameList("5e8888e8be7fff746fb26b5a", 0);
        List<Map<String, Object>> r2 = resourceItemRepository.findByNameListMap("5e8888e8be7fff746fb26b5a", 0);
        ResourceItem r3 = resourceItemRepository.findByNameBean("5e8888e8be7fff746fb26b5a", 0);
        Map<String, Object> r4 = resourceItemRepository.findByNameMap("5e8888e8be7fff746fb26b5a", 0);

        log.info(GsonUtil.objectToJsonStr(r1));
        log.info(GsonUtil.objectToJsonStr(r2));
        log.info(GsonUtil.objectToJsonStr(r3));
        log.info(GsonUtil.objectToJsonStr(r4));

        log.info("结束");

    }

    @Test
    public void test1() {
        ResourceItemRepository resourceItemRepository = RepositoryProxy.proxy(ResourceItemRepository.class);

        ResourceItem resourceItem = new ResourceItem();
        resourceItem.setCmsId("xxxxx");
        resourceItem.setShared(false);
        resourceItemRepository.save(resourceItem);
        resourceItemRepository.findById(resourceItem.getId());
        resourceItemRepository.deleteById(resourceItem.getId());
    }
}
