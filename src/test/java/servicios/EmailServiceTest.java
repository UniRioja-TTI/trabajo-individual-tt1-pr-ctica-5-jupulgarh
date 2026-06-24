package servicios;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import modelo.Destinatario;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testEnviarEmailRetornaTrue() {
        Destinatario dest = new Destinatario();
        assertTrue(emailService.enviarEmail(dest, "hola"));
    }
}
