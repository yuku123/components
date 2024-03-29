#!/bin/bash

## 使用变量

### 环境变量

### set 命令打印当前的环境列表

echo "使用set 命令打印出 当前的环境列表 ------------------------------"
set
echo "使用set 命令打印完毕--------------------------------------------"

### 取得环境变量的值，如果希望打印出$字符串那么加上\
echo "取得环境变量的值 \$HOME = $HOME"

### 生成用户变量值 a=3 中间不能有空格
echo "对a进行赋值为3-----"
a=3
echo "命令\$a所取到的值为 $a"

### 引用变量的区别
echo "\n"
b=a
echo "b=a 之后的\$b为 $b"
b=$a
echo "b=\$a 之后的\$b 为 $b"

### 命令替换
echo "\n"
a=$(date)
echo "a=$(date)赋值之后，执行date命令，然后给a，那么a的值为 $a"
echo "使用\$()命令同样能得到以上的结果：b=\$(date)"
b=$(date)
echo "b的值为 $b"
