package MTG.MTG.config;

import MTG.MTG.domain.ChatMessage;
import MTG.MTG.domain.DragImage;
import MTG.MTG.layout.MainView;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import javax.annotation.PostConstruct;

@Configuration
public class MainConfig {

    @Bean
    public UnicastProcessor<ChatMessage> publisher() {
        return UnicastProcessor.create();
    }

    @Bean
    public Flux<ChatMessage> messages(UnicastProcessor<ChatMessage> publisher) {
        return publisher.replay(50).autoConnect();
    }

    @Bean
    public UnicastProcessor<DragImage> imagePublisher() {
        return UnicastProcessor.create();
    }

    @Bean
    public Flux<DragImage> images(UnicastProcessor<DragImage> imagePublisher) {
        return imagePublisher.replay(50).autoConnect();
    }

}
