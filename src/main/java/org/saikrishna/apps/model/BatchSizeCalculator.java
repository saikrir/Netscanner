package org.saikrishna.apps.model;

public class BatchSizeCalculator {


    private int chunkSize;
    private int taskCount;
    private int startIndex;
    private int endIndex;
    private int numChunks;
    private int index;

    public BatchSizeCalculator(int chunkSize, int taskCount) {
        this.chunkSize = chunkSize;
        this.taskCount = taskCount;
        this.numChunks = taskCount / chunkSize;
    }

    public void next() {
        this.index++;
    }

    public boolean canIterate() {
        return this.index < this.numChunks;
    }

    public int getStartIndex() {
        this.startIndex = this.index * this.chunkSize + 1;
        return this.startIndex;
    }

    public int getEndIndex() {
        this.endIndex = this.startIndex + this.chunkSize;
        return this.endIndex;
    }

    public int getNumChunks() {
        return this.numChunks;
    }

}
