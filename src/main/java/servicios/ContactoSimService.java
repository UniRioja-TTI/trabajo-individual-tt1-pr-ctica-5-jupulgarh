package servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import modelo.Punto;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
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
    
    private String ultimoTexto = "";
	
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
	
	private DatosSimulation parsearRespuestaGrid(String respuesta) {
	    DatosSimulation datos = new DatosSimulation();
	    
	    if (respuesta == null || respuesta.isBlank()) return datos;

	    String[] lineas = respuesta.strip().split("\\R+");
	    
	    try {
	        datos.setAnchoTablero(Integer.parseInt(lineas[0].trim()));
	    } catch (Exception e) { return datos; }

	    Map<Integer, List<Punto>> puntosPorTiempo = new HashMap<>();
	    int maxTiempo = -1;

	    for (int i = 1; i < lineas.length; i++) {
	        String[] p = lineas[i].split(",");
	        if (p.length != 4) continue;
	        
	        try {
	            int t = Integer.parseInt(p[0].trim());
	            Punto punto = new Punto();
	            punto.setY(Integer.parseInt(p[1].trim()));
	            punto.setX(Integer.parseInt(p[2].trim()));
	            punto.setColor(p[3].trim());
	            
	            puntosPorTiempo.computeIfAbsent(t, k -> new ArrayList<>()).add(punto);
	            maxTiempo = Math.max(maxTiempo, t);
	        } catch (Exception ignored) {}
	    }
	    
	    datos.setPuntos(puntosPorTiempo);
	    datos.setMaxSegundos(maxTiempo + 1);
	    return datos;
	}

    @Override
    public DatosSimulation descargarDatos(int ticket) {
    	String url = baseUrl + "/Resultados?nombreUsuario=" + usuario + "&tok=" + ticket;
        
        Map<String, Object> respuestaJson = restTemplate.postForObject(url, null, Map.class);
        
        if (respuestaJson != null && respuestaJson.containsKey("data")) {
            String textoCrudo = (String) respuestaJson.get("data");
            this.ultimoTexto = textoCrudo;
            
            System.out.println("--- MENSAJE DEL SISTEMA ---");
            System.out.println(this.ultimoTexto);
            
            return parsearRespuestaGrid(textoCrudo);
        }
        return new DatosSimulation();
    }
    
    public String getUltimoTexto() {
        return this.ultimoTexto;
    }

    @Override
    public List<Entidad> getEntities() {
    	return entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
    	return entidades.stream().anyMatch(entidad -> entidad.getId() == id);
    }
}