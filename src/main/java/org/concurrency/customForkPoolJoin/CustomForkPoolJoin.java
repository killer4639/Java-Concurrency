package org.concurrency.customForkPoolJoin;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.IntStream;

public class CustomForkPoolJoin {
    private final List<CustomForkPoolThread> executors;

    public CustomForkPoolJoin(int noOfThreads) {
        this.executors = new ArrayList<>();
        createExecutors(noOfThreads, new Object());

        startAllExecutors();

        startStatusCheckDaemonThread();
    }

    public void invoke(CustomForkJoinRunnable runnable) {
        if (Thread.currentThread() instanceof CustomForkPoolThread curThread) {
            curThread.addTaskToQueue(runnable);
        } else {
            if (!executors.isEmpty()) {
                executors.getFirst().addTaskToQueue(runnable);
                System.out.println("Task added in first queue");
            }
            synchronized (runnable) {
                while (!runnable.isCompleted()) {
                    try {
                        runnable.wait();
                    } catch (InterruptedException interruptedException) {
                        System.out.println("Interrupted while waiting for completion");
                    }
                }
            }
        }
    }

    public void shutdown() {
        for (Thread thread : executors) {
            thread.interrupt();
        }
        for (Thread thread : executors) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted while joining");
            }
        }
    }

    private void startAllExecutors() {
        for (Thread thread : executors) {
            thread.start();
        }
    }

    private void createExecutors(int noOfThreads, Object signalWrite) {
        for (int i = 0; i < noOfThreads; i++) {
            int finalI = i;
            ArrayList<Integer> otherIndices = new ArrayList<>();
            IntStream.range(0, noOfThreads).filter(val -> val != finalI).forEach(otherIndices::add);
            CustomForkPoolThread thread = new CustomForkPoolThread(new ConcurrentLinkedDeque<>(), otherIndices, executors, signalWrite, i);
            executors.add(thread);
        }
    }

    private void startStatusCheckDaemonThread() {
        Thread statusCheck = new Thread(() -> {
            while (true) {
                for (CustomForkPoolThread thread : executors) {
                    System.out.println("Size of queue: " + thread.getTasksCount());
                }
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // skip
                }
            }
        });
        statusCheck.setDaemon(true);
        statusCheck.start();
    }

    private static class CustomForkPoolThread extends Thread {
        private final Deque<CustomForkJoinRunnable> queue;
        private final Object signalWrite;
        private final List<Integer> indices;
        private final List<CustomForkPoolThread> allThreads;
        private final int index;

        public CustomForkPoolThread(Deque<CustomForkJoinRunnable> queue, List<Integer> indices, List<CustomForkPoolThread> allThreads, Object signalWrite, int index) {
            this.queue = queue;
            this.indices = indices;
            this.signalWrite = signalWrite;
            this.allThreads = allThreads;
            this.index = index;
        }

        @Override
        public void run() {
            if (allThreads.size() == 1) {
                runAllTasks();
            } else {
                while (true) {
                    runAllTasks();

                    boolean foundElement = tryStealTask();

                    if (waitForNewTask(foundElement)) break;
                }
            }
        }

        public CustomForkJoinRunnable getLastTask() {
            return queue.pollLast();
        }

        public void addTaskToQueue(CustomForkJoinRunnable task) {
            queue.addFirst(task);
            synchronized (signalWrite) {
                signalWrite.notifyAll();
            }
        }

        public Integer getTasksCount() {
            return queue.size();
        }

        private boolean waitForNewTask(boolean foundElement) {
            if (!foundElement) {
                synchronized (signalWrite) {
                    try {
                        signalWrite.wait();
                    } catch (InterruptedException ex) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean tryStealTask() {
            boolean foundElement = false;
            Collections.shuffle(indices);
            for (Integer idx : indices) {
                CustomForkJoinRunnable stealTask = allThreads.get(idx).getLastTask();
                if (stealTask != null) {
                    queue.addFirst(stealTask);
                    foundElement = true;
                    break;
                }
            }
            return foundElement;
        }

        private void runAllTasks() {
            while (true) {
                CustomForkJoinRunnable task = queue.pollFirst();
                if (task == null) {
                    if (allThreads.size() == 1) {
                        synchronized (signalWrite) {
                            try {
                                signalWrite.wait();
                            } catch (InterruptedException exception) {
                                System.out.println("Interrupted thread: " + index);
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                } else {
                    task.run();
                }
            }
        }
    }
}
