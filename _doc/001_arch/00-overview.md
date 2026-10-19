# 项目总览

## 1. 项目简介

z-util 是 z-opc-foundation 的**基础库** —— 面向日常 Java 开发的多模块工具库集。它解决的问题是
「同一件小事在全站被写 N 遍」：组织内其余仓（z-boot starter 家族、z-opc、z-ctc、z-mq、z-gw…）
要用的字符串/集合/IO/加密/JWT、JSON 与多格式解析、表达式与对象整形 DSL、数据源与动态查询、
缓存、IoC/AOP/代理、二进制序列化、监控、Office、图像、数学与 ML，全部收在这里。

**技术栈：** Java 8（根 POM `compile.version=8`，各模块 `maven.compiler.source/target=8`）+
Maven 多模块 + JUnit Jupiter 5.11.4（带 `junit-vintage-engine` 跑历史 JUnit4 用例）
+ ANTLR `.g4` 语法（**21** 个 `.g4` 文件在本仓内）

**坐标与版本：** `io.github.yuku123:z-util:${revision}`（`packaging=pom`），根 POM
`<properties><revision>1.2.8</revision>`。**本仓无 parent** —— 2026-10-03 起根 POM 不写
`<parent>`、也不 import 外部 BOM，第三方版本全部由本仓根 POM 的 `<properties>` +
`<dependencyManagement>`（**121** 条）自供，发布出去的根 pom 在 repo1 可被独立解析。

**盘面实测：** 仓内共 **71** 个 `pom.xml`（根 1 + 模块 70），其中 **62** 个 `packaging=jar`、
**8** 个 `packaging=pom`（纯聚合件）；根 POM 的 `<modules>` 有 **26** 个条目。
受版本控制的 `.java` 共 **1,993** 个（`src/main/java` 下 1,278、`src/test/java` 下 699，
另有 16 个在 `src/*/resources` 下作测试夹具或参考样例）。

## 2. 模块架构

