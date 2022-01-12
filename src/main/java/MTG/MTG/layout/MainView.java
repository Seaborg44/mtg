package MTG.MTG.layout;

import MTG.MTG.config.MainConfig;
import MTG.MTG.domain.Card;
import MTG.MTG.domain.Deck;
import MTG.MTG.domain.DragImage;
import MTG.MTG.domain.Nutzer;
import MTG.MTG.service.Broadcaster;
import MTG.MTG.service.CardService;
import MTG.MTG.service.DeckService;
import MTG.MTG.service.UserService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Route("main")
@StyleSheet("../CSS/stylesheet.css")
@Push
public class MainView extends VerticalLayout {

    private CardService cardService;
    private DeckService deckService;
    private UserService userService;
    private MainConfig mainConfig;
    public static Nutzer player1;
    public static Nutzer player2;
    public static List<Nutzer> numberOfPlayers = new ArrayList<>();
    private Binder<Board> binder = new Binder<>(Board.class);
    private Board board1 = new Board();
    private Board board2 = new Board();
    private Board board3 = new Board();
    private Board board4 = new Board();
    private Board auxilliaryBoard1;
    private Board auxilliaryBoard2;
    private VerticalLayout player1Hand = new VerticalLayout();
    private VerticalLayout player2Hand = new VerticalLayout();
    private Button navigateToDeckButton = new Button("to DeckComposition");
    private Image mainImage = new Image();
    private Nutzer loggedUser = VaadinSession.getCurrent().getAttribute(Nutzer.class);
    private boolean is2Playeractive = false;
    private boolean is1Playeractive = false;
    private Grid<Deck> playersDeck = new Grid<>(Deck.class);
    private Deck chosenDeck;
    public static String boardId = new String();
    private Button startGameButton = new Button("start");
    private Image handImage;
    private DragImage dragImage;
    Registration broadcasterRegistration;
    private final UnicastProcessor<DragImage> imagePublisher;
    private final Flux<DragImage> images;
    private Span spanToDelete;
    Random rnd = new Random();

    @Autowired
    public MainView(CardService cardService, MainConfig mainConfig, DeckService deckService, UserService userService,
                    Flux<DragImage> images,UnicastProcessor<DragImage> imagePublisher ) throws IOException {
        this.images = images;
        this.imagePublisher = imagePublisher;
        this.cardService = cardService;
        this.mainConfig = mainConfig;
        this.deckService = deckService;
        this.userService = userService;
        assignPlayers();
        add(getNavigateToDeck(), getStartGameButton());
        add(getMainImage());
        add(getPlayer1Hand(), getPlayer2Hand());
        add(new ChatLayout(mainConfig.publisher(), mainConfig.messages(mainConfig.publisher())));
        add(getPlayersDeck());
        add(getBoard1());
        add(getBoard2());
        add(getBoard3());
        add(getBoard4());
    }

    private void assignPlayers() {
        if (loggedUser.getUsername().equals(numberOfPlayers.get(0).getUsername())) {
            is1Playeractive = true;
        } else {
            is2Playeractive = true;
        }
    }

    public Board getBoard1() {
        board1.setId("1");
        board1.getStyle().set("background-color", "yellow");

        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(1).getUsername())) {
                    auxilliaryBoard1 = board1;
                    board1 = board4;
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

        return board1;
    }

    public Board getBoard2() {
        board2.setId("2");
        board2.getStyle().set("background-color", "red");

        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(1).getUsername())) {
                    auxilliaryBoard2 = board2;
                    board2 = board3;
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

        return board2;
    }

    public Board getBoard3() {
        board3.setId("3");
        board3.getStyle().set("background-color", "green");

        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(1).getUsername())) {
                    board3 = auxilliaryBoard2;
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

        return board3;
    }

    public Board getBoard4() {
        board4.setId("4");
        board4.getStyle().set("background-color", "blue");

        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(1).getUsername())) {
                    board4 = auxilliaryBoard1;
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

        return board4;
    }

    public VerticalLayout getPlayer1Hand() {
        player1Hand.setClassName("player-one-hand");
        player1Hand.setMaxWidth("60%");
        return player1Hand;
    }

    public VerticalLayout getPlayer2Hand() {
        player2Hand.setClassName("player-two-hand");
        player2Hand.setMaxWidth("60%");
        return player2Hand;
    }

    public Button getNavigateToDeck() {
        navigateToDeckButton.addClassName("navigate-to-deck");
        navigateToDeckButton.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("/deck");
        });
        return navigateToDeckButton;
    }

    private Image getMainImage() {
        mainImage.setClassName("selected-image");
        mainImage.setMaxWidth(230 + "px");
        mainImage.setMaxHeight(290 +"px");
        return mainImage;
    }

    private Grid<Deck> getPlayersDeck() {
        playersDeck.setClassName("Players-Deck-Main");
        playersDeck.setColumns("deckName");

        if (loggedUser != null) {
            playersDeck.setItems(deckService.getDecksOfTheUser(loggedUser.getId()));
        }

        playersDeck.addItemClickListener(clickEvent -> {
            chosenDeck = clickEvent.getItem();
            try {
                List<Card> list = deckService.fetchAllCardsInTheDeck(chosenDeck.getId());
                deckService.setDeck(deckService.processDeckFromDbToGrid(list));
                chosenDeck.setCards(deckService.getDeck());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return playersDeck;
    }

    private Button getStartGameButton() {
        startGameButton.addClassName("start-game-button");
        startGameButton.addClickListener(event -> {
            getHand();
        });
        return startGameButton;
    }

    private DragImage getHandImage(Card card) {
        dragImage = new DragImage();
        dragImage.addClassName("hand-image");
        dragImage.setSrc(card.getUrl());
        dragImage.setMaxHeight("100%");
        dragImage.setMaxWidth(60 + "px");
        dragImage.getStyle().set("bottom","10%");
        dragImage.addClickListener(clickEvent -> {
            getMainImage().setSrc(clickEvent.getSource().getSrc());
        });
        return dragImage;
    }

    private void getHand() {
        Random rnd = new Random();
        for (int i = 0; i < 7; i++) {
            Card card = chosenDeck.getCards().get(rnd.nextInt(0, chosenDeck.getCards().size()));
            player1Hand.add(getHandImage(card));
//            mainConfig.imagePublisher().onNext(getHandImage(card));
            if (card.getQuantity() > 1) {
                card.quantity--;
            } else {
                card.quantity--;
                chosenDeck.getCards().remove(card);
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(newMessage -> {
            spanToDelete = new Span(newMessage);
            if (is1Playeractive) {
                if (boardId.equals("1")) {
                    ui.access(() -> board1.add(spanToDelete));
                }
                if (boardId.equals("2")) {
                    ui.access(() -> board2.add(spanToDelete));
                }
                if (boardId.equals("3")) {
                    ui.access(() -> board3.add(spanToDelete));
                }
                if (boardId.equals("4")) {
                    ui.access(() -> board4.add(spanToDelete));
                }
            }
            if (is2Playeractive) {
                if (boardId.equals("1")) {
                    ui.access(() -> board4.add(spanToDelete));
                }
                if (boardId.equals("2")) {
                    ui.access(() -> board3.add(spanToDelete));
                }
                if (boardId.equals("3")) {
                    ui.access(() -> board2.add(spanToDelete));
                }
                if (boardId.equals("4")) {
                    ui.access(() -> board1.add(spanToDelete));
                }
            }
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }

}