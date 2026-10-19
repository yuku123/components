# z-ex 练习场的拆除记录与残留知识点

2026-10-16，`z-util-zex` 模块整体删除（`git rm -r`）。本文只保留两块东西：为什么要删，以及从里面真正读出来、值得留在文档里的知识点。

## 为什么删

删除前它是仓库里唯一一个「源码在 `src/main`、测试也在 `src/main`」的模块：162 个 main 文件（bust 92 / leetcode 28 / sort 13 / interview 8 / source 7 / disrupt 6 / bytecode 4 / guava 1），加 46 个 `@Test` 类混在同一棵树下。

几个把它拖到必须处置的点：

1. **它一直在默认 reactor 之外**，只在 `sandbox` profile 里挂载，Central 上从来没有 `z-util-zex` 这个坐标。所以它长期不受「12284 测试全绿」这条发布路径约束，绿了也说明不了什么。
2. **2026-10-16 迁 `src/main` → `src/test` 后首次编译成功，随即暴露 10 处红**，分布在 4 个类。这不是迁移引入的缺陷，是代码从来没在测试位上跑过：里面写着 `read()` 等 stdin、弹 `JFrame`、读本机绝对路径 `bpmn-engine-1.2.1-SNAPSHOT.jar`。在无头 CI 上，这些用例的正确行为就是红。
3. **里面几乎没有可复用的生产代码**。逐个读过之后确认：`disrupt/Disrupt.java` 是个只有 javadoc 的空壳类；`bytecode/asm/A.java` 把 `.class` 写到 `/Users/zifang/workplace/idea_workplace/...` 这条已不存在的绝对路径；`bytecode/ctclass/CtClassTest.java` 同样写死 `/Users/zifang/Downloads/trash/...`。**按目录名判断会误判**——`disrupt` 目录名看着有料，实际主类却是个空壳——所以是逐文件读过的。
4. **章节目录名本身就错**：`bust/charpter12`（`charpter` 拼错）、`bust/chapter1` 到 `chapter13` 缺 `chapter7`，说明它跟着某本书的目录在长，不是跟着自己的设计在长。
5. **它自己的依赖现在已经解析不出来**。`z-util-zex` 声明 `z-util-http:${revision}` 为 compile 依赖，而 `1.2.8` 只提交未安装，单独构建 zex 直接 `Cannot access central ... in offline mode and the artifact io.github.yuku123:z-util-http:jar:1.2.8 has not been downloaded from it before`。一个连自己的 pom 都跑不通的模块，不该继续占着仓库。
6. **依赖声明本身是漏的**。根 `pom.xml` 只在 `<dependencyManagement>` 里钉了 `guava` 的版本，全仓没有任何模块真正声明 guava；而 `src/test/.../guava/**` 十个 `_Study` 类 `import com.google.common.collect.*`。同理 `bytecode/asm/A.java` import 的 `org.objectweb.asm` 也没有任何声明——它能编译是**搭了 javassist 的便车**（javassist 自带 relocate 过的 ASM），不是显式依赖。

真正值得留的东西只有下面这几件，全部已经读进本文。

---

## 一、LMAX Disruptor：三件套与「发布才可见」

原文件 `disrupt/`：`LongEvent`、`LongEventFactory`、`LongEventHandler`、`LongEventProducer`、`LongEventMain`。

装配顺序是固定的五步，缺一步就跑不起来：

```java
Executor executor = Executors.newCachedThreadPool();
LongEventFactory factory = new LongEventFactory();
int bufferSize = 1024;                    // 必须是 2 的幂
Disruptor<LongEvent> disruptor = new Disruptor<>(
        factory, bufferSize, executor, ProducerType.MULTI, new BlockingWaitStrategy());
disruptor.handleEventsWith(new LongEventHandler());   // 消费者
disruptor.start();                                     // 启动线程
RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
```

真正值钱的是生产者这一侧：

```java
public void onData(ByteBuffer bb) {
    long sequence = ringBuffer.next();        // 申请下一个可用槽位
    try {
        LongEvent event = ringBuffer.get(sequence);
        event.set(bb.getLong(0));
    } finally {
        ringBuffer.publish(sequence);         // 只有 publish 之后消费者才看得见
    }
}
```

这里有本仓自己的 `z-util` 用得上的一般性结论：**`RingBuffer` 的槽位在 `publish` 之前是「借来的」，`publish` 必须在 `finally` 里**。漏了它一旦构造事件时抛异常，消费者会永久等在这个 sequence 上——不是丢一条消息，是整个 ring buffer 停摆。同理 `bufferSize` 必须是 2 的幂（内部用 `sequence & (size-1)` 定位，省掉取模），传 1000 会静默错位。

`ProducerType.MULTI` + `BlockingWaitStrategy` 是「多生产者 + 生产者允许阻塞」的组合；换成 `SINGLE` 时消费者要靠 sequence 严格递增来判重，`MULTI` 下则必须自己在事件里带业务 ID。

## 二、StampedLock 的三模式，以及乐观读必须 validate