```
z-util/
├── pom.xml                       # 聚合根：无 parent，revision=1.2.8，<modules> 26 条
├── z-util-core/                  # 基础件：bean/encrypt/io/json/jwt/lang/lock/meta/net/ratelimit/
│                                 #   resilience/state/time/trace/util（254 个 main java）
├── z-util-dsl/                   # 语言处理家族（pom 聚合）
│   ├── z-util-dsl-kernel/        # 自研词法/语法/AST + 运行时动态加载 .g4（15）
│   ├── z-util-expr/              # 表达式家族（pom 聚合，6 个引擎）
│   │   ├── z-util-expr-el/       #   自研 EL：${a} / 算术 / 比较 / 三元
│   │   ├── z-util-expr-js/       #   自研 JS 引擎雏形（lexer + instruction）
│   │   ├── z-util-expr-groovy/   #   Groovy 脚本扩展点
│   │   ├── z-util-expr-lua/      #   Lua 脚本扩展点
│   │   ├── z-util-expr-sql/      #   内存 SQL：把行数据当表查（join / group by / 聚合）
│   │   └── z-util-expr-obj/      #   对象整形：二维表 → 任意高维结构（15 个算子）
│   └── z-util-parser/            # 格式解析家族（pom 聚合，8 种格式）
│       ├── z-util-parser-json/   #   JSONParser + JsonObject/JsonArray + 序列化注解
│       ├── z-util-parser-xml/    #   XDocument / XElement / XAttribute + XPath
│       ├── z-util-parser-yaml/   #   YamlG4Parser + SnakeYamlBackend 门面
│       ├── z-util-parser-csv/    #   CsvReader / CsvWriter / 编码探测
│       ├── z-util-parser-toml/   #   TomlParser / TomlG4Parser
│       ├── z-util-parser-ini/    #   IniParser / IniG4Parser
│       ├── z-util-parser-properties/
│       └── z-util-parser-proto/  #   ProtoParser / ProtoG4Parser
├── z-util-wf/                    # 内存工作流家族（pom 聚合）
│   └── z-util-wf-kernel/         # DAG 模型 + BPMN 解析 + runtime + persistence + interfaces（31）
├── z-util-http/                  # HTTP 家族（jar + 3 个子件）
│   ├── z-util-http-core/         #   注解式客户端 / curl 解析 / SSE（19）
│   ├── z-util-http-proxy/        #   代理（30）
│   └── z-util-http-server/       #   简易 HTTP 服务（基于 JDK com.sun.net.httpserver，6）
├── z-util-jdbc/                  # 数据源注册 + 多方言动态查询 + 内存 SQL + 极简 ORM（69）
├── z-util-pattern/               # GoF + 行为模式 + 校验原语（pom 聚合，18 个子件）
│   ├── z-util-pattern-cache/     #   进程内缓存：TTL + LRU / W-TinyLFU + builder
│   ├── z-util-pattern-chain/ …   #   chain / command / composite / event / factory / ioc /
│   │                             #   memento / pool / register / spi / state / strategy /
│   │                             #   stream / template / validation / visitor / builder
├── z-util-serialize/             # 跨语言 schema-first 编解码（pom 聚合，5 个子件）
│   ├── z-util-serialize-core/    #   ZSerializer / ZDeserializer / Codec / SchemaRegistry
│   ├── z-util-serialize-codegen/ #   注解处理器（ZSerializeProcessor）
│   ├── z-util-serialize-formats/ #   JsonTranscoder
│   ├── z-util-serialize-it/      #   基准测试件（仅 src/test 下有类）
│   └── z-util-serialize-schema/  #   schema 定义
├── z-util-bc/                    # 字节码 + 源码工具集：ASM 编织 / 内存编译 / javaparser（139）
├── z-util-devops/                # DevOps 三件（pom 聚合）
│   ├── z-util-devops-git/        #   Git（JGit / Shell）+ GitHub API（33）
│   ├── z-util-devops-docker/     #   Docker 客户端（7）
│   └── z-util-devops-nexus/      #   Nexus REST（5）
├── z-util-ml/                    # 机器学习（教学/练手性质，98 个 main java）
├── z-util-monitor/               # JVM / 线程池 / OS / 网络监控 + 指标导出（33）
├── z-util-office/                # POI / PDFBox：Word / Excel / PDF / PPT + 模板引擎（17）
├── z-util-math/                  # NumPy/Pandas 风格数据结构（34）
├── z-util-visualization/         # Swing 图表与算法可视化（38）
├── z-util-ioc/                   # 轻量 IoC 容器（Guice 形接口 + JSR-330）+ AOP 集成（35）
├── z-util-aop/                   # @Advise / @Intercept + ProxyFactory（15，仅依赖 core 与 bc）
├── z-util-distribute/            # 分布式 ID：Snowflake / Segment / NanoId / UUID v7（6）
├── z-util-strategy/              # 决策引擎：决策包 / 灰度 / 试运行 / 对照组 / 可解释 trace（25）
├── z-util-sensitive/             # 敏感识别引擎：敏感词 DFA + PII 校验位 + 脱敏（16）
├── z-util-cli/                   # 命令行解析（DefaultParser + OptionGroup + HelpFormatter，17）
├── z-util-ch/                    # 中文工具：拼音 / 身份证 / 金额 / 星期（5）
├── z-util-all/                   # 总装件（pom umbrella）：只含 <dependencyManagement>，不产 jar
└── _doc/                         # 文档，见文末「文档目录」
```

## 3. 核心功能

### 3.1 基础件 `z-util-core`

