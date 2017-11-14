package org.saikrishna.apps.scanner.task;

import org.saikrishna.apps.model.LookupResult;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.saikrishna.apps.scanner.task.TaskUtils.ipAddressesInRange;
import static org.saikrishna.apps.scanner.task.TaskUtils.isSuccessfulLookup;

public class IPAddressRangeScanTask implements Callable<List<LookupResult>> {

    private String ipPrefix;
    private int startIndex;
    private int endIndex;

    public IPAddressRangeScanTask(String ipPrefix, int startIndex, int endIndex) {
        this.ipPrefix = ipPrefix;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public List<LookupResult> call() throws Exception {

        Iterator<String> ipAddressesInRange = ipAddressesInRange(ipPrefix, startIndex, endIndex);
        List<LookupResult> lookupResults = new ArrayList<>();

        for (Iterator<String> ipAddressIterator = ipAddressesInRange; ipAddressIterator.hasNext(); ) {

            String ipAddress = ipAddressIterator.next();
            InetAddress inetAddress = InetAddress.getByName(ipAddress);

            if(isSuccessfulLookup(inetAddress, ipAddress)){
                lookupResults.add(new LookupResult(inetAddress.getCanonicalHostName(), ipAddress));
            }
        }
        return lookupResults;
    }
}
