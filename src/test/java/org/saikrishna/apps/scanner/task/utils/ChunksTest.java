package org.saikrishna.apps.scanner.task.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.saikrishna.apps.model.ChunkBounds;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class ChunksTest {

    @Test
    public void testChunking() {
        Chunks chunks = new Chunks(5, 16);

        LinkedList<ChunkBounds> collector = new LinkedList<>();
        chunks.iterator().forEachRemaining(collector::add);

        assertEquals(4, collector.size());
        ChunkBounds firstChunk = collector.getFirst();

        assertEquals(0, firstChunk.getStartIdx());
        assertEquals(5    , firstChunk.getEndIdx());

        ChunkBounds lastChunk = collector.getLast();

        assertEquals(15, lastChunk.getStartIdx());
        assertEquals(16  , lastChunk.getEndIdx());
    }


    @Test
    public void testChunkingBoundary() {
        Chunks chunks = new Chunks(5, 1);

        LinkedList<ChunkBounds> collector = new LinkedList<>();
        chunks.iterator().forEachRemaining(collector::add);

        assertEquals(1, collector.size());
        ChunkBounds firstChunk = collector.getFirst();

        assertEquals(0, firstChunk.getStartIdx());
        assertEquals(1    , firstChunk.getEndIdx());
    }
}