集合 / 字符串 / IO / 并发 / 加密 / JWT / 限流 / 熔断 / 调度 / 反射 / 序列化工具集。关键入口
（实测存在的类）：`jwt.Jwt` / `jwt.Claims` / `jwt.HmacSha256`、`encrypt.AesUtil` / `encrypt.RsaUtil` /
`encrypt.ApiSignUtil`、`ratelimit.SlidingWindowRateLimiter` / `ratelimit.TokenBucketRateLimiter`、
`resilience.CircuitBreaker` / `resilience.Bulkhead` / `resilience.TimeLimiter`、
`meta.page.PageResult`、`lang.concurrency.NameThreadFactory`。

### 3.2 语言处理家族 `z-util-dsl`（kernel / expr / parser）

- **kernel**：自研词法 / 语法 / AST，运行时可动态加载 `.g4` 语法文件。
- **expr**：6 个引擎各管一种语言，**彼此不共用 SPI** —— `ElEvaluator`（自研 EL）、
  `ExpressionEngine`（自研 JS 引擎雏形）、`GroovyExecutor`、`LuaExecutor`、
  `VirtualTableEngine`（内存 SQL）、`ObjEngine`（对象整形）。
- **parser**：8 种格式各一个解析器（JSON / XML / YAML / CSV / TOML / INI / properties / proto），
  基于 ANTLR `.g4` + 自研 kernel。

### 3.3 数据与存储 `z-util-jdbc` / `z-util-pattern-cache` / `z-util-bc` / `z-util-serialize`

- `z-util-jdbc`：`context.DataSourceRegistry` / `dialect.Dialects`（MySql / Postgres / H2）/
  `query.Query` / `query.Criteria` / `query.QueryCompiler` / `memory.InMemoryTables` /
  `respository.CrudRepository` / `plugin.MyBatisPageInterceptor`。
- `z-util-pattern-cache`：**进程内**缓存（TTL + LRU / W-TinyLFU + builder + 装饰器），
  **无 Redis / 无远端后端**。
- `z-util-bc`：字节码模型 + ASM 编织 + 内存编译 + javaparser 源码面。
- `z-util-serialize`：Z-Serialize 线格式（header / varint / schemaId / 压缩加密），
  `ZSerializer` / `ZDeserializer` / `CodecRegistry` / `SchemaRegistry`，
  线格式规范见 `_doc/001_arch/06-wire-format.md`。

### 3.4 决策与风控两件 `z-util-strategy` / `z-util-sensitive`

- `z-util-strategy`：`DecisionEngine`（current / candidate 双槽 + `AtomicReference` 原子切换）、
  `DecisionPackage`（不可变快照 + 版本 + `RunMode` DRAFT/TRIAL/ACTIVE）、`TrafficPolicy`（稳定哈希分流）、
  `ControlPolicy`（对照组放行）、`DecisionMetrics.InMemory`、`rule.Rule` / `RuleEvaluator`、
  `loader.DecisionPackageLoader`（Java builder + JSON/YAML）。设计见
  `_doc/001_arch/04-strategy-engine.md`。
- `z-util-sensitive`：`SensitiveDetector`（词库热更新走 volatile 换引用）、`DfaMatcher`（构建后不可变）、
  `WordDictionary`、`SensitiveResult`（带区间，可 `mask`）、`pii.IdCardRule`（GB11643 校验位）/
  `BankCardRule`（Luhn）/ `MobileRule` / `EmailRule` / `IpRule` / `UrlRule`。选型与不做什么见
  `_doc/001_arch/03-sensitive-detect-engine.md`。

### 3.5 其它单件模块

