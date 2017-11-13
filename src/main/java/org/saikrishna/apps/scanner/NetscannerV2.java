package org.saikrishna.apps.scanner;

import org.saikrishna.apps.model.LookupResult;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NetscannerV2 implements INetScanner<List<LookupResult>> {

    private ExecutorService executorService;

    @Override
    public List<LookupResult> scanNetwork(String ipAddresPrefix) {
        return scanNetwork(ipAddresPrefix, DEFAULT_HOST_SET);
    }

    @Override
    public List<LookupResult> scanNetwork(final String ipAddressPrefix, int scanNum) {

        executorService = Executors.newFixedThreadPool(4);

        List<CompletableFuture<List<LookupResult>>> complatableFutures = new ArrayList<>();

        List<LookupResult> scanResults = null;

        int chunkSize = NUM_WORKER_THREADS;
        int nChunks = scanNum / chunkSize;


        for (int i = 0; i < nChunks; i++) {

            final int startIndex = i * chunkSize + 1;
            final int endIndex = startIndex + chunkSize;

            complatableFutures.add(CompletableFuture.supplyAsync(() -> {
                return IntStream.range(startIndex, endIndex)
                        .mapToObj(number -> ipAddressPrefix + number)
                        .map((ipAddress) -> {
                            InetAddress inetAddress = null;
                            try {
                                inetAddress = InetAddress.getByName(ipAddress);
                            }
                            catch (UnknownHostException e) {
                                System.out.println(e);
                            }
                            return new LookupResult( inetAddress.getCanonicalHostName(), ipAddress);
                }).filter(lookupResult -> !lookupResult.getHostName().equals(lookupResult.getIpAddress()))
                        .collect(Collectors.toList());
            },executorService).exceptionally(throwable -> new ArrayList<>()));
        }

        CompletableFuture[] completableFutures = complatableFutures.toArray(new CompletableFuture[complatableFutures.size()]);

        try {
            CompletableFuture.allOf(completableFutures).get();

            scanResults = complatableFutures.stream().map( completedFuture -> {
                List<LookupResult> results = null;
                try {
                    results = completedFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return results;
            }).flatMap(lookupResults -> lookupResults.stream()).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }

        return scanResults;
    }
}
