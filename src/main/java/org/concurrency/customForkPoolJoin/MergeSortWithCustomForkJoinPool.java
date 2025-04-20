package org.concurrency.customForkPoolJoin;

public class MergeSortWithCustomForkJoinPool {
    private final int POOL_SIZE = 12;
    private final CustomForkPoolJoin forkJoinPool;
    private final int THRESHOLD = 3000; // Threshold for sequential sorting

    public MergeSortWithCustomForkJoinPool() {
        this.forkJoinPool = new CustomForkPoolJoin(POOL_SIZE);
    }

    public void sort(int[] arr, int start, int end) {
        int[] sortedArray = new int[arr.length];
        long startTime = System.currentTimeMillis();
        MergeSortTask task = new MergeSortTask(arr, sortedArray, start, end, null);
        forkJoinPool.invoke(task);
        System.out.println("Time taken in customFork: " + (System.currentTimeMillis() - startTime));
        forkJoinPool.shutdown();
    }

    private class MergeSortTask extends CustomForkJoinRunnable {
        private final int[] arr;
        private final int start;
        private final int end;
        private final int[] sortedArray;

        public MergeSortTask(int[] arr, int[] sortedArray, int start, int end, MergeSortTask parent) {
            super(parent);
            this.arr = arr;
            this.start = start;
            this.end = end;
            this.sortedArray = sortedArray;
        }

        @Override
        public void run() {
            if (end - start <= THRESHOLD) {
                // Use sequential sort for small arrays
                sequentialSort(arr, start, end);
                this.setCompleted();
                if (parent != null) {
                    parent.merge();
                }
                return;
            }

            int mid = (start + end) / 2;
            MergeSortTask mergeSortTask1 = new MergeSortTask(arr, sortedArray, start, mid, this);
            this.addChild(mergeSortTask1);

            MergeSortTask mergeSortTask2 = new MergeSortTask(arr, sortedArray, mid + 1, end, this);
            this.addChild(mergeSortTask2);

            forkJoinPool.invoke(mergeSortTask1);
            forkJoinPool.invoke(mergeSortTask2);
        }

        private void sequentialSort(int[] arr, int start, int end) {
            if (start >= end) return;

            int mid = (start + end) / 2;
            sequentialSort(arr, start, mid);
            sequentialSort(arr, mid + 1, end);
            mergeArray(arr, start, mid, end);
        }

        @Override
        public void mergeResults() {
            mergeArray(arr, start, (start + end) / 2, end);
        }


        private void mergeArray(int[] array, int start, int mid, int end) {
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
            System.arraycopy(sortedArray, start, array, start, end - start + 1);
        }

    }
}
