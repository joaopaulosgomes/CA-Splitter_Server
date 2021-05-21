package ie.cct.cbwa.casplittr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ie.cct.*")
public class CaSplittrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaSplittrApplication.class, args);
	}

}
