package io.microservices.auth.server;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class CommonUnitTest {

    @Test
    void shouldCreateBCryptPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 16);
        String encodedPassword = encoder.encode("Dipeshm007");

        System.out.println("Password " + encodedPassword);
        assertThat(encodedPassword).isNotEmpty();
    }

}
