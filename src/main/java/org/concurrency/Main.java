package org.concurrency;

import org.concurrency.multiThreadedMergeSort.MultiThreadedMergeSortTest;

public class Main {
    public static void main(String[] args) {
        AutomatedTest test = new MultiThreadedMergeSortTest();
        test.run();
    }
}