原文件 `source/StampedLockTest.java`。三种模式的差别在**读操作要不要写内存屏障**：

| 模式 | 写法 | 读期间可否写 | 适用 |
|---|---|---|---|
| 写锁 | `lock.writeLock()` / `unlock(stamp)` | — | 互斥 |
| 悲观读 | `lock.readLock()` / `unlockRead(stamp)` | 否（阻塞写） | 读多写少、但要求读一致 |
| 乐观读 | `lock.tryOptimisticRead()` | 是 | 读极多、冲突极少 |

乐观读的完整形态不是「tryOptimisticRead 拿到就用」，而是三段兜底：

```java
long stamp = lock.tryOptimisticRead();
if (stamp != 0) {
    value = data.get(key);              // 读数据，不加锁
} else {
    return pessimisticRead(key);        // 印戳拿不到 → 降级
}
if (!lock.validate(stamp)) {           // 读的过程中有写发生 → 印戳失效
    return pessimisticRead(key);        // 必须重读
}
return value;
```

漏掉 `validate(stamp)` 会读到「撕裂值」：`HashMap.get` 返回对象引用本身没问题，但如果是 `data.get(k).length` 这种「读两次」，中间被写入就会得到一个不存在的组合。**乐观读只保证单次读取的原子性，不保证读取序列的一致性**，这个区分是它和 `ReadWriteLock` 的唯一实质差别。

## 三、线程池扩展的坑：beforeExecute / afterExecute 里 ThreadLocal 会泄漏

原文件 `bust/charpter12/ThreadPoolExtend.java`，通过继承 `ThreadPoolExecutor` 覆写两个钩子打点计时：

```java
protected void beforeExecute(Thread t, Runnable r) {
    localTime.set(System.nanoTime());
}
protected void afterExecute(Runnable r, Throwable t) {
    System.out.println(System.currentTimeMillis() - localTime.get());
}
```

原代码能跑，但有三个真问题，是「自己写线程池扩展」的通用教训：

1. **`localTime` 是 `ThreadLocal` 却没有 `remove()`**。线程池的线程复用不销毁，值会跟着线程一直活着。如果这个池子以后接别的埋点、或者任务被复用，`localTime.get()` 会拿到上一次某个毫不相关的任务的时间戳，埋点数据静默串味。钩子里 set 就要配对 remove。
2. **`afterExecute` 里取线程名用的是 `Thread.currentThread().getName()`，这是错的**。`afterExecute` 由**执行任务的 worker 线程**调用（此时该线程还没回到取任务的循环），但 `beforeExecute` 里用的是参数 `t.getName()`——同一个钩子对，两个来源。演示里看不出差别，是因为 demo 恰好由同一个线程提交。真池子里提交方和执行方不同线程，会打出两个不同线程名的同一条耗时记录。
3. **`afterExecute` 拿不到任务抛出的异常**。`submit()` 的任务异常被包进 `FutureTask`，只出现在 `Future.get()` 里；`execute()` 的异常则由 worker 线程的 `UncaughtExceptionHandler` 接。要在钩子里统一采集耗时和异常，`execute()` 提交的任务需要在包装层自己捕获。

## 四、FileChannel 的位置写与 gather/scatter

原文件 `bust/chapter11/FileChannelTest.java` 与 `case2/TransformInputStreamOutputStream.java`。三个值得记住的形状：

```java
FileChannel fileChannel = fosRef.getChannel();
fileChannel.write(buffer1);                    // 追加写，channel position 自动前进

buffer2.position(1); buffer2.limit(3);        // 只取 "23"
fileChannel.position(2);                      // 从文件第 2 字节写 → abcde 变成 ab23e
fileChannel.write(buffer2);

fileChannel.write(buffer3, 1);                // 写 absolutePosition，不动 channel position
```

即：`write(buffer)` 走 channel 的当前位置并推进它；`write(buffer, position)` 从文件绝对位置写且不动 channel 位置。同一套还有 `ByteBuffer.flip()` 之前是写模式（position=0, limit=capacity），之后是读模式（position=limit 旧值, limit=position 旧值）——`Compact` 类题目基本都在这两者之间来回。

Stream 侧的经典形状是**定长 buffer + 循环读**，注意 `read` 不保证读满：

```java
byte[] buffer = new byte[1024];
int len;
while ((len = in.read(buffer)) > 0) {
    out.write(buffer, 0, len);   // 必须用实际读到的 len，不能用 buffer.length
}
```

`in.read(buffer)` 只保证「至少 1 字节」，EOF 才返回 -1。把 `out.write(buffer)` 写成无条件写满 1024，最后一段就会补零，是 IO 方向最常见的 bug。

## 五、AQS 的方法面可以靠反射 dump 出来

原文件 `source/ClassPrinter.java`，10 行：

```java
for (Method method : AbstractQueuedSynchronizer.class.getDeclaredMethods()) {
    System.out.println(method.getReturnType().getName() + " " + method.getName()
            + "(" + String.join(", ", Arrays.stream(method.getParameterTypes())
            .map(Class::getName).collect(toList())) + ")");
}
```

