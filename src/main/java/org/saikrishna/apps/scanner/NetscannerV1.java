package org.saikrishna.apps.scanner;

import org.saikrishna.apps.model.BatchSizeCalculator;
import org.saikrishna.apps.scanner.task.IPAddressRangeScanTask;
import org.saikrishna.apps.model.LookupResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * NetscannerV1
 */
public class NetscannerV1 implements INetScanner<List<LookupResult>>{

    private int numberOfThreads = NUM_WORKER_THREADS;
    private ExecutorService executorService;

    public NetscannerV1() {
        this(NUM_WORKER_THREADS);
    }

    public NetscannerV1(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.executorService = Executors.newFixedThreadPool(this.numberOfThreads);
    }


    public List<LookupResult> scanNetwork(String ipAddressPrefix){
        return scanNetwork(ipAddressPrefix, DEFAULT_HOST_SET);
    }

    public List<LookupResult> scanNetwork(String ipAddressPrefix, int scanNum){
        BatchSizeCalculator batchSizeCalculator = null;
        List<Future<List<LookupResult>>> futures = new ArrayList<>();

        for(batchSizeCalculator = new BatchSizeCalculator(NUM_WORKER_THREADS, scanNum);
            batchSizeCalculator.canIterate();
            batchSizeCalculator.next()) {

            futures.add(this.executorService.submit(new IPAddressRangeScanTask(ipAddressPrefix,
                    batchSizeCalculator.getStartIndex() , batchSizeCalculator.getEndIndex())));
            batchSizeCalculator.next();
        }

        System.out.println("Scanning ...");

        List<LookupResult> results =  futures.stream().map(listFuture -> {
            List<LookupResult> result = null;
            try {
                result = listFuture.get();
            } catch (InterruptedException | ExecutionException e) { }
            return result;
        }).flatMap(lookupResults -> lookupResults.stream())
                .filter(lookupResult -> lookupResult!=null)
                .collect(Collectors.toList());

        this.executorService.shutdown();
        return results;
    }
}
