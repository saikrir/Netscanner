package org.saikrishna.apps.scanner.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.saikrishna.apps.model.LookupResult;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class IPAddressScanResultsSupplierTest {

    IPAddressScanResultsSupplier supplier;

    @Before
    public void setup() throws UnknownHostException {
        supplier = new IPAddressScanResultsSupplier("127.0.0.",1,2);
        spy(supplier);
        InetAddress address = InetAddress.getByName("myhost.domain.com");
    }

    @Test
    public void testGet() throws Exception {
        List<LookupResult> lookupResults = supplier.get();
        //Mockito.verify(supplier.getInetAddress(anyString()), times(2));
        assertEquals(1, lookupResults.size());
    }

}