package org.saikrishna.apps.scanner.task;

import org.saikrishna.apps.model.LookupResult;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static org.saikrishna.apps.scanner.task.TaskUtils.ipAddressesInRange;
import static org.saikrishna.apps.scanner.task.TaskUtils.isSuccessfulLookup;

public class IPAddressScanResultsSupplier implements Supplier<List<LookupResult>>{


    private int startIndex;
    private int endIndex;
    private String ipAddressPrefix;

    public IPAddressScanResultsSupplier(String ipAddressPrefix, int startIndex, int endIndex) {
        this.ipAddressPrefix = ipAddressPrefix;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public List<LookupResult> get() {

        Iterator<String> ipAddressesInRange =
                ipAddressesInRange(this.ipAddressPrefix, this.startIndex, this.endIndex);

        List<LookupResult> lookupResults = new ArrayList<>();

        while (ipAddressesInRange.hasNext()) {
            String nextIpAddress =  ipAddressesInRange.next();
            InetAddress inetAddress = null;

            try {
                inetAddress =InetAddress.getByName(nextIpAddress);
            }
            catch (UnknownHostException e) {
                System.err.println(e);
            }

            if(isSuccessfulLookup(inetAddress, nextIpAddress)) {
                lookupResults.add(new LookupResult(inetAddress.getCanonicalHostName(), nextIpAddress));
            }
        }
        return lookupResults;
    }
}
