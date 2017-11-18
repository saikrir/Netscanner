package org.saikrishna.apps.model;

public class ChunkBounds {

    private int startIdx;
    private int endIdx;

    public ChunkBounds(int startIdx, int endIdx) {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }

    public int getStartIdx() {
        return startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    @Override
    public String toString() {
        return "ChunkBounds{" +
                "startIdx=" + startIdx +
                ", endIdx=" + endIdx +
                '}';
    }
}
