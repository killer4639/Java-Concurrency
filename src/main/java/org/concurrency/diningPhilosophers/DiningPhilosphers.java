package org.concurrency.diningPhilosophers;

import java.util.HashSet;
import java.util.Set;

class DiningPhilosophers {
    private final Set<Integer> eating;

    public DiningPhilosophers() {
        eating = new HashSet<>();
    }

    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        synchronized (eating) {
            while (!(eating.isEmpty() || (eating.size() == 1 && (eating.contains((philosopher + 2) % 5) || eating.contains((philosopher + 3) % 5))))) {
                eating.wait();
            }
            eating.add(philosopher);
            pickLeftFork.run();
            pickRightFork.run();
        }

        synchronized (eating) {
            eat.run();
            eating.remove(philosopher);
            putLeftFork.run();
            putRightFork.run();
            eating.notifyAll();
        }

    }
}
