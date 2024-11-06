package hexlet.code;

import hexlet.code.config.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev")
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testKeyLoading() {
        assertNotNull(jwtTokenProvider.getPrivateKey(), "Private key should not be null");
        assertNotNull(jwtTokenProvider.getPublicKey(), "Public key should not be null");
    }
}
