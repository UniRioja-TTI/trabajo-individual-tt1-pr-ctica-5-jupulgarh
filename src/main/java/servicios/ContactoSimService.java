package servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

	@Autowired
    private RestTemplate restTemplate;

    @Value("${servicio.consumible.base-url}")
    private String baseUrl;

    @Value("${servicio.usuario}")
    private String usuario;
	
	private final Map<Integer, DatosSolicitud> solicitudesProvisionales = new HashMap<>();
	
	private final List<Entidad> entidades = Arrays.asList(
			new Entidad(1, "Nombre1", "Descripcion1"),
			new Entidad(1, "Nombre2", "Descripcion2"),
			new Entidad(1, "Nombre3", "Descripcion3")
    );
	
	@Override
    public int solicitarSimulation(DatosSolicitud sol) {
		String url = baseUrl + "/Solicitud/Solicitar?nombreUsuario=" + usuario;
		try {
	        Map<String, Object> response = restTemplate.postForObject(url, sol, Map.class);
	        return (int) response.get("tokenSolicitud");
	    } catch (Exception e) {
	        System.err.println("Error al solicitar: " + e.getMessage());
	        return -1;
	    }
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
    	String url = baseUrl + "/Resultados?nombreUsuario=" + usuario + "&tok=" + ticket;
    	try {
    		return restTemplate.postForObject(url, null, DatosSimulation.class);
        } catch (Exception e) {
            System.err.println("Error al descargar: " + e.getMessage());
            return null; 
        }
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