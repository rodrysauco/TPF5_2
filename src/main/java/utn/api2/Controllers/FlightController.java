package utn.api2.Controllers;

import com.ModelsTP5.Model.Airport;
import com.ModelsTP5.dto.AirportDTO;
import com.ModelsTP5.dto.RouteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import utn.api2.AirportFinal;

import java.util.*;

import static utn.api2.Api2Application.urlApi;

@RestController
@RequestMapping("/flight")
public class FlightController {
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/", produces = "application/json")
    public Map getAllRoutes(){
        ResponseEntity<AirportDTO[]> airports = restTemplate.getForEntity(urlApi + "airport/",AirportDTO[].class);
        List<AirportDTO> availableAirports = Arrays.asList(airports.getBody());
        Map<String,List> map = new HashMap<>();
        List<AirportFinal> airport = new ArrayList();
        for (AirportDTO airportDTO : availableAirports) {
            AirportFinal airportFinal = new AirportFinal(airportDTO.getNombre(),airportDTO.getIata());
            airport.add(airportFinal);
        }
        map.put("Aiports available",airport);
        return map;
    }
}
