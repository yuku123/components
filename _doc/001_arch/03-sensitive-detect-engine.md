# z-util-sensitive — 敏感识别引擎

> 落库日期：2026-10-13
> 定位：文本中的**敏感词**与**敏感信息（PII）**识别、判定与脱敏。
> 与 `z-util-strategy`（`_doc/001_arch/04-strategy-engine.md`）的关系：它是决策引擎的一个**事实来源**，不是决策者。

## 1. 选型结论：为什么全自研

2026-10-13 联网核实 GitHub 现状后的取舍：

| 候选 | 结论 |
|---|---|
| `com.github.houbb:sensitive-word`（6.1k★ / 2026-03 活跃 / Apache-2.0 / JDK8 / 核心零传递依赖） | 质量确实好，但 **不引**。理由见下 |
| `com.huaban:jieba-analysis`（Apache-2.0 / JDK8 / 词典 20MB） | **不引** |
| `data-privacy-stack/presidio`（MIT，前身 microsoft/presidio） | 纯 Python + spaCy，Java 只能走 REST sidecar，违背轻依赖定位 |
| `LeapBeyond/scrubadub` | 2023-09 停更，正则模式少、无中文实体 |
| `NLPchina/ansj_seg` | 约 3 年未更新 |
| `hankcs/HanLP` | **排除**：中文预训练模型仅限研究教学，不可商用 |

不引 `sensitive-word` 的三条理由：

1. **词库是资产也是负债**。风控词库必须能按业务词表增量更新、按监管口径下线词条，
   跟在上游 6W+ 通用词库后面，反倒要写适配层去剔除它不该拦的词。
2. **`z-util-ch` 已有身份证工具**（`IdcardUtil`，GB11643 校验位 + 省份码 + 出生日期），
   PII 这块不需要外部库兜底，自研反而能对齐它的口径。
3. 引入 20MB 词典 + 一个传递依赖，与本仓"库件不替应用背依赖"的既有铁律冲突。

代价是自己要养 DFA 与规则表。换来的是零新依赖、可控词库、口径统一。

## 2. 能力边界

**做**：敏感词/黑名单命中、违禁词分级、PII 实体识别与校验、命中定位、脱敏掩码。
**不做**：中文分词、语义模型分类、涉政涉黄内容审核模型。
分词不做是因为它只服务于"关键词提取"这一个衍生需求，而真正要分词的场景
（内容审核）本身需要模型，规则层引入 20MB 词典换不来可用的分类能力。
将来若确有分词需求，走 SPI 接入，不污染内核。

## 3. 结构

```
com.zifang.util.sensitive
├── SensitiveType          枚举：WORD / ID_CARD / BANK_CARD / MOBILE / EMAIL / IP / URL / CUSTOM
├── WordEntry              词条：文本 / 类型 / 标签 / 分值权重
├── WordDictionary         词库：条目集合 + 标签
├── DfaMatcher             DFA 树匹配，构建后不可变、多线程共享
├── WordMatch              命中：起止下标 / 词 / 类型 / 标签
├── SensitiveResult        汇总命中，可 mask()
├── SensitiveDetector      门面：detect / mask
├── MaskStyle              掩码策略：全遮 / 保头保尾 / 哈希
├── MaskUtil               掩码实现
└── pii/                   PII 规则：每类一个 (regex, validator) 对
    ├── PiiRule            抽象：匹配 + 校验
    ├── IdCardRule         身份证（GB11643 校验位 + 省份码 + 真实日期）
    ├── BankCardRule       银行卡（Luhn）
    ├── MobileRule         手机号（号段前缀 + 长度）
    ├── EmailRule          邮箱
    ├── IpRule             IPv4（不含 IPv6，见 §4.2）
    └── UrlRule            URL
```

## 4. 两个关键设计

### 4.1 DFA：构建期冻结，匹配期零分配

敏感词匹配走 DFA（确定性有限自动机）而非逐词 `contains`：
N 个词条建一次树，之后扫全文是 O(len)，与词库规模无关。
节点用 `Map<Character, DfaNode>` 还是数组取决于字符集大小——本模块同时匹配中文与 ASCII，
用 `Map` 避免为 Unicode 全空间开数组。

**词库可热更新**：`DfaMatcher` 不可变，更新 = 构建新实例 + 原子替换引用。
`SensitiveDetector` 持有 `volatile DfaMatcher`，读无锁。

### 4.2 正则 ≠ 命中：PII 必须过校验位

这是本模块与"正则一把梭"的关键差别，也是最容易被省掉的一步。
`6222021234567890` 匹配银行卡正则，但它不是卡号 —— Luhn 校验会把它挡掉。
身份证同理：格式对但校验位错的号码必须拒掉，否则会误伤大量正常文本里的长数字串。

规则接口因此是 `(Pattern, validator)` 而不是裸 `Pattern`：
正则负责**召回**，validator 负责**精确率**。

### 4.3 命中必须带位置

只返回"是否命中"无法做脱敏与高亮定位。所有命中一律携带 `[start, end)` 区间，
`SensitiveResult.mask()` 据此替换，避免破坏原文其它部分。

## 5. 本轮交付范围

**已实现**
- `SensitiveType` / `WordEntry` / `WordDictionary` / `DfaMatcher` / `WordMatch`
- PII 六类规则 + `PiiRule` 抽象
- `SensitiveResult`（汇总 + 命中分类查询）、`SensitiveDetector` 门面
- `MaskStyle` + `MaskUtil`（全遮 / 保头保尾 / 哈希）
- 单测：DFA 命中与不误伤、PII 校验位正反例、掩码、并发读

**未实现（有意留白）**
- 中文分词与关键词提取（见 §2）
- 拼音/形近字变体归一（如 `微信` → `ㄨㄧㄒㄧㄣ`）——需要一张变体映射表，
  这张表属于业务资产不是代码，应由调用方通过 `WordDictionary` 自带词条喂进来
- 规则持久化与热更新源：应用层职责
