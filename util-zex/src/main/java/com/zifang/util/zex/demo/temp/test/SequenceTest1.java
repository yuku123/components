package com.zifang.util.zex.demo.temp.test;

import com.zifang.util.zex.demo.temp.Sequence;
import org.junit.Test;

public class SequenceTest1 {

	@Test
	public void name() {
		Sequence sequence = new Sequence(0, 0);
		for (int i = 0; i < 1000; i++) {
			long id = sequence.nextId();
			System.out.println(id);
		}
	}

}
