package servicios;

import org.springframework.stereotype.Service;
import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class ContactoSimService implements InterfazContactoSim {

	private final Map<Integer, DatosSolicitud> solicitudesProvisionales = new HashMap<>();
	
	private final List<Entidad> entidades = Arrays.asList(
			new Entidad(1, "Nombre1", "Descripcion1"),
			new Entidad(1, "Nombre2", "Descripcion2"),
			new Entidad(1, "Nombre3", "Descripcion3")
    );
	
	@Override
    public int solicitarSimulation(DatosSolicitud sol) {
		int token = new Random().nextInt(900000) + 100000;
        solicitudesProvisionales.put(token, sol);
        return token;
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        return null;
    }

    @Override
    public List<Entidad> getEntities() {
    	return entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return true;
    }
}