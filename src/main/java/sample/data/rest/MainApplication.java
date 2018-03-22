package sample.data.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.web.support.SpringBootServletInitializer;
/**
 * @author Esteban Bett
 */
@SpringBootApplication
@EnableCaching
public class MainApplication  extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}
