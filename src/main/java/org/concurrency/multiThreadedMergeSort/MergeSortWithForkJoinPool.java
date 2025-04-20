package org.concurrency.multiThreadedMergeSort;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MergeSortWithForkJoinPool extends MergeSort {
    private final ForkJoinPool forkJoinPool;

    public MergeSortWithForkJoinPool(int size) {
        super(size);
        this.forkJoinPool = new ForkJoinPool();
    }

    @Override
    public void sort(int[] arr, int start, int end) {
        forkJoinPool.invoke(new MergeSortTask(arr, start, end));
    }

    private class MergeSortTask extends RecursiveTask<Void> {
        private final int[] arr;
        private final int start;
        private final int end;
        private boolean completed;

        public MergeSortTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
            this.completed = false;
        }

        @Override
        protected Void compute() {
            if (start == end) {
                return null;
            }

            int mid = (start + end) / 2;
            MergeSortTask mergeSortTask1 = new MergeSortTask(arr, start, mid);
            MergeSortTask mergeSortTask2 = new MergeSortTask(arr, mid + 1, end);

            invokeAll(mergeSortTask1, mergeSortTask2);

            mergeArray(arr, start, mid, end);
            return null;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}
