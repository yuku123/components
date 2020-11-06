##monitorableexecutor是什么?

monitorableexecutor是一个基于JDK的concurrent包中ThreadPoolExecutor以及ScheduledExecutorService实现的可监控的线程池管理器。

monitorableexecutor里默认的监控线程池的实现FixedMonitorableExecutor，通过继承ThreadPoolExecutor，实现其execute、beforeExecute、afterExecute等方法实现下述线程池信息的统计。

通过ExecutorManager对象的getExecutor方法获取线程池，则可享受monitorableexecutor监控服务。

##monitorableexecutor有哪些功能？

monitorableexecutor旨在解决JVM内线程池健康状况难以监控的问题。通过内部维护的ScheduledExecutorService，以可配置的时间间隔的频率打印线程池状态，如果需要，则进行告警。

目前监控的线程池信息包括：

* 已提交任务数
* 已启动任务数
* 执行成功任务数
* 执行失败任务数
* 上次任务启动时间
* 上次任务结束时间
* 所有任务总耗时
* 告警次数
* 任务状态（主要用于超时任务的取消）

目前已实现的告警服务：LogAlarmService（日志打印告警），告警策略：ThreadPoolOvertimeAlarmPolicy（判断线程池最后一个启动的任务是否在超时时间内结束）

##monitorableexecutor有哪些缺点？

首先，考虑到大量线程竞争情况下会影响monitorableexecutor吞吐，所以对线程池状态对象ThreadPoolStatus的内部信息（即上述统计信息）进行修改的时候，没有对ThreadPoolStatus进行加锁，这意味着线程池的状态并不是线程安全的。

其次，目前没有支持丰富的告警策略以及告警服务，需要使用monitorableexecutor的开发者自行去完善这部分功能。

最后，monitorableexecutor目前仅能用于监控单JVM内的线程池，不支持多JVM环境下的监控。

##有问题反馈
如果有任何意见和建议，请联系我：

* 邮件(jlee381344197#gmail.com, 把#换成@)
* QQ: 381344197
* github博客：http://leesir.github.io/

***

##License

See the LICENSE.md file for license rights and limitations (MIT).
