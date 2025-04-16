package org.concurrency.multiThreadedMergeSort;

import java.util.ArrayList;
import java.util.List;

public abstract class MergeSort {
    private final int size;
    private final int[] sortedArray;

    public MergeSort(int size) {
        this.size = size;
        this.sortedArray = new int[size];
    }

    abstract void sort(int[] arr, int start, int end);

    protected void mergeArray(int[] array, int start, int end) {
        int mid = (start + end) / 2;
        int i = start;
        int j = mid + 1;
        int index = start;
        while (index <= end) {
            if (i > mid) {
                sortedArray[index] = array[j];
                j++;
            } else if (j > end) {
                sortedArray[index] = array[i];
                i++;
            } else {
                if (array[i] < array[j]) {
                    sortedArray[index] = array[i];
                    i++;
                } else {
                    sortedArray[index] = array[j];
                    j++;
                }
            }
            index++;
        }

        for (int idx = 0; idx + start <= end; idx++) {
            array[idx + start] = sortedArray[idx+start];
        }
    }
}
