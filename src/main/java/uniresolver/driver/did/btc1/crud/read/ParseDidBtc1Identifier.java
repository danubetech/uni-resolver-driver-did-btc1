package uniresolver.driver.did.btc1.crud.read;

import foundation.identity.did.DID;
import org.bitcoinj.base.Bech32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uniresolver.ResolutionException;
import uniresolver.driver.did.btc1.Network;
import uniresolver.driver.did.btc1.crud.read.records.IdentifierComponents;

public class ParseDidBtc1Identifier {

    private static final Logger log = LoggerFactory.getLogger(ParseDidBtc1Identifier.class);

    /*
     * 4.2.1 Parse did:btc1 Identifier
     */

    // See https://dcdpr.github.io/did-btc1/#parse-didbtc1-identifier
    public static IdentifierComponents parseDidBtc1Identifier(DID identifier) throws ResolutionException {

        if (identifier == null) throw new IllegalArgumentException("Identifier cannot be null");

        // 1. Set identifierComponents to an empty object.

        IdentifierComponents identifierComponents;
        Network identifierComponentsNetwork;
        Integer identifierComponentsVersion;
        String identifierComponentsHrp;
        byte[] identifierComponentsGenesisBytes;

        // 2. Using a colon (:) as the delimiter, split the identifier into an array of components.
        // 3. Set scheme to components[0].
        // 4. Set methodId to components[1].

        String scheme = DID.URI_SCHEME;
        String methodId = identifier.getMethodName();
        String methodSpecificId = identifier.getMethodSpecificId();
        String[] methodSpecificIdParts = methodSpecificId.split(":");

        // 5. If the length of components equals 3, set identifierComponents.version to 1
        // and identifierComponents.network to mainnet. Set idBech32 to components[2].

        String version;
        String network;
        String idBech32;

        if (methodSpecificIdParts.length == 1) {

            version = "1";
            network = "mainnet";
            idBech32 = methodSpecificIdParts[0];

        // 6. Else if length of components equals 4, check if components[2] can be cast to an
        // integer. If so, set identifierComponents.version to components[2] and
        // identifierComponents.network to mainnet. Otherwise, set identifierComponents.network
        // to components[2] and identifierComponents.version to 1. Set idBech32 to components[3].

        } else if (methodSpecificIdParts.length == 2) {

            if (canBeConvertedToInteger(methodSpecificIdParts[0])) {
                version = methodSpecificIdParts[0];
                network = "mainnet";
            } else {
                version = "1";
                network = methodSpecificIdParts[0];
            }
            idBech32 = methodSpecificIdParts[1];

        // 7. Else if the length of components equals 5, set identifierComponents.version to
        // components[2], identifierComponents.network to components[3] and idBech32 to the components[4].

        } else if (methodSpecificIdParts.length == 3) {

            version = methodSpecificIdParts[0];
            network = methodSpecificIdParts[1];
            idBech32 = methodSpecificIdParts[2];

        // 8. Else MUST raise InvalidDID error. There are an incorrect number of components to the identifier.

        } else {
            throw new ResolutionException(ResolutionException.ERROR_INVALIDDID, "Invalid number of segments in method-specific identifier '" + methodSpecificId + "': " + methodSpecificIdParts.length);
        }

        // 9. Check the validity of the identifier components. The scheme MUST be the value did. The methodId
        // MUST be the value btc1. The identifierComponents.version MUST be convertible to a positive integer
        // value. The identifierComponents.network MUST be one of mainnet, signet, testnet, or regtest. If any
        // of these requirements fail then an InvalidDID error MUST be raised.

        if (! "btc1".equals(methodId)) {
            throw new ResolutionException(ResolutionException.ERROR_METHODNOTSUPPORTED, "Method not supported: " + methodId);
        }
        try {
            identifierComponentsVersion = Integer.parseInt(version);
        } catch (NumberFormatException ex) {
            throw new ResolutionException(ResolutionException.ERROR_INVALIDDID, "Invalid version: " + version);
        }
        try {
            identifierComponentsNetwork = Network.valueOf(network);
        } catch (IllegalArgumentException ex) {
            throw new ResolutionException(ResolutionException.ERROR_INVALIDDID, "Invalid network: " + network);
        }

        // 10. Decode idBech32 using the Bech32 algorithm to get decodeResult.

        Bech32.Bech32Data bech32Data = Bech32.decode(idBech32);

        // 11. Set identifierComponents.hrp to decodeResult.hrp.

        identifierComponentsHrp = bech32Data.hrp;

        // 12. Set identifierComponents.genesisBytes to decodeResult.value.

        identifierComponentsGenesisBytes = bech32Data.decode5to8();

        // 13. Return identifierComponents.

        identifierComponents = new IdentifierComponents(identifierComponentsNetwork, identifierComponentsVersion, identifierComponentsHrp, identifierComponentsGenesisBytes);

        if (log.isDebugEnabled()) log.debug("parseDidBtc1Identifier: " + identifierComponents);
        return identifierComponents;
    }

    /*
     * Helper methods
     */

    private static boolean canBeConvertedToInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
