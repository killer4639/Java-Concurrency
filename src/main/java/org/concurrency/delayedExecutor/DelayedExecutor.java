package org.concurrency.delayedExecutor;

import java.util.PriorityQueue;

/**
 * Takes a runnable and the time after which this should run.
 * It should be thread safe, which means multiple runnables shouldn't run simultaneously,
 * Also if the runnable wait in the code, another runnable should be able to run.
 */
public class DelayedExecutor {
    private final PriorityQueue<Task> queue;
    private final Thread thread;

    public DelayedExecutor() {
        this.queue = new PriorityQueue<>();
        Runnable taskExecutor = () -> {
            synchronized (this) {
                while (true) {
                    while (queue.isEmpty()) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Task executor interrupted");
                            return;
                        }
                    }

                    Task task;
                    while (true) {
                        task = queue.poll();
                        long now = System.currentTimeMillis();
                        if (task != null && task.getTimeToExecute() <= now) {
                            break;
                        } else {
                            queue.add(task);
                            try {
                                this.wait(task.getTimeToExecute() - now);
                            } catch (InterruptedException e) {
                                System.out.println("Task executor interrupted");
                                return;
                            }
                        }
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Task executor interrupted");
                        return;
                    }
                    try {
                        task.getTask().run();
                    } catch (Exception e) {
                        System.out.println("Error in the task");
                        System.out.println(e.getMessage());
                    }
                }
            }
        };

        thread = new Thread(taskExecutor);
        thread.start();
    }

    public synchronized void submitTask(Runnable task, long delay) {
        queue.add(new Task(task, delay));
        notifyAll();
    }

    public void shutdown() {
        thread.interrupt();
    }

    private static class Task implements Comparable<Task> {
        private Runnable task;
        private long timeToExecute;

        public Task(Runnable task, long delay) {
            this.task = task;
            this.timeToExecute = System.currentTimeMillis() + delay;
        }

        public Runnable getTask() {
            return task;
        }

        public void setTask(Runnable task) {
            this.task = task;
        }

        public long getTimeToExecute() {
            return timeToExecute;
        }

        public void setTimeToExecute(long timeToExecute) {
            this.timeToExecute = timeToExecute;
        }

        @Override
        public int compareTo(Task o) {
            return Long.compare(this.timeToExecute, o.timeToExecute);
        }
    }
}