| 模块 | 一句话 |
|---|---|
| `z-util-office` | POI / PDFBox：Excel / Word / PDF / PPT 读写 + 模板引擎 + 链式门面（`ExcelTemplate` / `PdfUtil` / `PptTemplate`） |
| `z-util-media` | 图像处理、验证码、GIF、二维码（`CaptchaUtil` / `GifEncoder` / `graph.QRCode.QRCodeUtil`，含 encoder 与 decoder 两侧） |
| `z-util-math` | NumPy/Pandas 风格结构（`NdArray` / `Linalg` / `DataFrame` / `Series` / `CSVReader`） |
| `z-util-ml` | 教学性质的 ML：`nn.Sequential` / `tree.DecisionTree` / `clustering.KMeans` / `decomposition.PCA` / `rl.QLearning` 等 |
| `z-util-monitor` | `JvmMonitor` / `OsMonitor` / `NetMonitor` / `MetricsRegistry` + `HtmlExporter` / `JsonExporter` + 告警策略 |
| `z-util-ioc` | `Injector` / `Module` / `Scopes` / `context.ClassPathApplicationContext` / `aop.AopProxyPostProcessor` |
| `z-util-aop` | `@Advise` / `@Intercept` / `ProxyFactory`（仅依赖 `z-util-core` 与 `z-util-bc`） |
| `z-util-distribute` | `SnowflakeIdWorker` / `SegmentIdGenerator` / `NanoId` / `UuidV7` |
| `z-util-cli` | `CLI` / `CommandLine` / `DefaultParser` / `OptionGroup` / `help.HelpFormatter` |
| `z-util-ch` | `PinyinGeneratorUtil` / `IdcardUtil` / `MoneyUtil` / `NumberChineseUtil` / `WeekUtil` |
| `z-util-visualization` | Swing 图表与算法可视化（`chart.*` / `swing.AlgoVisualizer`） |
| `z-util-wf-kernel` | 内存工作流：`bpmn.BpmnXmlParser` / `engine.runtime.WorkflowRuntimeEngine` / `engine.runtime.GatewayEvaluator` / `persistence.FileWorkflowPersistencePlugin` |
| `z-util-devops-git` | JGit / Shell 的 Git 能力 + GitHub API；GitHub REST 入口切 okhttp 的进度见 `_doc/001_arch/07-roadmap.md` |

## 4. 对外集成 / 上下游

**本仓无对外 HTTP 边界** —— 这是库，不监听端口。`z-util-http` 的 `AllPathHttpServer` 基于 JDK
`com.sun.net.httpserver.HttpServer`，**端口由调用方传入**，本仓不设默认端口。

**上游依赖（根 POM `<dependencyManagement>` 121 条自供）：** `log4j 2.26.1` / `slf4j 2.0.16`
（配 `log4j-slf4j2-impl`）/ `jackson 2.18.11`（作 databind / jsr310 辅助，JSON 主线是自研
`z-util-parser-json`）/ `okhttp 4.12.0` / `netty 4.1.138.Final` / `protobuf-java 3.25.5` /
`junit-jupiter 5.11.4` / `junit-vintage-engine`；另有 25 条传递依赖连贯性锚点（把上游 pom 自带的
传递件按坐标钉死）。

**下游消费方（本机实测 grep）：** 组织内各仓按需引 `io.github.yuku123:z-util-<module>`，例如
`z-trade-order-core` 引 `z-util-core`、`z-vector` 引 `z-util-{core,math,ml}`、
`z-wf` 引 `z-util-{core,parser-json,expr-el,expr-js,jdbc,wf-kernel}`。

**发布：** Maven Central 已发布（`io.github.yuku123:z-util` 及各 `z-util-<module>`）。
发布脚本在本仓 `_doc/003_script/deploy_maven_center.sh`（子命令 `gpg-init` / `publish` / `verify`），
`install-settings.sh` 负责把 `<server id="central">` 写进 `~/.m2/settings.xml`；
操作手册见 `_doc/006_release/01-release-to-maven-central.md` 与 `02-release-guide.md`。

## 5. 技术特点

1. **无父链的独立 POM 树**：根 POM 不写 `<parent>`、不 import 外部 BOM，121 条
   `dependencyManagement` 全部自供；发布出去的根 pom 在 repo1 可被任何消费者独立解析。
