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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinContext;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinServletContext;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.VaadinWebsocketEndpointExporter;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Route("main")
@StyleSheet("../CSS/stylesheet.css")
@Push
public class MainView extends VerticalLayout {

    private CardService cardService;
    private DeckService deckService;
    private UserService userService;
    private MainConfig mainConfig;
    public static Nutzer player1 = new Nutzer();
    public static Nutzer player2 = new Nutzer();
    public static List<Nutzer> numberOfPlayers = new ArrayList<>();
    private Board board1 = new Board();
    private Board board2 = new Board();
    private Board board3 = new Board();
    private Board board4 = new Board();
    private Board auxilliaryBoard1;
    private Board auxilliaryBoard2;
    private VerticalLayout player1Hand = new VerticalLayout();
    private VerticalLayout player2Hand = new VerticalLayout();
    private VerticalLayout auxilliaryPlayer1Hand = player1Hand;
    private Button navigateToDeckButton = new Button("to DeckComposition");
    private Image mainImage = new Image();
    private Nutzer loggedUser = VaadinSession.getCurrent().getAttribute(Nutzer.class);
    private boolean is2Playeractive = false;
    private boolean is1Playeractive = false;
    private Grid<Deck> playersDeck = new Grid<>(Deck.class);
    private Deck chosenDeck;
    public static String boardId = new String();
    public static String dragStartBoardId = new String();
    private Button startGameButton = new Button("start");
    private Image handImage;
    private DragImage dragImage;
    private DragImage accessDragImage;
    private static String idOfDragImage = new String();
    public static String url;
    public static String mainImgUrl;
    private final UnicastProcessor<DragImage> imagePublisher;
    private final Flux<DragImage> images;
    private UI mainUI;
    Registration broadcasterRegistration;
    Registration broadcasterRegistration2;
    Random rnd = new Random();
    private Button drawCardButton = new Button("Draw");

