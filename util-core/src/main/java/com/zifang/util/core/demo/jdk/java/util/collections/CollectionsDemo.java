package com.zifang.util.core.demo.jdk.java.util.collections;

import org.junit.Test;

import java.util.*;

public class CollectionsDemo {

	/**
	 * 方法用于获取一个Deque 视图，作为一个后进先出(LIFO)队列。
	 */
	@Test
	public void asLifoQueue() {
		// create Deque object
		Deque<Integer> deque = new ArrayDeque<Integer>(7);

		// populate the object
		deque.add(1);
		deque.add(2);
		deque.add(3);
		deque.add(4);
		deque.add(5);
		deque.add(6);
		deque.add(7);

		// get queue from the deque
		Queue nq = Collections.asLifoQueue(deque);
		nq.offer(8);
		System.out.println(nq.poll());
		System.out.println(nq.poll());
		System.out.println("View of the queue is: " + nq);
	}


	/**
	 * 返回指定 collection 的一个动态类型安全视图。试图插入一个错误类型的元素将导致立即抛出
	 * ClassCastException。假设在生成动态类型安全视图之前，collection 不包含任何类型不正确的元素，并且所有对该
	 * collection 的后续访问都通过该视图进行，则可以保证 该 collection 不包含类型不正确的元素。
	 * 一般的编程语言机制中都提供了编译时
	 * （静态）类型检查，但是一些未经检查的强制转换可能会使此机制无效。通常这不是一个问题，因为编译器会在所有这类未经检查的操作上发出警告
	 * 。但有的时候，只进行单独的静态类型检查并不够。例如，假设将 collection 传递给一个第三方库，则库代码不能通过插入一个错误类型的元素来毁坏
	 * collection。
	 * 
	 * 动态类型安全视图的另一个用途是调试。假设某个程序运行失败并抛出 ClassCastException，这指示一个类型不正确的元素被放入已参数化
	 * collection
	 * 中。不幸的是，该异常可以发生在插入错误元素之后的任何时间，因此，这通常只能提供很少或无法提供任何关于问题真正来源的信息。如果问题是可再现的
	 * ，那么可以暂时修改程序，使用一个动态类型安全视图来包装该 collection，通过这种方式可快速确定问题的来源。
	 */
	@Test
	public void checkedCollection() {
		ArrayList<String> arlst = new ArrayList<String>();

		// populate the list
		arlst.add("TP");
		arlst.add("PROVIDES");
		arlst.add("QUALITY");
		arlst.add("TUTORIALS");

		List l = arlst;
		// 这个不会报错
		l.add(2);
		// create typesafe view of the collection
		Collection<String> tslst = Collections.checkedCollection(arlst, String.class);

		Collection t = tslst;
		// 这个会报错
		t.add(2);
		System.out.println("Type safe view is: " + tslst);
	}

	/**
	 * 方法用于将所有从一个列表中的元素复制到另一个。
	 * 
	 * IndexOutOfBoundsException--如果目标表是太小，无法包含整个源列表此异常。
	 * 
	 * UnsupportedOperationException-- 被抛出，如果目的地列表的列表迭代器不支持set操作。
	 */
	@Test
	public void copy() {
		// create two lists
		List<String> srclst = new ArrayList<String>(5);
		List<String> destlst = new ArrayList<String>(10);

		srclst.add("Java");
		srclst.add("is");
		srclst.add("best");

		destlst.add("C++");
		destlst.add("is");
		destlst.add("older");

		Collections.copy(destlst, srclst);

		System.out.println("Value of source list: " + srclst);
		System.out.println("Value of destination list: " + destlst);
	}

