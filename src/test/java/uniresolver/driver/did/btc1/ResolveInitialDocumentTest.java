package uniresolver.driver.did.btc1;

import com.fasterxml.jackson.databind.ObjectMapper;
import foundation.identity.did.DID;
import foundation.identity.did.DIDDocument;
import org.erdtman.jcs.JsonCanonicalizer;
import org.junit.jupiter.api.Test;
import uniresolver.driver.did.btc1.crud.read.ParseDidBtc1Identifier;
import uniresolver.driver.did.btc1.crud.read.ResolveInitialDocument;
import uniresolver.driver.did.btc1.crud.read.records.IdentifierComponents;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResolveInitialDocumentTest {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testResolveInitialDIDDocument() throws Exception {

		Map<String, Object> didDocumentMetadata = new HashMap<>();

		ResolveInitialDocument resolveInitialDocument = new ResolveInitialDocument(TestUtil.testBitcoinConnection(), TestUtil.testIpfsConnection());

		DID identifier = DID.fromString(TestUtil.readResourceString("did.txt"));
		Map<String, Object> resolutionOptions = TestUtil.readResourceJson("resolutionOptions.json");

		IdentifierComponents identifierComponents = ParseDidBtc1Identifier.parseDidBtc1Identifier(identifier, didDocumentMetadata);

		DIDDocument initialDIDDocument = resolveInitialDocument.resolveInitialDIDDocument(identifier, identifierComponents, resolutionOptions, didDocumentMetadata);
		String initialDIDDocumentCanonicalized = new JsonCanonicalizer(initialDIDDocument.toJson()).getEncodedString();
		Map<String, Object> initialDIDDocumentMap = (Map<String, Object>) objectMapper.readValue(initialDIDDocumentCanonicalized, Map.class);

		DIDDocument expectedInitialDIDDocument = DIDDocument.fromMap(TestUtil.readResourceJson("initialDidDocument.json"));
		String expectedInitialDIDDocumentCanonicalized = new JsonCanonicalizer(expectedInitialDIDDocument.toJson()).getEncodedString();
		Map<String, Object> expectedInitialDIDDocumentMap = (Map<String, Object>) objectMapper.readValue(expectedInitialDIDDocumentCanonicalized, Map.class);

		assertEquals(expectedInitialDIDDocumentCanonicalized, initialDIDDocumentCanonicalized);
		assertEquals(expectedInitialDIDDocumentMap, initialDIDDocumentMap);
	}
}
