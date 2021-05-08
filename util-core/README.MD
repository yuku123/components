从guava，common-lang3 获得思路

1. 生成list,map,set的简单方法
2. 将其他形式的数据结构很方便地转换过去
3. 操作collection要变得很舒服
4. 集合提供全量的韦恩图操作

5. 集合的包装类，可以对集合操作做统计

1.只读设置:immutableList

2.函数式编程:解耦

     1) predicate

2)Function

工具:

Collections2.filter()过滤器

Collections2.transfer()转换

Functions.compose()组合式函数编程

3.加入约束条件:非空 长度验证

Constraint

Preconditions

4.集合的操作:交集差集并集

5.Multiset Multimap BiMap

6.Table