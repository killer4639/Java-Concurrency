package org.concurrency.customForkPoolJoin;

import org.concurrency.AutomatedTest;

import java.util.Random;

public class CustomForkPoolJoinTest implements AutomatedTest {
    private final static int size = 100000000;
    private final Random random = new Random();

    @Override
    public void run() {
        int[] arr1 = new int[size];
        int[] arr2 = new int[size];


        System.out.println();
        for (int i = 0; i < size; i++) {
            arr1[i] = random.nextInt(0, 500);
            arr2[i] = arr1[i];
//            System.out.print(arr1[i] + " ");
        }
        System.out.println();

        MergeSortWithCustomForkJoinPool sorter = new MergeSortWithCustomForkJoinPool();
        sorter.sort(arr1, 0, arr1.length - 1);

        for (int i = 0; i < size; i++) {
//            System.out.print(arr1[i] + " ");
        }
        for (int i = 0; i < size - 1; i++) {
            if (arr1[i] > arr1[i + 1]) {
                System.out.print(" Wrong " + i + " ");
            }
        }
    }
}
