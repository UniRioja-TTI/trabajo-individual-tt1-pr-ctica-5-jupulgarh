package servicios;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;

@Service
public class EmailService implements InterfazEnviarEmails {

    private final Logger logger;

    // Constructor que recibe el Logger para poder loguear
    public EmailService(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        // Logueo del intento de envío sin realizar la operación real
        logger.info("Simulación de envío de email a: {} con contenido: {}", dest, email);
        return true;
    }
}
