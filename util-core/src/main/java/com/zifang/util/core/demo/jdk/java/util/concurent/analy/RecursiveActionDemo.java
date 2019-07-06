package com.zifang.util.core.demo.jdk.java.util.concurent.analy;

import java.util.Arrays;
import java.util.concurrent.*;

public class RecursiveActionDemo {

    private static class SortTask extends RecursiveAction {

        static final int THRESHOLD = 100;

        final long[] array;
        final int lo, hi;

        public SortTask(long[] array, int lo, int hi) {
            this.array = array;
            this.lo = lo;
            this.hi = hi;
        }

        public SortTask(long[] array) {
            this(array, 0, array.length);
        }

        public void sortSequentially(int lo, int hi) {
            Arrays.sort(array, lo, hi);
        }

        public void merge(int lo, int mid, int hi) {
            long[] buf = Arrays.copyOfRange(array, lo, mid);
            for (int i = 0, j = lo, k = mid; i < buf.length; j++) {
                array[j] = (k == hi || buf[i] < array[k]) ? buf[i++] : array[k++];
            }
        }

        @Override
        protected void compute() {
            if (hi - lo < THRESHOLD) {
                sortSequentially(lo, hi);
            }else {
                int mid = (lo + hi) >>> 1;
                invokeAll(new SortTask(array, lo, mid), new SortTask(array, mid, hi));
                merge(lo, mid, hi);
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long[] array = new long[120];
        for (int i = 0; i < array.length; i++) {
            array[i] = (long) (Math.random() * 1000);
        }
        System.out.println(Arrays.toString(array));

        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new SortTask(array));
        pool.awaitTermination(5, TimeUnit.SECONDS);
        pool.shutdown();

    }

}