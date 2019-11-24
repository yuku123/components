package com.zifang.util.core.demo.jdk.util.concurent.packages.forkjoin;

import java.util.concurrent.ForkJoinTask;

public class MyForkJoinTask<V> extends ForkJoinTask<V> {
	

	private static final long serialVersionUID = -6161392123639719799L;

	private V value;

	private boolean success = false;

	@Override
	public V getRawResult() {
		return value;
	}

	@Override
	protected void setRawResult(V value) {
		this.value = value;
	}

	@Override
	protected boolean exec() {
		System.out.println("exec");
		return this.success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}
}
