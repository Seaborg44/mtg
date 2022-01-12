package MTG.MTG;

import MTG.MTG.layout.MainView;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MtgApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtgApplication.class, args);

	}

}
