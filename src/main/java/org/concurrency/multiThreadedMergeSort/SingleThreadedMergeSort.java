package org.concurrency.multiThreadedMergeSort;

public class SingleThreadedMergeSort extends MergeSort {
    public SingleThreadedMergeSort(int size) {
        super(size);
    }

    @Override
    public void sort(int[] array, int start, int end) {
        if (start == end) {
            return;
        }
        int mid = (start + end) / 2;
        sort(array, start, mid);
        sort(array, mid + 1, end);
        mergeArray(array, start, mid, end);
    }
}
