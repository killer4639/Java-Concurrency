package org.concurrency.multiThreadedMergeSort;

import org.concurrency.AutomatedTest;

import java.util.Random;

/**
 * ForkPool is extreamly optimized for these types of operations
 */
public class MultiThreadedMergeSortTest implements AutomatedTest {
    private final static int size = 10000000;
    private final Random random = new Random();

    @Override
    public void run() {
        int[] arr1 = new int[size];

        for (int i = 0; i < size; i++) {
            arr1[i] = random.nextInt();
        }

        long start = System.currentTimeMillis();
        MergeSort sorter = new MergeSortWithForkJoinPool(arr1.length);
        sorter.sort(arr1, 0, arr1.length - 1);
        System.out.println("Time taken in forkpool: " + (System.currentTimeMillis() - start));
    }
}
