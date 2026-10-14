package com.zifang.util.wf.kernel.engine.interfaces;

import com.zifang.util.core.pattern.register.Register;
import com.zifang.util.wf.kernel.config.Engine;

import java.util.HashMap;
import java.util.Map;

/**
 * 引擎工厂类，负责创建和管理不同类型的执行引擎。
 * <p>
 * 注册模型：kernel 不强依赖任何 executor 子模块。executor 子模块在自己的静态初始化块中调用
 * {@link #register(String, Class)} 把引擎类挂进来，避免 reactor cycle。
 *
 * @see AbstractEngine
 */
public class EngineFactory {

    /**
     * 引擎实例缓存池（type -> 已初始化实例）
     */
    public static Map<String, AbstractEngine> engineCache = new HashMap<>();

    /**
     * 已注册的引擎类型映射表（type -> engine class）。由各 executor 子模块自注册。
     * 用 {@link Register} 而非裸 Map，好处是拿到「拒 null key」这条契约 ——
     * 自注册表里一个 null type 会一路传播到 getEngine 才炸。
     */
    private static final Register<String, Class<? extends AbstractEngine>> registeredEngines =
            new Register<>();

    /**
     * 注册一种引擎实现。重复注册后者覆盖前者。
     */
    public static void register(String type, Class<? extends AbstractEngine> clazz) {
        registeredEngines.register(type, clazz);
    }

    /**
     * 根据引擎配置获取引擎实例。
     *
     * @param engine 引擎配置
     * @return 引擎实例，如果类型未注册返回 null
     */
    public static AbstractEngine getEngine(Engine engine) {
        String type = engine.getType();
        if (engineCache.containsKey(type)) {
            return engineCache.get(type);
        }
        Class<? extends AbstractEngine> clazz = registeredEngines.get(type);
        if (clazz == null) {
            return null;
        }
        try {
            AbstractEngine abstractEngine = clazz.newInstance();
            abstractEngine.setMode(engine.getMode());
            abstractEngine.setConfiguration(engine.getProperties());
            abstractEngine.doInitial();
            engineCache.put(type, abstractEngine);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return engineCache.get(type);
    }
}
