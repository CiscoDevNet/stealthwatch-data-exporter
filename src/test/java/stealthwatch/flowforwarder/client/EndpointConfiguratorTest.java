package stealthwatch.flowforwarder.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_STORE_FILE;
import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_STORE_PASSWORD;
import static org.junit.Assert.assertEquals;

public class EndpointConfiguratorTest {

    @Test
    public void beforeRequest() {
        System.setProperty(KEY_STORE_FILE, getClass().getResource("data-exporter.pkcs12").getFile());
        System.setProperty(KEY_STORE_PASSWORD, "data-exporter");

        Map<String, List<String>> headers = new HashMap<>();
        new EndpointConfigurator().beforeRequest(headers);

        String certificate = headers.get("X-nginx-ssl-client-cert").get(0);

        //noinspection StringConcatenationMissingWhitespace
        assertEquals("-----BEGIN CERTIFICATE-----\t" +
                     "MIIFTDCCAzQCAQEwDQYJKoZIhvcNAQELBQAwajELMAkGA1UEBhMCVVMxEzARBgNV" +
                     "BAgMCkNhbGlmb3JuaWExETAPBgNVBAcMCFNhbiBKb3NlMQ4wDAYDVQQKDAVDaXNj" +
                     "bzEPMA0GA1UECwwGRGV2TmV0MRIwEAYDVQQDDAljaXNjby5jb20wHhcNMTgwOTI0" +
                     "MTU1MTA4WhcNMTkwOTI0MTU1MTA4WjBuMQswCQYDVQQGEwJVUzEQMA4GA1UECAwH" +
                     "R2VvcmdpYTEQMA4GA1UEBwwHQXRsYW50YTEOMAwGA1UECgwFQ2lzY28xFTATBgNV" +
                     "BAsMDFN0ZWFsdGh3YXRjaDEUMBIGA1UEAwwLbGFuY29wZS5jb20wggIiMA0GCSqG" +
                     "SIb3DQEBAQUAA4ICDwAwggIKAoICAQDYQdIOzmIDicYHjhYU8u+kJoMXvBuLP0fR" +
                     "z4S44p3b8fipp3Bib2/sZLuHVcY70fLefJBd3M2hO999Zx+q8pfihMzP015NdJ1m" +
                     "FcaKDdW+lZRfqsf6XULltgIG47hdt8NmxISiVL4rjR60KYt+YkXtXkNYtVTCI6eC" +
                     "ZSXiYMCvj4udh85ZUtXeFNFX96HYPKN/tcFtRAbjrE6ASC4okbGnGbEPLPw9XB30" +
                     "kak7Te9JLVUW1ujjM5loTBkXwAJAmsTWqb3gGmHlBf8FjeuME1ZBvVA1pX9PEpIe" +
                     "48RU+qlZeXx+PgN1Fr3s83/Mdo8Ut0t1B4piukUrAe5i+Qap7RpSqAnUllTyzUqD" +
                     "tKGItyxVdUMSmAEtdZuk/N5Vd8nYHfYpcK3VcM80uFNSy/nDbkvxXnL0+VvNKaPB" +
                     "KXvsJQ5JEThcQYZjbpXYVGfkJSRYolfjdmfCLlGJE020Slss7xjRLGHwz79Nzop5" +
                     "u9jmjPLPWgQdpckeTlh4bkioCT9hTGohJsG5GllJjTJDNZUwCe2RXFqOy3Kc8bU1" +
                     "Ul4ICzoz9WYJRLC56Jzd2IH1fAqa4xTfYvK+ZMWyPOJdwEopAOsEOVXfyjCTHbNk" +
                     "zzu/1XhcIN/Dja15qL74pFl0HGUZOcy/zbwPfAqpnVcwhlgkcyAXTVQMUExpmYR5" +
                     "4/iLJo/C6QIDAQABMA0GCSqGSIb3DQEBCwUAA4ICAQAYPXwwF0rZeV8+2W4lI8M0" +
                     "4tYdLTN/IWZfn20y9EwjluS368qzB5KO99b2kS1XgdqiROHytSyZILyHUheZ67GS" +
                     "Opo0jxvCEDANKJIKLYv5uUs90rMWLXuE+M7V/VshdBsOzdFfsub+G0nVsMEVoJcJ" +
                     "A2pd1NfOlZoWvjo8nNmSJU21vpkr3kGD6HclaLPqVU3fz8x/YIXlURRjj5CyPEMj" +
                     "P69d4xQYWa4hvsNlmryOWtLj0ctuUZBseFNEk6GdNBwTqzHFejcYEnYB1ll73MVg" +
                     "MzC6VeNC+3XiLbVf5irYWv9WHf5oBcN1vrUxeX7SSqhhrV+9C1SvORAug2mLehNw" +
                     "pBx5T9xdOI37OMiRVuQf3PcOLfG8qn8FVtouDZoFEi31UG5jIW8BsycJEYvEG4pg" +
                     "CoLaJkNZPUc3ddkK4MSMU+wLKAWYejtkWVgZ4ZPt468+T29B4YpQK4ht9qWYjbaa" +
                     "tA7Y1+4QoxLd0wbFvjnHYuuBXR4eK6a8s2G16BX8XxOehddFnBL0VUsEiDK6v/PG" +
                     "zjV0xk4KvplfQMbnI5TLytIfWPnOKH8Xzi76csemZStR3/9Yi9mrLVj+LiXievgA" +
                     "RYQvDgKxT+GP26nE+nKaRW+RwoH02MDZCzguWb+LcHPoBxkKoH33p90hJq4LQLQM" +
                     "M9VlEaKDivxybSO8LEHsWw==\t" +
                     "-----END CERTIFICATE-----",
                     certificate);
    }

}
