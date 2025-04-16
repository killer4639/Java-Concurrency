package org.concurrency.multiThreadedMergeSort;

import org.concurrency.AutomatedTest;

public class MultiThreadedMergeSortTest implements AutomatedTest {
    @Override
    public void run() {
        int[] arr = {100, 20, 11, 4, 2, 20, 50};
        MultiThreadedMergeSort sorter = new MultiThreadedMergeSort();
        sorter.sort(arr, 0, arr.length - 1);

        for (Integer i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
