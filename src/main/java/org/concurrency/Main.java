package org.concurrency;

import org.concurrency.customForkPoolJoin.CustomForkPoolJoinTest;
import org.concurrency.zeroGCLogger.ZeroGCLoggerTest;

public class Main {
    public static void main(String[] args) {
        AutomatedTest test = new ZeroGCLoggerTest();
        test.run();
    }
}