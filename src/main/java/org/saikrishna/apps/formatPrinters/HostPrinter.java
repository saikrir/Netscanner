package org.saikrishna.apps.formatPrinters;

import org.saikrishna.apps.model.LookupResult;

import java.util.List;

public class HostPrinter {

    public void printHosts(List<LookupResult> lookupResults){
        if(lookupResults!=null) {

            System.out.println("Found  [" + lookupResults.size() + "] Entries ");

            lookupResults.forEach(lookupResult -> {
                System.out.println("IP Address [ " + lookupResult.getIpAddress() + " ] -> ( " + lookupResult.getHostName() + " ) " );
            });
        }
    }

}