    @Autowired
    public MainView(CardService cardService, MainConfig mainConfig, DeckService deckService, UserService userService,
                    Flux<DragImage> images, UnicastProcessor<DragImage> imagePublisher) throws IOException {
        this.images = images;
        this.imagePublisher = imagePublisher;
        this.cardService = cardService;
        this.mainConfig = mainConfig;
        this.deckService = deckService;
        this.userService = userService;
        assignPlayers();
        add(getNavigateToDeck(), getStartGameButton(), getDrawCardButton());
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
        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(0).getUsername())) {
                is1Playeractive = true;
            } else {
                is2Playeractive = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Board getBoard1() {
        board1.setId("1");
        board1.getStyle().set("background-color", "yellow");
        board1.addClickListener(click -> Notification.show(click.getSource().getId().get()));
        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(0).getUsername())) {
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
        board2.addClickListener(click -> Notification.show(click.getSource().getId().get()));
        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(0).getUsername())) {
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
        board3.addClickListener(click -> Notification.show(click.getSource().getId().get()));
        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(0).getUsername())) {
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
        board4.addClickListener(click -> Notification.show(click.getSource().getId().get()));
        try {
            if (loggedUser.getUsername().equals(numberOfPlayers.get(0).getUsername())) {
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
        player1Hand.getStyle().set("background-color", "grey");

        return player1Hand;
    }

    public VerticalLayout getPlayer2Hand() {

        player2Hand.setClassName("player-two-hand");
        player2Hand.setMaxWidth("60%");
        player2Hand.getStyle().set("background-color", "black");
        broadcasterRegistration = Broadcaster.register2(id -> {
            DragImage dragImage = createDragImage();
            dragImage.setSrc("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/513b7bfa-42c9-4d08-ad6c-8e5d478c42d3/dalfpib-83f22b02-5802-40b4-901b-3eecf0ca2058.png/v1/fill/w_1024,h_1463,q_80,strp/unofficial_magic_the_gathering_six_color_card_back_by_lordnyriox_dalfpib-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9MTQ2MyIsInBhdGgiOiJcL2ZcLzUxM2I3YmZhLTQyYzktNGQwOC1hZDZjLThlNWQ0NzhjNDJkM1wvZGFsZnBpYi04M2YyMmIwMi01ODAyLTQwYjQtOTAxYi0zZWVjZjBjYTIwNTgucG5nIiwid2lkdGgiOiI8PTEwMjQifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6aW1hZ2Uub3BlcmF0aW9ucyJdfQ.OGsglye34mbde4lkXNsDFFCl83jSzl1zNBLep-hhyKY");
            dragImage.setId(id);
            getUI().ifPresent(ui -> ui.access(() -> player2Hand.add(dragImage)));
        });

        return player2Hand;
    }

    public Button getNavigateToDeck() {
        navigateToDeckButton.addClassName("navigate-to-deck");
        navigateToDeckButton.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("/deck");
        });
        return navigateToDeckButton;
    }
    private Button getDrawCardButton() {
        drawCardButton.addClassName("draw-card-button");
        drawCardButton.addClickListener(click -> drawCard());

        return drawCardButton;
    }


    public Image getMainImage() {
        mainImage.setClassName("selected-image");
        mainImage.setMaxWidth(230 + "px");
        mainImage.setMaxHeight(290 + "px");

        mainImage.addClickListener(clickEvent -> {
            Notification.show(is1Playeractive + " " + is2Playeractive);
        });
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
        dragImage.setId(String.valueOf(rnd.nextInt(44, 4444444)));
        dragImage.getStyle().set("bottom", "10%");

        dragImage.addClickListener(clickEvent -> {
            getMainImage().setSrc(clickEvent.getSource().getSrc());
            this.url = clickEvent.getSource().getSrc();
            this.idOfDragImage = String.valueOf(clickEvent.getSource().getId().get());
            Notification.show(clickEvent.getSource().getId().get());
        });

        dragImage.addDragStartListener(drag -> {
            getMainImage().setSrc(drag.getSource().getSrc());
            this.url = drag.getSource().getSrc();
            this.idOfDragImage = String.valueOf(drag.getSource().getId().get());
            this.dragStartBoardId = drag.getSource().getParent().get().getId().orElseThrow(NoSuchElementException::new);
        });

        return dragImage;
    }

    private void getHand() {
        Random rnd = new Random();
        for (int i = 0; i < 7; i++) {
            Card card = chosenDeck.getCards().get(rnd.nextInt(0, chosenDeck.getCards().size()));
            DragImage dragImage = getHandImage(card);
            player1Hand.add(dragImage);
            if (is1Playeractive) Broadcaster.broadcast2(dragImage.getId().get(), 1);
            if (is2Playeractive) Broadcaster.broadcast2(dragImage.getId().get(), 0);

            if (card.getQuantity() > 1) {
                card.quantity--;
            } else {
                card.quantity--;
                chosenDeck.getCards().remove(card);
            }
        }
    }


    private DragImage createDragImage() {
        DragImage dragImage2 = new DragImage();
        dragImage2.setMaxHeight("100%");
        dragImage2.setMaxWidth(60 + "px");
        dragImage2.setId(String.valueOf(rnd.nextInt(44, 4444444)));
        dragImage2.addClickListener(clickEvent -> {
            getMainImage().setSrc(clickEvent.getSource().getSrc());
            this.url = clickEvent.getSource().getSrc();
            this.idOfDragImage = String.valueOf(clickEvent.getSource().getId().get());
            Notification.show(clickEvent.getSource().getId().get());
        });

        dragImage2.addDragStartListener(drag -> {
            this.url = drag.getSource().getSrc();
            this.idOfDragImage = String.valueOf(drag.getSource().getId().get());
            this.dragStartBoardId = drag.getSource().getParent().get().getId().orElseThrow(NoSuchElementException::new);
        });

        return dragImage2;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();

        broadcasterRegistration = Broadcaster.register(newMessage -> {
            accessDragImage = createDragImage();
            accessDragImage.setId(idOfDragImage);
            accessDragImage.setSrc(url);
            deleteFromDragStartBoard(ui, dragStartBoardId);
            if (is2Playeractive) {
                if (boardId.equals("1")) {
                    ui.access(() -> board1.add(accessDragImage));
                }
                if (boardId.equals("2")) {
                    ui.access(() -> board2.add(accessDragImage));
                }
                if (boardId.equals("3")) {
                    ui.access(() -> board3.add(accessDragImage));
                }
                if (boardId.equals("4")) {
                    ui.access(() -> board4.add(accessDragImage));
                }
            }

            if (is1Playeractive) {
                if (boardId.equals("1")) {
                    ui.access(() -> board4.add(accessDragImage));
                }
                if (boardId.equals("2")) {
                    ui.access(() -> board3.add(accessDragImage));
                }
                if (boardId.equals("3")) {
                    ui.access(() -> board2.add(accessDragImage));
                }
                if (boardId.equals("4")) {
                    ui.access(() -> board1.add(accessDragImage));
                }
            }


            deleteFromHand(ui, accessDragImage.getId().get());
        });

    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }


    private void deleteFromDragStartBoard(UI ui, String idOfBoard) {

        try {
            switch (idOfBoard) {
                case "4":
                    Optional<Component> diToRemove = board1.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove != null)
                        ui.access(() -> board1.remove(diToRemove.get()));
                    break;
                case "3":
                    Optional<Component> diToRemove2 = board2.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove2 != null)
                        ui.access(() -> board2.remove(diToRemove2.get()));
                    break;
                case "2":
                    Optional<Component> diToRemove3 = board3.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove3 != null)
                        ui.access(() -> board3.remove(diToRemove3.get()));
                    break;
                case "1":
                    Optional<Component> diToRemove4 = board4.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove4 != null)
                        ui.access(() -> board4.remove(diToRemove4.get()));
                    break;
                default:
                    ui.access(() -> Notification.show("no id present"));
            }

            switch (idOfBoard) {
                case "1":
                    Optional<Component> diToRemove = board1.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove != null)
                        ui.access(() -> board1.remove(diToRemove.get()));
                    break;
                case "2":
                    Optional<Component> diToRemove2 = board2.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove2 != null)
                        ui.access(() -> board2.remove(diToRemove2.get()));
                    break;
                case "3":
                    Optional<Component> diToRemove3 = board3.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove3 != null)
                        ui.access(() -> board3.remove(diToRemove3.get()));
                    break;
                case "4":
                    Optional<Component> diToRemove4 = board4.getChildren()
                            .filter(f -> f.getClass().equals(DragImage.class))
                            .filter(f -> f.getId().get().equals(idOfDragImage))
                            .findFirst();
                    if (diToRemove4 != null)
                        ui.access(() -> board4.remove(diToRemove4.get()));
                    break;
                default:
                    ui.access(() -> Notification.show("no id present"));
            }
        } catch (Exception e) {

        }
    }


    public void deleteFromHand(UI ui, String id) {
        Optional<Component> cardToRemove = Optional.ofNullable(player2Hand.getChildren().collect(Collectors.toList()).stream()
                .filter(f -> f.getId().get().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("could not find an element with such id: " + id)));
        System.out.println(cardToRemove);
        if (cardToRemove != null) {
            ui.access(() -> player2Hand.remove(cardToRemove.get()));
        }
    }


    private void drawCard() {
        Card card = Optional.of(chosenDeck.getCards().get(0)).orElseThrow(() -> new NoSuchElementException("no more cards in your deck"));
        DragImage drawnCard = getHandImage(card);
        player1Hand.add(drawnCard);

        if (is1Playeractive) Broadcaster.broadcast2(drawnCard.getId().get(), 1);
        if (is2Playeractive) Broadcaster.broadcast2(drawnCard.getId().get(), 0);

        chosenDeck.getCards().remove(chosenDeck.getCards().get(0));
    }
}