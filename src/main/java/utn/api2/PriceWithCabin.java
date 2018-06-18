package utn.api2;

import com.ModelsTP5.dto.CabinDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PriceWithCabin {
    private String nombre;
    private int precio;
    private LocalDate desde;
    private LocalDate hasta;
}
