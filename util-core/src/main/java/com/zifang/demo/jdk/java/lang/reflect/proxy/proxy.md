# 动态代理类的属性
  - 如果所有的代理接口都是public的，那么代理类就是public、final的，切不是abstract的
  - 动态代理类的名称以”$ProxyN”开头,N是代理类的唯一编号.
  - 动态代理类都继承于java.lang.reflect.Proxy
  - 动态代理类实现了其创建时指定的接口，且保持接口指定的顺序
  - 如果动态代理类实现了一个非public接口，那么它将定义和接口相同的包名；否则代理类的包是不确定的，默认是com.sun.proxy,运行时，包密封性不防止特定包成功定义代理类；如果都不是，动态代理类将由同一个类加载器和相同的包与特定签名定义.
  - 动态代理类实现了其创建时指定的所有接口，调用代理类Class对象的getInterfaces将返回和创建时指定接口顺序相同的列表，调用 getMethods方法返回所有接口方法的数组对象，调用getMethod会返回代理类接口中期望的method.
  - 调用Proxy.isProxyClass方法时,传入Proxy.getProxyClass返回的Class或者Proxy.newProxyInstance返回对象的Class，都会返回true，否则返回false.
  - 代理类的java.security.ProtectionDomain是由系统根类加载器加载的，代理类的代码也是系统信任的代码生成的，此保护域通常被授予java.security.AllPermission
  - 每一个代理类都有一个public的，含有一个InvocationHandler实现为参数的构造方法，设置了调用处理器接口，就不必使用反射api访问构造方法，通过Proxy.newProxyInstance可以产生和Proxy.getProxyClass和调用句柄相同的调用构造函数行为.
# 动态代理实例的属性
  - 给定一个代理实例proxy，Foo实现的接口之一，表达式 proxy instanceof Foo 返回true,(Foo) proxy能成功转换.
  - 每个代理实例都关联一个InvocationHandler， 通过Proxy.getInvocationHandler方法，将返回代理类关联的InvocationHandler.
  - 代理类实例调用其代理接口中所声明的方法时，这些方法将被编码，并最终由调用处理器(InvocationHandler)的invoke方法执行.
  - 代理类根类java.lang.Object中的hashCode,equals和toString方法，也会被分派到调用处理其的invoke方法执行；可能的原因有：一是因为这些方法为 public 且非 final 类型，能够被代理类覆盖；二是因为这些方法往往呈现出一个类的某种特征属性，具有一定的区分度，所以为了保证代理类与委托类对外的一致性，这三个方法也应该被分派到委托类执行。
  - 当代理的一组接口有重复声明的方法且该方法被调用时，代理类总是从排在最前面的接口中获取方法对象并分派给调用处理器，而无论代理类实例是否正在以该接口（或继承于该接口的某子接口）的形式被外部引用，因为在代理类内部无法区分其当前的被引用类型。