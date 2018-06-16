package utn.api2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.client.RestTemplate;
@EntityScan("com.ModelsTP5")
@SpringBootApplication
public class Api2Application {

	public static String urlApi;
	public static void main(String[] args) {
		urlApi  = "http://localhost:8080/";
		SpringApplication.run(Api2Application.class, args);
	}
}
