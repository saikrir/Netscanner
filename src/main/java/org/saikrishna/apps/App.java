package org.saikrishna.apps;

import org.saikrishna.apps.aop.ExecutionTimeReporter;
import org.saikrishna.apps.formatPrinters.HostPrinter;
import org.saikrishna.apps.model.LookupResult;
import org.saikrishna.apps.scanner.INetScanner;
import org.saikrishna.apps.scanner.NetscannerV1;
import org.saikrishna.apps.scanner.NetscannerV2;
import org.saikrishna.apps.scanner.task.utils.TaskUtils;

import java.util.List;

public class App {

    public static void main(String[] args) {
        INetScanner<List<LookupResult>> scanner = (INetScanner<List<LookupResult>>)
                ExecutionTimeReporter.wrapAround(new NetscannerV1());
        new HostPrinter().printHosts(scanner.scanNetwork(TaskUtils.localIpAddressPrefix(), 20));
    }
}
