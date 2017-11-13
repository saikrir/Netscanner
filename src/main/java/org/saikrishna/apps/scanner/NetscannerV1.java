package org.saikrishna.apps.scanner;

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
        int chunkSz = scanNum/NUM_WORKER_THREADS;
        int nChunks = scanNum/chunkSz;
        List<Future<List<LookupResult>>> futures = new ArrayList<>();

        int startIndex = 0;

        for(int i=1; i < nChunks; i++) {
            int endIndex =  i* chunkSz;
            futures.add(this.executorService.submit(new IPAddressRangeScanTask(ipAddressPrefix, startIndex, endIndex )));
            startIndex = endIndex + 1;
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
