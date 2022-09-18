package MTG.MTG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class MtgApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtgApplication.class, args);
	}



}