读 JDK 并发源码时的通用手法：**先 dump 方法签名面，再读实现**。`AbstractQueuedSynchronizer` 的方法面一眼能看出结构——`acquire(int)` / `release(int)` / `tryAcquire` / `tryRelease` / `addWaiter` / `setHeadAndPropagate` / `doAcquireShared` / `doReleaseShared`——arg 形式是独占还是共享一目了然，然后才去啃那个 while 循环。同一手法适用于 `java.util.concurrent` 下任何锁类。

## 六、并发知识清单（`bust/charpter12` + `bust/chapter13`）

这批文件本身是 demo，价值在于清单本身，按「什么时候用得着」重新归类后：

- **内存可见性**：`NoVisibility`（普通字段不保证跨线程可见）、`FieldVisibility`（加 volatile 后可见）、`VisibilityProblem` / `VolatileExample`。结论：对一个 flag 用 `volatile` 或 `AtomicBoolean`，不要用 `synchronized` 去包一次 boolean 的读写。
- **原子性**：`AtomicTest`、`TestSolveAtomic`、`TestSolveView`（`AtomicInteger` 自旋 CAS）、`Disorder`（`incrementAndGet` vs `getAndIncrement`）。结论：多线程计数不用锁，用 `LongAdder`（`Disorder` 里对比过）。
- **伪共享**：`FalseSharingExample`——两个线程各写一个 `volatile long`，因为落在同一条 cache line 而互相失效。注意**原代码里定义了 `ValuePadding`（前后各 7 个 long 填充）却只实例化了 `ValueNoPadding`**，等于只测了「有没有填充」的两端，没跑填充版的对照。真正要下结论得两个都跑。
- **阶段同步**：`PhaserTest`——覆盖 `CountDownLatch` / `CyclicBarrier` 的短板：前两者只能「一次性」或「定数」，`Phaser` 支持 parties 动态注册注销，`onAdvance` 钩子可以观察每阶段完成。适合「N 个 worker 分阶段跑、阶段间栅栏、人数还会变」。
- **线程池扩展点**：`ThreadPoolExtend`（见上面第三节）。
- **阻塞队列**：`BlokingQueueTest`。
- **socket/NIO**：`bust/chapter13` 整批（`NioChannelTest000~005`、`SocketTest001/002`、`URLTest`、`InetAddressTest`）。
- **线程基础**：`test001/` 下的 `CreateThread`、`JoinTest`、`InterruptTest`、`NotifyTest`、`ReentratTest`、`TestUncatchException`（注意 `ReentratTest` 拼错，原样保留）。
- **JVM**：`JVMTest`、`ShutdownHook`。

## 七、ASM 的 ClassVisitor / MethodVisitor 访问者链

原文件 `bytecode/asm/A.java` + `Base.java`。ASM 的形状是**双层访问者**：`ClassVisitor.visitMethod` 返回一个 `MethodVisitor`，后续该方法的所有指令回调都发给这个返回的 visitor。给方法插桩的最小形状：

```java
classReader.accept(new MyClassVisitor(classWriter), ClassReader.SKIP_DEBUG);

@Override
public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
    if (!name.equals("<init>") && mv != null) {   // 构造方法不能插桩
        mv = new MyMethodVisitor(mv);
    }
    return mv;
}

@Override
public void visitCode() {                          // 方法入口
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitLdcInsn("start");
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
}

@Override
public void visitInsn(int opcode) {
    if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {  // 所有出口
        // 同样插一句 println("end")
    }
    mv.visitInsn(opcode);                         // 转发，不能吞
}
```

两个坑：`visitCode()` 里插的指令栈深必须为 0（所以只调 `System.out`，不要调有返回值的方法）；**所有 visitor 回调都必须把 `mv` 转发下去**，漏一个 `mv.visitInsn(opcode)` 那条指令就凭空消失。

ASM 在本仓里已经被自己的 `z-util-bc` 全面替代（自研 CFG 反编译器，见 `z-util 字节码桥接` 那条记录）。**要动字节码一律走 `z-util-bc`，不要引 ASM。**顺带记一笔：这个示例当年能编译，靠的是 javassist 内置的 relocate 版 ASM，pom 里从没声明过 ASM 依赖——**依赖不声明时，「能编译」是借来的，不是保证的**。

---

## 删完之后

- 根 `pom.xml` 的 `sandbox` profile **整个移除**：实测它只有 `<module>z-util-zex</module>` 一个成员，删模块后这个 profile 就是空的。
- 连带删掉根 `pom.xml` 里两处解释「为什么 z-ex 不列进发布清单」的注记（第二条注记本身只是在解释第一条，一并删）。
- `README.md` 四处提及移除：发布清单注记（中央 404 那条）、模块表行、目录树行、末尾「不发布」说明段。
- 默认 reactor 与发布清单**不变**——`z-ex` 从未在默认 reactor 里。
- 仓库净减 208 个 Java 文件（162 main + 46 test），少掉 `disruptor` / `javassist` / `jol-core` 三个只被它引用的第三方坐标，以及 `z-util-http` 在仓库内的一处消费者。
