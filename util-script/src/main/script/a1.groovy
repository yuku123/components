package groovy

/*
 * Print方法 print println 后面必须跟参数 否则会抛groovy.lang.MissingPropertyException
 */

//print
//println
println "helloworld"
/*
 * 单引号中的内容严格对应java中的String 不对$符号进行转义
 */
def s1 = 'i am 100 $ dollar'
println s1

class Person {
    String name
    Integer age
}
def person = new Person(name:"john",age:18)
/*
 * 双引号“”的内容和脚本语言处理有点像，如果字符中有$符号的话，则会表达式先求值
 */
def s2 = "i am 100 ${person.name} dollar"
println s2

/*
 * 三个引号 三个单引号或三个双引号 中的字符串支持随意换行 不同处""" """支持$等特殊符号转义 ''' '''不支持，强String类型
 */
def s3 = """
tom
    is
        a
             good
                  boy
$s1

"""
println s3

def s4 = '''
tom
    is
        a
             good
                  boy
$s1

'''
println s4

/**
 * 定义函数
 */
//无参函数
def fun1() {

}

//有参函数，无需指定参数类型
def fun2() {

}

//如果制定了函数返回类型，则可不必加关键字def 来定义函数
String fun3() {
    return "welcome"
}

//如果不使用return 来设置返回值 ，则函数里最后一句代码的执行结果被设置成返回值  类型不匹配则会报错
Integer fun4() {
    "a story"
    "about"
    "prince"  //if this is end类型不匹配 GroovyCastException
    4.0    //if this is a number will be return a Integer number 4
}

println fun4()


/*
 * assert 断言 可以简单理解为 比较 进行简单的判断
 */
def str1 = "abc"

assert str1.size() == 3
//assert str1.size() != 3


/*
 * 循环
 */

for (int i =0;i<5;i++ ) {
    println i + "for 1"
}

for (i =0;i<5;i++ ) {
    println i + "for 2"
}

for (i in 4..8 ) {
    println i-4 + "for 3"
}

5.times {
    println "circle"
}

/*
 *三目运算符
 *
 */
def name
def result =name != null?name :"name is error"
println result

/*
 * 异常
 */
try {
    //      5/0
}catch(Exception e) {
    print e
}

try {
//     5/0
}catch(anything) {
    print anything
}

/*
 * Switch
 */
def age = 36
def rate

switch (age) {
    case 10..26:
        rate = 0.05
        break
    case 27..36:
        rate = 0.06
        break
    default:
        throw new Exception()
}

println rate

/*
 * asType 数据类型转换
 */
def type1 = 100
def type2 = 200.00
def type3 = type2.asType(Integer)
def type4 = type1.asType(String)

println("groovy 语言学习--语法基础（1） ---end ")