2. **聚合件 8 个、jar 件 62 个**：8 个 `packaging=pom` 纯聚合件（`z-util-dsl` / `z-util-expr` /
   `z-util-parser` / `z-util-pattern` / `z-util-serialize` / `z-util-wf` / `z-util-devops` /
   `z-util-all`）只做模块编排，不产 jar。
3. **expr 6 引擎各管一种语言、互不共用 SPI**：`z-util-expr-el` / `-js` / `-groovy` / `-lua` /
   `-sql` / `-obj` 各自独立，避免"万能表达式引擎"变成大杂烩。
4. **OBJ 不带语法解析器**：程序形状就是 JSON（保序 Map/List 树），任何给出这种树的解析器解析出来
   即可执行 —— 取数链「库里取数 → 内存 SQL 出二维表 → OBJ 抬成渲染结构」因此三段可独立替换。
5. **自研 JSON 优先**：`z-util-parser-json` 是仓内 JSON 主线（`.g4` + 自研 DSL 解析器），
   Jackson 只作 databind / jsr310 辅助，不与它抢主位。
6. **缓存只做进程内**：`z-util-pattern-cache` 明确不引 Redis / 远端后端，跨节点缓存不是本仓的活。
7. **内部依赖实测有向、无环**（逐模块 POM 的 `<dependency>` 里的 `z-util-*` 条目）：
   `z-util-aop → core, bc`；`z-util-bc`（零内部依赖，叶子）与 `z-util-dsl-kernel`（叶子）；
   `parser-{json,csv,ini,properties,proto,toml} → dsl-kernel`，`parser-xml / parser-yaml → core, dsl-kernel`；
   `core → parser-json, pattern-cache, pattern-composite, pattern-factory`；
   `pattern-cache → pattern-register`，`pattern-stream → pattern-chain`，其余 pattern 子件 → core；
   `ioc → core, aop`；`http-proxy → http-core, core, parser-json`，`http-server → http-core, http-proxy, aop`；
   `expr-obj → expr-el, parser-json`，`expr-sql → dsl-kernel, core`，`expr-js → dsl-kernel`；
   `jdbc → core, aop, expr-sql, expr-obj, distribute, parser-json`；
   `wf-kernel → core, pattern-register, expr-el, parser-json`；
   `strategy → expr-sql, parser-json`；`ml → math`，`visualization → ml`，`math → core`。
   跨层反向引用会成环，编译顺序由 Maven 保证。

## 6. 目录结构

本仓是纯 Java 库，源码按「模块目录 = Maven 模块」组织，每个模块内是
`src/main/java/com/zifang/util/…` 的标准包树（包根逐模块实测，见下）：

