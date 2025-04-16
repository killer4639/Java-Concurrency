package org.concurrency;

import org.concurrency.barrier.BarrierTest;
import org.concurrency.unisexBathroomProblem.UnisexBathroomTest;

public class Main {
    public static void main(String[] args) {
        AutomatedTest test = new BarrierTest();
        test.run();
    }
}