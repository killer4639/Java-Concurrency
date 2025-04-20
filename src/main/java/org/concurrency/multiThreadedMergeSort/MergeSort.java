package org.concurrency.multiThreadedMergeSort;

public abstract class MergeSort {
    private final int size;
    private final int[] sortedArray;

    public MergeSort(int size) {
        this.size = size;
        this.sortedArray = new int[size];
    }

    public abstract void sort(int[] arr, int start, int end);

    protected void mergeArray(int[] array, int start, int mid, int end) {
        int i = start;
        int j = mid + 1;
        int k = start;

        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                sortedArray[k++] = array[i++];
            } else {
                sortedArray[k++] = array[j++];
            }
        }

        while (i <= mid) {
            sortedArray[k++] = array[i++];
        }

        while (j <= end) {
            sortedArray[k++] = array[j++];
        }

        // Copy back to original array
        System.arraycopy(sortedArray, start, array, start, end-start+1);
    }
}
