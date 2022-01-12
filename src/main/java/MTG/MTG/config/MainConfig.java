package MTG.MTG.config;

import MTG.MTG.domain.ChatMessage;
import MTG.MTG.domain.DragImage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

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
