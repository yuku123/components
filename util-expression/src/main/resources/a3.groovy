/*class Test3 {

}*/


def map = [key1: "john", key2: "tom", key3: "rose"]
map.each { println it }

map.each({ key, value -> println "$key : $value" })

/*
 * 使用默认构造方法实例化Person类
 */

def person1 = new Person()

person1.setName("lucky")
person1.setAge(20)
person1.name = "jack"
println person1
/*
 * groovy 风格
 */
def person2 = new Person(['name': 'gaga', 'age': 22])
println person2

/*
 * java代码 防止空指针
 */
if (person2 != null) {

    println "check right"
}

/*
 * groovy 代码防止空指针
 */

person2?.println "welcome no null point"

/*
 * 可变长参数 在调用的的时候可以使用任意个数的参数
 */

int sum(int ... var) {
    def total = 0
    for (i in var) {
        total += i
    }
    return total
}

println sum(1, 2, 2)

/*
 *枚举
 */

enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,

    THURSDAY, FRIDAY, SATURDAY
}

println Day.FRIDAY
/*
 * 同java5一样，groovy支持带构造器、属性和方法的enum
 */

enum Planet {
    MERCURY(3.303e+23, 2.49376e6),
    MARS(3.303e+23, 2.49376e6),
    EARTH(5.976e+24, 6.37814e6)

    double mass
    double radius

    Planet(double mass, double radius) {
        this.mass = mass
        this.radius = radius
    }

    void printMe() {
        println "${name()} has a mass of ${mass} and a radius of ${radius}"
    }
}

Planet.EARTH.printMe()
/*
 * 可以.方法查看enum相关的api 有很多
 */
println Planet.EARTH.name()

/*
 * metaClass 元类 通过元类，可以为对象增加方法 体现为groovy的动态性
 */
def message = "hello world"
print message.metaClass

//message.metaClass.methods.each { println it.name }
//message.metaClass.properties.each {println it.getKey()}