```
z-util/
├── pom.xml
├── z-util-core/src/main/java/com/zifang/util/core/
│   ├── bean/  encrypt/  io/  json/  jwt/  lang/  lock/  meta/  net/
│   ├── ratelimit/  resilience/  state/  time/  trace/  util/  extra/  feature/  sys/  system/
│   └── Const.java
├── z-util-dsl/
│   ├── z-util-dsl-kernel/src/main/java/com/zifang/util/dsl/          # g4/ + token/
│   ├── z-util-expr/
│   │   ├── z-util-expr-el/src/main/java/com/zifang/util/expr/el/
│   │   ├── z-util-expr-groovy/src/main/java/com/zifang/util/expr/groovy/
│   │   ├── z-util-expr-lua/src/main/java/com/zifang/util/expr/lua/
│   │   ├── z-util-expr-obj/src/main/java/com/zifang/util/expr/obj/
│   │   ├── z-util-expr-sql/src/main/java/com/zifang/util/expr/sql/
│   │   └── z-util-expr-js/src/main/java/com/zifang/util/expression/   # 包名是 expression 不是 expr
│   └── z-util-parser/
│       ├── z-util-parser-csv/src/main/java/com/zifang/util/parser/csv/
│       ├── z-util-parser-ini/src/main/java/com/zifang/util/parser/ini/
│       ├── z-util-parser-properties/src/main/java/com/zifang/util/parser/properties/
│       ├── z-util-parser-proto/src/main/java/com/zifang/util/parser/proto/
│       ├── z-util-parser-toml/src/main/java/com/zifang/util/parser/toml/
│       ├── z-util-parser-json/src/main/java/com/zifang/util/json/       # 包名是 json 不是 parser
│       ├── z-util-parser-xml/src/main/java/com/zifang/util/xml/
│       └── z-util-parser-yaml/src/main/java/com/zifang/util/yaml/
├── z-util-wf/z-util-wf-kernel/src/main/java/com/zifang/util/wf/kernel/
│   ├── components/  config/  bpmn/  engine/runtime/  engine/interfaces/  persistence/  json/
├── z-util-http/
│   ├── z-util-http-core/src/main/java/com/zifang/util/http/{base,client,parser,sse}/
│   ├── z-util-http-proxy/src/main/java/com/zifang/util/http/client/      # 与 core 同在 http 包下
│   └── z-util-http-server/src/main/java/com/zifang/util/http/server/
├── z-util-jdbc/src/main/java/com/zifang/util/db/                       # 包名是 db 不是 jdbc
│   └── context/  dialect/  query/  memory/  respository/  plugin/  generater/
├── z-util-pattern/z-util-pattern-*/src/main/java/com/zifang/util/core/pattern/<模式名>/
│   ├── cache/  chain/  command/  composite/  event/  factory/  ioc/  memento/  pool/
│   ├── register/  spi/  state/  strategy/  stream/  template/  validation/  visitor/  builder/
├── z-util-serialize/
│   ├── z-util-serialize-core/src/main/java/com/zifang/util/serialize/core/{compress,crypto}/
│   ├── z-util-serialize-codegen/src/main/java/com/zifang/util/serialize/codegen/
│   ├── z-util-serialize-formats/src/main/java/com/zifang/util/serialize/formats/json/
│   ├── z-util-serialize-it/src/test/java/com/zifang/util/serialize/bench/
│   └── z-util-serialize-schema/src/main/java/com/zifang/util/serialize/annotation/
├── z-util-bc/src/main/java/com/zifang/util/bc/{bytecode,compile,weave,source}/
├── z-util-devops/
│   ├── z-util-devops-git/src/main/java/com/zifang/util/devops/git/
│   ├── z-util-devops-docker/src/main/java/com/zifang/util/devops/docker/
│   └── z-util-devops-nexus/src/main/java/com/zifang/util/devops/nexus/
├── z-util-ml/src/main/java/com/zifang/util/ml/            # nn/ tree/ clustering/ decomposition/ rl/ …
├── z-util-monitor/src/main/java/com/zifang/util/monitor/
├── z-util-office/src/main/java/com/zifang/util/office/
├── z-util-math/src/main/java/com/zifang/util/numpy/       # 包名是 numpy
├── z-util-media/src/main/java/com/zifang/util/media/      # graph/QRCode/ … 
├── z-util-visualization/src/main/java/com/zifang/util/visuallization/   # 历史拼写：visuallization
├── z-util-ioc/src/main/java/com/zifang/util/ioc/
├── z-util-aop/src/main/java/com/zifang/util/aop/
├── z-util-distribute/src/main/java/com/zifang/util/distributes/sequence/
├── z-util-strategy/src/main/java/com/zifang/util/strategy/{rule,loader}/
├── z-util-sensitive/src/main/java/com/zifang/util/sensitive/pii/
├── z-util-cli/src/main/java/com/zifang/util/cli/
├── z-util-ch/src/main/java/com/zifang/util/ch/
├── z-util-all/                                          # 总装件，无源码
└── _doc/
    ├── 001_arch/     # 8 篇架构文档
    ├── 003_script/   # 15 个脚本（发布 / 校验 / Shell 学习笔记）
    └── 006_release/  # 2 篇发布文档
```
