package org.saikrishna.apps;

import org.saikrishna.apps.aop.ExecutionTimeReporter;
import org.saikrishna.apps.formatPrinters.HostPrinter;
import org.saikrishna.apps.model.LookupResult;
import org.saikrishna.apps.scanner.INetScanner;
import org.saikrishna.apps.scanner.NetscannerV1;
import org.saikrishna.apps.scanner.NetscannerV2;

import java.util.List;

public class App {
    public static void main(String[] args) {
        INetScanner<List<LookupResult>> scanner = (INetScanner<List<LookupResult>>)
                ExecutionTimeReporter.wrapAround(new NetscannerV2());
        new HostPrinter().printHosts(scanner.scanNetwork("10.68.113.", 100));
    }
}
