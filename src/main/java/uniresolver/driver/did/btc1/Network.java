package uniresolver.driver.did.btc1;

import org.bitcoinj.base.BitcoinNetwork;

public enum Network {

    mainnet,
    signet,
    testnet,
    regtest;

    public org.bitcoinj.base.BitcoinNetwork toBitcoinjNetwork() {
        return switch (this) {
            case mainnet -> BitcoinNetwork.MAINNET;
            case signet -> BitcoinNetwork.SIGNET;
            case testnet -> BitcoinNetwork.TESTNET;
            case regtest -> BitcoinNetwork.REGTEST;
        };
    }
}
