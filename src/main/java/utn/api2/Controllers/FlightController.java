package utn.api2.Controllers;

import com.ModelsTP5.Model.Airport;
import com.ModelsTP5.Model.RouteXCabin;
import com.ModelsTP5.Model.Routes;
import com.ModelsTP5.dto.AirportDTO;
import com.ModelsTP5.dto.PriceDTO;
import com.ModelsTP5.dto.RouteDTO;
import org.jboss.logging.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import utn.api2.AirportFinal;
import utn.api2.PriceWithCabin;

import java.time.LocalDate;
import java.util.*;

import static utn.api2.Api2Application.urlApi;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private RestTemplate restTemplate = new RestTemplate();
    private String rtn = "No connection";

    @GetMapping(value = "/", produces = "application/json")
    public Map getAllRoutes(){
        Map<String,Object> map = new HashMap<>();
        try{
            ResponseEntity<AirportDTO[]> airports = restTemplate.getForEntity(urlApi + "airport/",AirportDTO[].class);
            List<AirportDTO> availableAirports = Arrays.asList(airports.getBody());
            List<AirportFinal> airport = new ArrayList();
            for (AirportDTO airportDTO : availableAirports) {
                AirportFinal airportFinal = new AirportFinal(airportDTO.getCiudad().getName()+" ("+airportDTO.getCiudad().getIata()+")"+" - "+airportDTO.getNombre()+" - "+airportDTO.getCiudad().getCountry().getName(),airportDTO.getIata());
                airport.add(airportFinal);
            }
            map.put("Aeropuertos disponibles",airport);
        } catch (Exception e){
            map.put("Error",rtn);
        }
        return map;
    }

    @GetMapping(value = "/flight")
    public Map getPricesForRouteCabin(@RequestHeader(value = "origin")String origen, @RequestHeader(value = "destiny")String destino,@RequestHeader(value = "date") String dateOf){
        Map<String,Object> map = new HashMap<>();
        try {
            ResponseEntity<PriceDTO[]> allPrices = restTemplate.getForEntity(urlApi + "price/especific/" + origen + "/" + destino + "/" + dateOf, PriceDTO[].class);
            RouteDTO route = restTemplate.getForEntity(urlApi +"routes/exactRoute/" +origen+ "/" + destino,RouteDTO.class).getBody();
            List<PriceDTO> all = Arrays.asList(allPrices.getBody());
            map.put("Origen", new AirportFinal(route.getOrigin().getNombre(), route.getOrigin().getIata()));
            map.put("Destino", new AirportFinal(route.getDestination().getNombre(), route.getDestination().getIata()));
            map.put("Fecha", dateOf);
            if (!all.isEmpty()) {
                List<PriceWithCabin> pwc = new ArrayList<>();
                for (PriceDTO pricedto : all) {
                    PriceWithCabin price = new PriceWithCabin(pricedto.getCabin().getNombre(), pricedto.getPrice(), pricedto.getDesde(), pricedto.getHasta());
                    pwc.add(price);
                }
                map.put("Cabinas disponibles", pwc);
            }else {
                String ret = "Sin cabinas asignadas";
                map.put("Cabinas disponibles",ret);
            }
        } catch (Exception e){
            map.put("Error",rtn);
        }
        return map;
    }

    @GetMapping(value = "/destinos")
    public Map getDestinyFromOrigin(@RequestHeader(value = "iata") String iata) {
        Map<String,Object> map = new HashMap<>();
        try {
            ResponseEntity<RouteDTO[]> allRoutes = restTemplate.getForEntity(urlApi + "routes/",RouteDTO[].class);
            List<RouteDTO> rutas = Arrays.asList(allRoutes.getBody());
            List<AirportFinal> destinos = new ArrayList<>();
            for(RouteDTO route : rutas){
                if(iata.equalsIgnoreCase(route.getOrigin().getIata())) {
                    AirportFinal airportFinal = new AirportFinal(route.getDestination().getCiudad().getName()+" - "+route.getDestination().getNombre(), route.getDestination().getIata());
                    destinos.add(airportFinal);
                }
            }
            map.put("Destinos disponibles", destinos);

        }catch (Exception e) {
            map.put("Error",rtn);
        }
        return map;
    }
}
