package org.saikrishna.apps.scanner.task.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TaskUtilsTest {

    private InetAddress inetAddress;


    @Before
    public void setUp() throws Exception {
        inetAddress = InetAddress.getByName("myhost.domain.com");
    }


    @Test
    public void testIPAddressesGenerationTest(){
        int startAddress = 0;
        int endAddress = 100;
        String prefix = "10.10.10.";
        Iterator<String> resultsIterator = TaskUtils
                                .ipAddressesInRange(prefix,startAddress, endAddress);

        List<String> ipAddresses = new ArrayList<>();
        resultsIterator.forEachRemaining(ipAddresses::add);

        assertEquals(ipAddresses.get(0), "10.10.10.0");
        assertEquals(ipAddresses.get(100), "10.10.10.100");
    }


    @Test
    public void testLookupSuccess() throws Exception {
        assertTrue(TaskUtils.isSuccessfulLookup(inetAddress, "10.10.10.10"));
    }


    @Test
    public void testLookupFail() throws Exception {
        inetAddress = InetAddress.getByName("10.10.10.10");
        assertFalse(TaskUtils.isSuccessfulLookup(inetAddress, "10.10.10.10"));
    }
}