//原文网址
http://tutorials.jenkov.com/java-reflection/dynamic-proxies.html

````
//听过反射可以得到泛型方法的T值
Method method = MyClass.class.getMethod("getStringList", null);
Type returnType = method.getGenericReturnType();
if(returnType instanceof ParameterizedType){
    ParameterizedType type = (ParameterizedType) returnType;
    Type[] typeArguments = type.getActualTypeArguments();
    for(Type typeArgument : typeArguments){
        Class typeArgClass = (Class) typeArgument;
        System.out.println("typeArgClass = " + typeArgClass);
    }
}

//访问参数化类型

method = Myclass.class.getMethod("setStringList", List.class);
Type[] genericParameterTypes = method.getGenericParameterTypes();
for(Type genericParameterType : genericParameterTypes){
    if(genericParameterType instanceof ParameterizedType){
        ParameterizedType aType = (ParameterizedType) genericParameterType;
        Type[] parameterArgTypes = aType.getActualTypeArguments();
        for(Type parameterArgType : parameterArgTypes){
            Class parameterArgClass = (Class) parameterArgType;
            System.out.println("parameterArgClass = " + parameterArgClass);
        }
    }
}


//访问公有字段的泛型类型信息

public class MyClass {
  public List<String> stringList = ...;
}

Field field = MyClass.class.getField("stringList");
Type genericFieldType = field.getGenericType();
if(genericFieldType instanceof ParameterizedType){
    ParameterizedType aType = (ParameterizedType) genericFieldType;
    Type[] fieldArgTypes = aType.getActualTypeArguments();
    for(Type fieldArgType : fieldArgTypes){
        Class fieldArgClass = (Class) fieldArgType;
        System.out.println("fieldArgClass = " + fieldArgClass);
    }
}
````