	/**
	 * 方法用于为'true'如果两个指定collection中没有相同的元素。
	 */
	public static void disjoint() {
		// create two lists
		List<String> srclst = new ArrayList<String>(5);
		List<String> destlst = new ArrayList<String>(10);

		// populate two lists
		srclst.add("Java");
		srclst.add("is not");
		srclst.add("best");

		destlst.add("C++");
		destlst.add("is");
		destlst.add("older");

		// check elements in both collections
		boolean iscommon = Collections.disjoint(srclst, destlst);

		System.out.println("No commom elements: " + iscommon);
	}

	@Test
	public void test(){
		List<String> emptylst = Collections.emptyList();//生成不可变的list
		List list = Collections.unmodifiableList(new ArrayList(Arrays.asList(new String[]{"a", "b"})));
		Set set = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"a", "b"})));
		Map map = Collections.unmodifiableMap(new HashMap());
	}


	/**
	 * 填充整个集合
	 */
	@Test
	public void fill() {
		// create array list object
		List arrlist = new ArrayList();

		// populate the list
		arrlist.add("A");
		arrlist.add("B");
		arrlist.add("C");

		System.out.println("List elements before fill: " + arrlist);

		// fill the list with 'TP'
		Collections.fill(arrlist, "TP");

		System.out.println("List elements after fill: " + arrlist);
	}

	/**
	 * 方法用于获取所指定元素集合等于指定对象中的数量。
	 */
	public static void frequency() {
		// create array list object
		List arrlist = new ArrayList();

		// populate the list
		arrlist.add("A");
		arrlist.add("B");
		arrlist.add("C");
		arrlist.add("C");
		arrlist.add("C");

		// check frequensy of 'C'
		int freq = Collections.frequency(arrlist, "C");

		System.out.println("Frequency of 'C' is: " + freq);
	}

	/**
	 * 方法用于获取指定源列表中指定的目标列表中第一次出现的起始位置。
	 */
	public static void indexOfSubList() {
		// create two array list objects
		List arrlistsrc = new ArrayList();
		List arrlisttarget = new ArrayList();

		// populate two lists
		arrlistsrc.add("A");
		arrlistsrc.add("B");
		arrlistsrc.add("C");
		arrlistsrc.add("D");
		arrlistsrc.add("E");

		arrlisttarget.add("C");
		arrlisttarget.add("D");
		arrlisttarget.add("E");

		// check target list in source list
		int index = Collections.indexOfSubList(arrlistsrc, arrlisttarget);

		System.out.println("Target list starts at index: " + index);
	}

	public static void list() {
		// create vector and array list
		List arrlist = new ArrayList();
		Vector v = new Vector();

		// populate the vector
		v.add("A");
		v.add("B");
		v.add("C");
		v.add("D");
		v.add("E");

		// create enumeration
		Enumeration e = v.elements();

		// get the list
		arrlist = Collections.list(e);

		System.out.println("Value of returned list: " + arrlist);
	}


	/**
	 * 方法用于返回一个不可变列表组成的n个拷贝的指定对象。
	 */
	public static void ncopies() {
		// create a list with n copies
		List list = Collections.nCopies(5, "tuitorial Point");

		// create an iterator
		Iterator itr = list.iterator();

		System.out.println("Values are :");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
		list.add("a");
	}

	/**
	 * 方法用于返回一组由指定映射支持
	 */
	public static void newSetFromMap() {
		Map<String, Boolean> map = new WeakHashMap<String, Boolean>();

		Set<String> set = Collections.newSetFromMap(map);

		set.add("Java");
		set.add("C");
		set.add("C++");

		// set and map values are
		System.out.println("Set is: " + set);
		System.out.println("Map is: " + map);
	}

	public static void replaceAll() {
		Vector vector = new Vector();

		vector.add("R");
		vector.add("B");
		vector.add("R");

		System.out.println("Initial values are :" + vector);

		Collections.replaceAll(vector, "R", "Replace All");

		System.out.println("Value after replace :" + vector);
	}


	/**
	 * 方法用于获取一个比较有否就实现Comparable接口的对象的集合的自然顺序相反。
	 */
	public static void reverseOrder() {
		LinkedList<Integer> list = new LinkedList();

		list.add(-28);
		list.add(20);
		list.add(-12);
		list.add(8);

		Comparator cmp = Collections.reverseOrder();

		Collections.sort(list, cmp);

		System.out.println("List sorted in ReverseOrder: ");
		for (int i : list) {
			System.out.println(i + " ");
		}
	}

	// 方法用于通过指定的距离进行旋转指定列表中的元素。
	public static void rotate() {
		// create array list object
		List numbers = new ArrayList();

		// populate the list
		for (int i = 0; i < 15; i++) {
			numbers.add(i);
		}

		System.out.println("Before : " + Arrays.toString(numbers.toArray()));

		// rotate the list at distance 10
		Collections.rotate(numbers, 10);

		System.out.println("After : " + Arrays.toString(numbers.toArray()));
	}


	/**
	 * 方法用于返回一个不可变集只包含指定对象。
	 */
	@Test
	public void singleton() {
		// create an array of string objs
		String init[] = { "One", "Two", "Three", "One", "Two", "Three" };

		// create two lists
		List list1 = new ArrayList(Arrays.asList(init));
		List list2 = new ArrayList(Arrays.asList(init));

		// remove from list1
		list1.remove("One");
		System.out.println("List1 value: " + list1);

		// remove from list2 using singleton
		list2.removeAll(Collections.singleton("One"));
		System.out.println("The SingletonList is :" + list2);
	}

	/**
	 * 方法被用于交换在指定列表中的指定位置的元素。
	 */
	@Test
	public void swap() {
		// create vector object
		Vector<String> vector = new Vector<String>();

		// populate the vector
		vector.add("1");
		vector.add("2");
		vector.add("3");
		vector.add("4");
		vector.add("5");

		System.out.println("Before swap: " + vector);

		// swap the elements
		Collections.swap(vector, 0, 4);

		System.out.println("After swap: " + vector);
	}

	/**
	 * 方法用于获得同步的(线程安全的)集合的指定集合的支持。
	 */
	public static void synchronizedCollection() {
		Vector<String> vector = new Vector<String>();

		vector.add("1");
		vector.add("2");
		vector.add("3");
		vector.add("4");
		vector.add("5");

		Collection<String> c = Collections.synchronizedCollection(vector);

		System.out.println("Sunchronized view is :" + c);
	}

	@Test
	public void test1(){
		List list = new ArrayList();
		list.add(123);
		list.add(456);
		list.add(12);
		list.add(78);
		//反转
		Collections.reverse(list);
		System.out.println(list);

		//shuffer 随机排序
		Collections.shuffle(list);
		System.out.println(list);

		//排序
		Collections.sort(list);
		System.out.println(list);

		Collections.swap(list, 2, 1);
		System.out.println(list);
	}

	@Test
	public void test2(){
		List list = new ArrayList();
		list.add(123);
		list.add(456);
		list.add(12);
		list.add(78);
		list.add(78);

		Object obj = Collections.max(list);
		System.out.println(obj);

		int count = Collections.frequency(list, 78);
		System.out.println(count);

//		List list1 = new ArrayList(); //错误的实现方式
		List list1 = Arrays.asList(new Object[list.size()]);
		Collections.copy(list1, list);
		System.out.println(list1);

		//通过如下的方法保证list的线程安全性。
		List list2 = Collections.synchronizedList(list);
		System.out.println(list2);
	}

	public static void main(String[] args) {
		// readonly();
		// addAll();
		// asLifoQueue();
		// binarySearch();
		// checkedCollection();
		// copy();
		// disjoint();
		// emptyList();
		// enumeration();
		// fill();
		// frequency();
		// indexOfSubList();
		// list();
		// max();
		// ncopies();
		// newSetFromMap();
		// replaceAll();
		// rotate();
		// shuffle();
		// singleton();
		// swap();
		// synchronizedCollection();
	}
}
