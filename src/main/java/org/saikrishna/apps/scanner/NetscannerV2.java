package org.saikrishna.apps.scanner;

import org.saikrishna.apps.model.ChunkBounds;
import org.saikrishna.apps.model.LookupResult;
import org.saikrishna.apps.scanner.task.IPAddressScanResultsSupplier;
import org.saikrishna.apps.scanner.task.utils.Chunks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class NetscannerV2 implements INetScanner<List<LookupResult>> {

    @Override
    public List<LookupResult> scanNetwork(String ipAddresPrefix) {
        return scanNetwork(ipAddresPrefix, DEFAULT_HOST_SET);
    }

    @Override
    public List<LookupResult> scanNetwork(final String ipAddressPrefix, int numIpsToScan) {

        List<LookupResult> scanResults = null;
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_WORKER_THREADS);
        List<CompletableFuture<List<LookupResult>>> completableFutures = new ArrayList<>();
        Chunks chunks = new Chunks(NUM_WORKER_THREADS, numIpsToScan);

        for (ChunkBounds chunkBounds : chunks) {

            CompletableFuture<List<LookupResult>> listCompletableFuture = supplyAsync(
                    new IPAddressScanResultsSupplier(ipAddressPrefix,
                            chunkBounds.getStartIdx(), chunkBounds.getEndIdx()),
                    executorService);

            completableFutures.add(listCompletableFuture);
        }

        System.out.println("Scanning...");

        scanResults = completableFutures.stream().map(completableFuture -> {
            List<LookupResult> results = null;
            try { results = completableFuture.get(); } catch (InterruptedException | ExecutionException ignored) { }
            return results;
        }).flatMap(lookupResults -> lookupResults.stream())
                .collect(Collectors.toList());

        executorService.shutdown();
        return scanResults;
    }

}
