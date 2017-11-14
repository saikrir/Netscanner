package org.saikrishna.apps.scanner.task;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskUtils {

    public static boolean isSuccessfulLookup(InetAddress inetAddress, String lookupIpAddress) {

        boolean isSuccessfulLookup = false;

        if(inetAddress!= null) {
            isSuccessfulLookup = !lookupIpAddress.equals(inetAddress.getCanonicalHostName());
        }

        return isSuccessfulLookup;
    }

    public static Iterator<String> ipAddressesInRange(String ipPrefix, int startIndex, int endIndex) {
        List<String> ipAddresses = IntStream.range(startIndex, endIndex)
                .mapToObj( number-> ipPrefix + number)
                .collect(Collectors.toList());
        return ipAddresses.iterator();
    }
}
