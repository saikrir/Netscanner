package org.saikrishna.apps.scanner;

public interface INetScanner<R>{

    int NUM_WORKER_THREADS = 10;
    int DEFAULT_HOST_SET = 255;

    R scanNetwork(String ipAddresPrefix);
    R scanNetwork(String ipAddressPrefix, int scanNum);
}
