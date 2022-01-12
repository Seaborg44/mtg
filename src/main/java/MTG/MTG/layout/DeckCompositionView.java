package MTG.MTG.layout;

import MTG.MTG.domain.Card;
import MTG.MTG.domain.Deck;
import MTG.MTG.domain.Nutzer;
import MTG.MTG.service.CardService;
import MTG.MTG.service.DeckService;
import MTG.MTG.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Route("/deck")
@StyleSheet("../CSS/stylesheet.css")
public class DeckCompositionView extends VerticalLayout {

    private CardService cardService;
    private DeckService deckService;
    private UserService userService;
    private Grid<Card> grid = new Grid<>(Card.class);
    private TextField filter = new TextField();
    private Image image = new Image();
    private Grid<Deck> playersDeck = new Grid<>(Deck.class);
    private Card cardAccess;
    private Card cardAccess2;
    private Button addCardButton = new Button("add Card to Deck");
    private Grid<Card> deckGrid = new Grid<>(Card.class);
    private Button saveDeckButton = new Button("save deck");
    private TextField deckNametextField = new TextField("entry deck name");
    private Button acceptDeckNameButton = new Button("Submit");
    private String valueFromTextField;
    private Notification notification = new Notification();
    private Button removeButton = new Button("remove Card");
    private String loggedUserString = new String();
    private Nutzer loggedUser = VaadinSession.getCurrent().getAttribute(Nutzer.class);
    private Button navigateToLoginButton = new Button("Login");
    private Button updateDeckButton = new Button("Update Deck");
    private Deck chosenDeck;
    private Button deleteButton = new Button("Delete Deck");

    @Autowired
    public DeckCompositionView(CardService cardService, DeckService deckService, UserService userService) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.userService = userService;
        this.setClassName("content");
        add(getFilterTextArea());
        add(getMainGrid());
        refresh();
        add(getImage());
        add(getAddCardButton());
        add(getDeckGrid(), getPlayersDeck());
        add(getSaveDeckButton(), getNavigateToLoginButton(), getUpdateDeckButton(), getDeleteButton(), getAcceptDeckNameButton());
        add(getAddDeckNameField());
        add(acceptDeckNameButton);
        add(notification);
        add(getRemoveButton());

