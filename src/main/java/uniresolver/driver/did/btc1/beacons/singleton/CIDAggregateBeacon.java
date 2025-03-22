package uniresolver.driver.did.btc1.beacons.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uniresolver.driver.did.btc1.crud.read.ResolveTargetDocument;

import java.util.Map;

public class CIDAggregateBeacon {

    private static final Logger log = LoggerFactory.getLogger(CIDAggregateBeacon.class);

    public static final String TYPE = "CIDAggregateBeacon";

    /*
     * 5.2.3 Process CIDAggregate Beacon Signal
     */

    // See https://dcdpr.github.io/did-btc1/#process-cidaggregate-beacon-signal
    public static ResolveTargetDocument.Update processCIDAggregateBeaconSignal(ResolveTargetDocument.Tx signalTx, Map<String, Object> signalSidecarData) {

        // TODO
        return null;
    }
}
