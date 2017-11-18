package org.saikrishna.apps.scanner.task.utils;

import org.saikrishna.apps.model.ChunkBounds;

import java.util.Iterator;

public class Chunks implements Iterable<ChunkBounds>{


    private int chunkSize;
    private int taskCount;

    public Chunks(int chunkSize, int taskCount) {
        this.chunkSize = chunkSize;
        this.taskCount = taskCount;
    }

    @Override
    public Iterator<ChunkBounds> iterator() {
        return new ChunkIterator(this.chunkSize, this.taskCount);
    }


    static class ChunkIterator implements Iterator<ChunkBounds> {

        private int chunkSize;
        private double numChunks;
        private int index;
        private int taskCount;

        public ChunkIterator(int chunkSize, int taskCount) {
            this.index = 0;
            this.chunkSize = chunkSize;
            this.numChunks =  Math.ceil((float)taskCount / chunkSize);
            this.taskCount = taskCount;

        }

        @Override
        public boolean hasNext() {
            return this.index < this.numChunks;
        }


        @Override
        public ChunkBounds next() {
            int startIndex = getStartIndex();
            int endIndex =  getEndIndex();
            this.index++;
            return new ChunkBounds(startIndex, endIndex);
        }

        protected int getStartIndex() {
            return this.index * this.chunkSize;
        }

        protected int getEndIndex() {

            int endIndex = getStartIndex() +  this.chunkSize;

            if(endIndex > taskCount) {
                endIndex = getStartIndex() +  (taskCount - getStartIndex());
            }

            return endIndex;
        }
    }

}
