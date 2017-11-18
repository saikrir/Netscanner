package org.saikrishna.apps.scanner.task.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.net.InetAddress.*;

public class TaskUtils {

    public static boolean isSuccessfulLookup(InetAddress inetAddress, String lookupIpAddress) {

        boolean isSuccessfulLookup = false;

        if(inetAddress!= null) {
            isSuccessfulLookup = !lookupIpAddress.equals(inetAddress.getCanonicalHostName());
        }

        return isSuccessfulLookup;
    }

    public static Iterator<String> ipAddressesInRange(String ipPrefix, int startIndex, int endIndex) {
        List<String> ipAddresses = IntStream.rangeClosed(startIndex, endIndex)
                .mapToObj( number-> ipPrefix + number)
                .collect(Collectors.toList());
        return ipAddresses.iterator();
    }


    public static String localIpAddressPrefix(){
        String retVal = null;
        try {
            String hostAddress = getLocalHost().getHostAddress();
            retVal = extractPrefixUsingRegex(hostAddress);
        } catch (UnknownHostException ignore) {
            System.err.println("Ignoring lookup Address");
        }
        return retVal;
    }

    private static String extractPrefixUsingRegex(String hostAddress) {
        String regEx = "(\\d+\\.)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(hostAddress);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(hostAddress.substring(matcher.start(), matcher.end()));
        }
        return builder.toString();
    }
}
