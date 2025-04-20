package org.concurrency.multiThreadedMergeSort;

import org.concurrency.AutomatedTest;

import java.util.Random;

public class SingleThreadedMergeSortTest implements AutomatedTest {
    private final static int size = 100000000;
    private final Random random = new Random();

    @Override
    public void run() {
        int[] arr1 = new int[size];
        int[] arr2 = new int[size];


        for (int i = 0; i < size; i++) {
            arr1[i] = random.nextInt();
            arr2[i] = arr1[i];
        }

        long start = System.currentTimeMillis();
        MergeSort sorter = new SingleThreadedMergeSort(arr1.length);
        sorter.sort(arr1, 0, arr1.length - 1);
        System.out.println("Time taken in singlethreaded: " + (System.currentTimeMillis() - start));

        for (int i = 0; i < size - 1; i++) {
            if (arr1[i] > arr1[i + 1]) {
                System.out.print(" Wrong " + i + " ");
            }
        }
    }
}
