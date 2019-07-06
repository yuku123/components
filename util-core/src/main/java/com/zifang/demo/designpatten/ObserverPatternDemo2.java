package com.zifang.demo.designpatten;

import java.util.Observable;
import java.util.Observer;

/**
 * Java的设计模式很多，观察者模式被称为是模式中的皇后，而且Java
 * jdk也对它做了实现，可见该设计模式的重要位置。在图形化设计的软件中，为了实现视图和事件处理的分离，大多都采用了Observer模式，
 * 比如Java的Swing，Flex的ActionScript等。在现实的应用系统中也有好多应用，比如像当当网、京东商城一类的电子商务网站，
 * 如果你对某件商品比较关注，可以放到收藏架，那么当该商品降价时，系统给您发送手机短信或邮件。这就是观察者模式的一个典型应用，商品是被观察者，有的叫主体；
 * 关注该商品的客户就是观察者。下面的一个事例将模拟这个应用。
 *
 */
public class ObserverPatternDemo2 {

	static class House extends Observable { // 表示房子可以被观察
		private float price;// 价钱

		public House(float price) {
			this.price = price;
		}

		public float getPrice() {
			return this.price;
		}

		public void setPrice(float price) {
			// 每一次修改的时候都应该引起观察者的注意
			super.setChanged(); // 设置变化点
			super.notifyObservers(price);// 价格被改变
			this.price = price;
		}

		public String toString() {
			return "房子价格为：" + this.price;
		}
	};

	static class HousePriceObserver implements Observer {
		private String name;

		public HousePriceObserver(String name) { // 设置每一个购房者的名字
			this.name = name;
		}

		public void update(Observable o, Object arg) {
			if (arg instanceof Float) {
				System.out.print(this.name + "观察到价格更改为：");
				System.out.println(((Float) arg).floatValue());
			}
		}
	};

	public static void main(String args[]) {
		House h = new House(1000000);
		HousePriceObserver hpo1 = new HousePriceObserver("购房者A");
		HousePriceObserver hpo2 = new HousePriceObserver("购房者B");
		HousePriceObserver hpo3 = new HousePriceObserver("购房者C");
		h.addObserver(hpo1);
		h.addObserver(hpo2);
		h.addObserver(hpo3);
		System.out.println(h); // 输出房子价格
		h.setPrice(666666); // 修改房子价格
		System.out.println(h); // 输出房子价格
	}
}
