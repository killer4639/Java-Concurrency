package org.concurrency.multiThreadedMergeSort;

public class MultiThreadedMergeSort extends MergeSort {
    public MultiThreadedMergeSort(int size) {
        super(size);
    }

    public void sort(int[] array, int start, int end) {
        if (start == end) {
            return;
        }
        int mid = (start + end) / 2;
        Runnable runnable1 = () -> {
            sort(array, start, mid);
        };
        Thread thread = new Thread(runnable1);
        thread.start();

        Runnable runnable2 = () -> {
            sort(array, mid + 1, end);
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();

        try {
            thread.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println("Thead Interrupted");
        }

        mergeArray(array, start, end);
    }
}
