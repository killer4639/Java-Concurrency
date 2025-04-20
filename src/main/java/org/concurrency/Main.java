package org.concurrency;

import org.concurrency.customForkPoolJoin.CustomForkPoolJoinTest;

public class Main {
    public static void main(String[] args) {
        AutomatedTest test = new CustomForkPoolJoinTest();
        test.run();
    }
}