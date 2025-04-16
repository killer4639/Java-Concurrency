package org.concurrency.multiThreadedMergeSort;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedMergeSort {

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

    private void mergeArray(int[] array, int start, int end) {
        int n = end - start + 1;
        int mid = (start + end) / 2;
        List<Integer> sortedArray = new ArrayList<>(n);
        int i = start;
        int j = mid + 1;
        int index = 0;
        while (index < n) {
            if (i > mid) {
                sortedArray.add(array[j]);
                j++;
                index++;
            } else if (j > end) {
                sortedArray.add(array[i]);
                i++;
                index++;
            } else {
                if (array[i] < array[j]) {
                    sortedArray.add(array[i]);
                    i++;
                    index++;
                } else {
                    sortedArray.add(array[j]);
                    j++;
                    index++;
                }
            }
        }

        for (int idx = 0; idx < n; idx++) {
            array[idx + start] = sortedArray.get(idx);
        }
    }
}
