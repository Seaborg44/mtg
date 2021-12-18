package MTG.MTG.layout;

import MTG.MTG.service.CardService;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Route("/")
public class MainView extends VerticalLayout {

    private CardService cardService;

    @Autowired
    public MainView(CardService cardService) {
        this.cardService = cardService;
        add(getImage());
    }

    private Image getImage() {
        String url = "https://c.tenor.com/tCP_mSVuoLUAAAAC/fake-news-media-donald-trump.gif";
        Image image = new Image(url, "ws");
        return image;
    }

}
