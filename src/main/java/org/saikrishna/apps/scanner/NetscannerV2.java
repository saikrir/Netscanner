package org.saikrishna.apps.scanner;

import org.saikrishna.apps.model.BatchSizeCalculator;
import org.saikrishna.apps.model.LookupResult;
import org.saikrishna.apps.scanner.task.IPAddressScanResultsSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class NetscannerV2 implements INetScanner<List<LookupResult>> {

    @Override
    public List<LookupResult> scanNetwork(String ipAddresPrefix) {
        return scanNetwork(ipAddresPrefix, DEFAULT_HOST_SET);
    }

    @Override
    public List<LookupResult> scanNetwork(final String ipAddressPrefix, int scanNum) {

        List<LookupResult> scanResults = null;
        BatchSizeCalculator batchSzCalc = null;
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_WORKER_THREADS);
        List<CompletableFuture<List<LookupResult>>> complatableFutures = new ArrayList<>();

        for ( batchSzCalc = new BatchSizeCalculator(NUM_WORKER_THREADS, scanNum)
              ;batchSzCalc.canIterate()
                ;batchSzCalc.next()) {

            final int startIndex = batchSzCalc.getStartIndex();
            final int endIndex = batchSzCalc.getEndIndex();

            complatableFutures.add(
                CompletableFuture.supplyAsync(
                 new IPAddressScanResultsSupplier(ipAddressPrefix, startIndex, endIndex), executorService)
            );
        }

        scanResults = complatableFutures.stream().map(completableFuture -> {
            List<LookupResult> results = null;
            try { results = completableFuture.get(); } catch (InterruptedException | ExecutionException e) { }
            return results;
        }).flatMap(lookupResults -> lookupResults.stream()).collect(Collectors.toList());
        executorService.shutdown();
        return scanResults;
    }

}
