package org.concurrency.unisexBathroomProblem;

import org.concurrency.AutomatedTest;

public class UnisexBathroomTest implements AutomatedTest {
    @Override
    public void run() {
        UnisexBathroomProblem unisexBathroom = new UnisexBathroomProblem();
        Thread female1 = new Thread(new Runnable() {
            public void run() {
                unisexBathroom.femaleUseBathroom();
            }
        });
        Thread male1 = new Thread(new Runnable() {
            public void run() {
                unisexBathroom.maleUseBathroom();
            }
        });
        Thread male2 = new Thread(new Runnable() {
            public void run() {
                unisexBathroom.maleUseBathroom();
            }
        });
        Thread male3 = new Thread(new Runnable() {
            public void run() {
                unisexBathroom.maleUseBathroom();
            }
        });
        Thread male4 = new Thread(new Runnable() {
            public void run() {
                unisexBathroom.maleUseBathroom();
            }
        });
        female1.start();
        male1.start();
        male2.start();
        male3.start();
        male4.start();
        try {
            female1.join();
            male1.join();
            male2.join();
            male3.join();
            male4.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted while joining");
        }
    }
}
