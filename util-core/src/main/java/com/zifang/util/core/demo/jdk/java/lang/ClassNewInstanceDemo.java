package com.zifang.util.core.demo.jdk.java.lang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * Java中五种不同方法的创建对象
 *
 *
 */
public class ClassNewInstanceDemo {

	static class Employee implements Cloneable, Serializable {
		private static final long serialVersionUID = 1L;
		private String name;

		public Employee() {
			System.out.println("Employee Constructor Called...");
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Employee other = (Employee) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Employee [name=" + name + "]";
		}

		@Override
		public Object clone() {
			Object obj = null;
			try {
				obj = super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return obj;
		}
	}

	public static void main(String... args) throws Exception {
		/**
		 * 1.使用new关键字
		 *  这是最常见的创建对象的方法，并且也非常简单。通过使用这种方法我们可以调用任何我们需要调用的构造函数。
		 */
		Employee emp1 = new Employee();
		emp1.setName("Naresh");
		System.out.println(emp1 + ", hashcode : " + emp1.hashCode());

		/**
		 * 2.使用class类的newInstance方法
		 *  我们也可以使用class类的newInstance方法来创建对象。此newInstance方法调用无参构造函数以创建对象。
		 *  我们可以通过newInstance() 用以下方式创建对象：
		 */
		// By using Class class's newInstance() method
		Employee emp2 = (Employee) Class.forName("com.zifang.util.core.demo.jdk.java.lang.Employee").newInstance();
		// 或者
		emp2 = Employee.class.newInstance();

		emp2.setName("Rishi");
		System.out.println(emp2 + ", hashcode : " + emp2.hashCode());

		/**
		 * 3.使用构造函数类的 newInstance方法 
		 * 	与使用class类的newInstance方法相似，java.lang.reflect.
		 *  Constructor类中有一个可以用来创建对象的newInstance()
		 *  函数方法。通过使用这个newInstance方法我们也可以调用参数化构造函数和私有构造函数
		 * 
		 *  这些 newInstance() 方法被认为是创建对象的反射手段。实际上，内部类的newInstance()方法使用构造函数类的
		 *  newInstance() 方法。这就是为什么后者是首选并且使用不同的框架如Spring, Hibernate, Struts等。
		 */
		// By using Constructor class's newInstance() method
		Constructor<Employee> constructor = Employee.class.getConstructor();
		Employee emp3 = constructor.newInstance();
		emp3.setName("Yogesh");
		System.out.println(emp3 + ", hashcode : " + emp3.hashCode());

		/**
		 * 4.使用clone方法
		 *  实际上无论何时我们调用clone方法，JAVA虚拟机都为我们创建了一个新的对象并且复制了之前对象的内容到这个新的对象中。使用
		 *  clone方法创建对象不会调用任何构造函数。 为了在对象中使用clone()方法，我们需要在其中实现可克隆类型并定义clone方法。
		 */
		// By using clone() method
		Employee emp4 = (Employee) emp3.clone();
		emp4.setName("Atul");
		System.out.println(emp4 + ", hashcode : " + emp4.hashCode());

		/**
		 * 5.使用反序列化 
		 *  无论何时我们对一个对象进行序列化和反序列化，JAVA虚拟机都会为我们创建一个单独的对象。在反序列化中，
		 *  JAVA虚拟机不会使用任何构造函数来创建对象。 对一个对象进行序列化需要我们在类中实现可序列化的接口。
		 */
		// By using Deserialization
		// Serialization
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.obj"));
		out.writeObject(emp4);
		out.close();
		// Deserialization
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.obj"));
		Employee emp5 = (Employee) in.readObject();
		in.close();
		emp5.setName("Akash");
		System.out.println(emp5 + ", hashcode : " + emp5.hashCode());
	}
}