        if (VaadinSession.getCurrent().getAttribute(Nutzer.class) != null) {
            add(getLoggedUserString());
        }
    }

    private Grid<Card> getMainGrid() {
        grid.setClassName("Main-Grid");
        grid.setColumns("name", "type", "manacost", "text", "aggro", "defense");
        setMaxWidth("70%");
        grid.getColumnByKey("text").setWidth("40%");
        grid.addItemClickListener(clickEvent->{
            cardAccess= clickEvent.getItem();
            image.setSrc(cardAccess.getUrl());
        });
        return grid;
    }

    public Button getNavigateToLoginButton() {
        navigateToLoginButton.addClassName("navigate-to-login");
        navigateToLoginButton.addClickListener(buttonClickEvent -> {
            if(loggedUser != null) {
                navigateToLoginButton.setText("Log out");
                VaadinSession.getCurrent().close();
            } else {
                UI.getCurrent().navigate("/");
            }
        });

        if (loggedUser != null) {
            navigateToLoginButton.setText("Log out");
        }
        return navigateToLoginButton;
    }

    private String getLoggedUserString() {
        loggedUserString = "Currently logged user: " + VaadinSession.getCurrent().getAttribute(Nutzer.class).getUsername();
        return loggedUserString;
    }

    private TextField getFilterTextArea() {
        filter.setPlaceholder("mind Uppercase!");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e-> update());
        return filter;
    }

    private Image getImage() {
        image.setClassName("selected-image");
        return image;
    }

    private Button getAddCardButton() {
        addCardButton.setClassName("add-card-button");
        addCardButton.addClickListener(clickEvent -> {
            if (deckService.getDeck().stream().anyMatch(card -> card.getName().equals(cardAccess.getName()))) {
                cardAccess = deckService.getDeck().stream().filter(card -> card.getName().equals(cardAccess.getName())).findAny().get();
                deckService.addCardToDeck(cardAccess);
                cardAccess.quantity++;
                updateDeckGrid(cardAccess, deckService.getDeck().indexOf(cardAccess));
            } else {
                deckService.addCardToDeck(cardAccess);
                cardAccess.quantity++;
                updateDeckGrid(cardAccess, deckService.getDeck().indexOf(cardAccess));
            }
            deckGrid.getFooterRows().get(0).getCells().get(1).setText(deckService.calculateQuantityOfCardsInDeck());
        });
        return addCardButton;
    }

    private Grid<Card> getDeckGrid() {
        deckGrid.setClassName("Deck-Grid");
        deckGrid.setColumns("quantity","name", "type", "manacost");
        deckGrid.addItemClickListener(clickEvent -> {
            cardAccess= clickEvent.getItem();
            image.setSrc(cardAccess.getUrl());
        });
        deckGrid.appendFooterRow().getCells().get(0).setText("Total: ");
        deckGrid.getFooterRows().get(0).getCells().get(1).setText(deckService.calculateQuantityOfCardsInDeck());
        return deckGrid;
    }

    private Button getSaveDeckButton() {
        saveDeckButton.setClassName("Save-Deck-Button");
        saveDeckButton.addClickListener(clickEvent -> {
            acceptDeckNameButton.setVisible(true);
            deckNametextField.setVisible(true);
        });
        return saveDeckButton;
    }

    private TextField getAddDeckNameField() {
        deckNametextField.setClassName("Add-DeckName-Field");
        deckNametextField.setClearButtonVisible(true);
        deckNametextField.setVisible(false);
        deckNametextField.setWidth("120px");
        deckNametextField.setHeight("100px");

        return deckNametextField;
    }
     private Button getAcceptDeckNameButton() {
        acceptDeckNameButton.addClassNames("Accept-DeckName-Button");
        acceptDeckNameButton.setVisible(false);
         acceptDeckNameButton.addClickListener(clickEvent -> {
             if (loggedUser != null) {
                 valueFromTextField = deckNametextField.getValue();
                 Deck deckToSave = new Deck();
                 List<Deck> deckList = new ArrayList<>();
                 deckToSave.setNutzer(loggedUser);
                 deckToSave.setDeckName(valueFromTextField);
                 deckToSave.setCards(deckService.populateDeck(deckService.getDeck()));
                 cardService.setRelationsForCards(deckToSave, deckList);
                 deckService.saveDeck(deckToSave);
                 deckService.getDeck().clear();
                 deckGrid.getFooterRows().get(0).getCells().get(1).setText("0");
                 deckGrid.setItems(deckService.getDeck());
                 deckNametextField.setValue("");
                 deckNametextField.setVisible(false);
                 acceptDeckNameButton.setVisible(false);
                 playersDeck.setItems(deckService.getDecksOfTheUser(loggedUser.getId()));
             } else {
                 Notification.show("You must be logged to be able to save the deck!");
             }
         });
        return acceptDeckNameButton;
    }

    private Button getDeleteButton() {
        deleteButton.addClassName("delete-button");
        deleteButton.addClickListener(event -> {
            try {
                deckService.deleteDeck(chosenDeck.getId());
                playersDeck.setItems(deckService.getDecksOfTheUser(loggedUser.getId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return deleteButton;
    }

    public void refresh() {
        grid.setItems(cardService.getallCards());
    }

    public Button getRemoveButton() {
        removeButton.setClassName("Remove-Button");
        removeButton.addClickListener(event -> {
           if (cardAccess.getQuantity() != 0) {
               cardAccess.quantity--;
           } if (cardAccess.getQuantity() == 0) {
               deckService.getDeck().remove(cardAccess);
           }
            deckGrid.setItems(deckService.getDeck());
            deckGrid.getFooterRows().get(0).getCells().get(1).setText(deckService.calculateQuantityOfCardsInDeck());
        });
        return removeButton;
    }

    private Button getUpdateDeckButton() {
        updateDeckButton.addClassName("update-button");
        updateDeckButton.addClickListener(event -> {
            try {
                Deck deckToSave = new Deck();
                deckToSave.setDeckName(chosenDeck.getDeckName());
                List<Deck> deckList = new ArrayList<>();
                deckToSave.setNutzer(loggedUser);
                deckToSave.setCards(deckService.populateDeck(deckService.getDeck()));
                deckList.add(deckToSave);
                cardService.setRelationsForCards(deckToSave, deckList);
                deckService.saveDeck(deckToSave);
                deckService.deleteDeck(chosenDeck.getId());
                cardService.clearQuantities(deckService.getDeck());
                deckService.getDeck().clear();
                deckGrid.getFooterRows().get(0).getCells().get(1).setText("0");
                deckGrid.setItems(deckService.getDeck());
                playersDeck.setItems(deckService.getDecksOfTheUser(loggedUser.getId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return updateDeckButton;
    }

    private Grid<Deck> getPlayersDeck() {
        playersDeck.setClassName("Players-Deck");
        playersDeck.setColumns("deckName");

        if (loggedUser != null) {
            playersDeck.setItems(deckService.getDecksOfTheUser(loggedUser.getId()));
        }

        playersDeck.addItemClickListener(clickEvent->{
            chosenDeck = clickEvent.getItem();
            try {
                List<Card> list = deckService.fetchAllCardsInTheDeck(chosenDeck.getId());
                deckService.setDeck(deckService.processDeckFromDbToGrid(list));
                deckGrid.setItems(deckService.getDeck());
                deckGrid.getFooterRows().get(0).getCells().get(1).setText(deckService.calculateQuantityOfCardsInDeck());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return playersDeck;
    }

    public void update() {
        grid.setItems(cardService.showFilterResults(getFilterTextArea().getValue()));
    }

    public void updateDeckGrid(Card card, int index) {
        List<Card> list = deckService.getDeck();
        String name = card.getName();
        int number = (int) list.stream()
                .filter(card1 -> card1.getName().equals(name))
                .count();

        if (number>=2) {
            list.remove(index);
        }

        deckService.calculateQuantityOfCardsInDeck();
        deckGrid.setItems(list);
    }
}
