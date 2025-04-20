package org.concurrency.customForkPoolJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CustomForkJoinRunnable implements Runnable {
    private AtomicBoolean isCompleted;
    protected final CustomForkJoinRunnable parent;
    protected final List<CustomForkJoinRunnable> children;

    public CustomForkJoinRunnable(CustomForkJoinRunnable parent) {
        this.isCompleted = new AtomicBoolean();
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    protected void addChild(CustomForkJoinRunnable child){
        this.children.add(child);
    }

    public void merge() {
        for (CustomForkJoinRunnable task : children) {
            if (!task.isCompleted()) {
                return;
            }
        }
        mergeResults();
        this.setCompleted();
        if (parent != null) {
            parent.merge();
        }
    }

    protected abstract void mergeResults();

    public void setCompleted() {
        this.isCompleted.set(true);
        synchronized (this) {
            this.notifyAll();
        }
    }

    public boolean isCompleted() {
        return this.isCompleted.get();
    }
}
