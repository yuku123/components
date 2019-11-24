package com.zifang.util.zex.demo.jdk.util.collections.delay;

import java.util.Calendar;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueDemo1 {

	public static void main(String[] args) throws InterruptedException {

		System.out.println("test delay queue.......");

		DelayQueue delayQueue = new DelayQueue();
		long now = System.currentTimeMillis();
		DelayedElement el1 = new DelayedElement(now + 5000);
		DelayedElement el2 = new DelayedElement(now + 3000);
		DelayedElement el3 = new DelayedElement(now + 6000);
		DelayedElement el4 = new DelayedElement(now + 2000);
		delayQueue.offer(el1);
		delayQueue.offer(el2);
		delayQueue.offer(el3);
		delayQueue.offer(el4);
		Delayed poll = null;
		while (delayQueue.size() > 0) {
			poll = delayQueue.poll();
			System.out.println("poll result :" + poll);
			Thread.sleep(500);
		}
	}

	static class DelayedElement implements Delayed {
		private final long delay;
		private final long expire;

		public DelayedElement(long delay) {
			this.delay = delay;
			expire = delay;
		}

		@Override
		public int compareTo(Delayed o) {
			// TODO Auto-generated method stub
			if (this.expire < ((DelayedElement) o).expire)
				return -1;
			else if (this.expire > ((DelayedElement) o).expire)
				return 1;
			else
				return 0;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long r = unit.convert(expire - System.currentTimeMillis(), TimeUnit.NANOSECONDS);
			return r;
		}

		@Override
		public String toString() {
			return "DelayedElement is " + delay + "; Date:" + Calendar.getInstance().getTime().toString()
					+ "; elapsed time is " + getDelay(TimeUnit.DAYS);
		}

	}
}
