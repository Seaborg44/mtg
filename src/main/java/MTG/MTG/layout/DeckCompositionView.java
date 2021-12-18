package MTG.MTG.layout;

import MTG.MTG.domain.Card;
import MTG.MTG.domain.Deck;
import MTG.MTG.domain.Nutzer;
import MTG.MTG.service.CardService;
import MTG.MTG.service.DeckService;
import MTG.MTG.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Route("/deck")
@StyleSheet("../CSS/stylesheet.css")
public class DeckCompositionView extends VerticalLayout {

    private CardService cardService;
    private DeckService deckService;
    private UserService userService;
    private Grid<Card> grid = new Grid<>(Card.class);
    private TextField filter = new TextField();
    private Image image = new Image();
    private Card cardAccess;
    private Button addCardButton = new Button("add Card to Deck");
    private Grid<Card> deckGrid = new Grid<>(Card.class);
    private Button saveDeckButton = new Button("save deck");
    private TextField deckNametextField = new TextField("entry deck name");
    private Button acceptDeckNameButton = new Button("Submit");
    private String valueFromTextField;
    private Notification notification = new Notification();
    private Button removeButton = new Button("remove Card");

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
        add(getDeckGrid());
        add(getSaveDeckButton());
        add(getAddDeckNameField());
        add(acceptDeckNameButton);
        add(notification);
        add(getRemoveButton());

    }

    private Grid<Card> getMainGrid() {
        grid.setClassName("Main-Grid");
        grid.setColumns("name", "type", "manacost", "text", "aggro", "defense");
        setMaxWidth("70%");
        grid.getColumnByKey("text").setWidth("40%");
        grid.addItemClickListener(l->{
            cardAccess= l.getItem();
            image.setSrc(cardAccess.getUrl());
        });
        return grid;
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
        addCardButton.addClickListener(b-> {
            deckService.addCardToDeck(cardAccess);
            cardAccess.quantity++;
            updateDeckGrid(cardAccess, deckService.getDeck().indexOf(cardAccess));
            deckGrid.getFooterRows().get(0).getCells().get(1).setText(calculateQuantityOfCardsInDeck());
        });
        return addCardButton;
    }

    private Grid<Card> getDeckGrid() {
        deckGrid.setClassName("Deck-Grid");
        deckGrid.setColumns("quantity","name", "type", "manacost");
        deckGrid.addItemClickListener(l->{
            cardAccess= l.getItem();
            image.setSrc(cardAccess.getUrl());
        });
        deckGrid.appendFooterRow().getCells().get(0).setText("Total: ");
        deckGrid.getFooterRows().get(0).getCells().get(1).setText(calculateQuantityOfCardsInDeck());
        return deckGrid;
    }

    private Button getSaveDeckButton() {
        saveDeckButton.setClassName("Save-Deck-Button");
        saveDeckButton.addClickListener(d->{
            acceptDeckNameButton.setVisible(true);
            deckNametextField.setVisible(true);
            acceptDeckNameButton.addClickListener(b-> {
                        valueFromTextField = deckNametextField.getValue();
                        deckNametextField.setValue("");
                        deckNametextField.setVisible(false);
                        acceptDeckNameButton.setVisible(false);
                        Deck deckToSave = new Deck();
                        Nutzer nutzer = new Nutzer();
                        userService.saveUser(nutzer);
                        deckToSave.setNutzer(nutzer);
                        deckToSave.setCards(deckService.getDeck());
                        deckToSave.setDeckName(valueFromTextField);
                        deckService.saveDeck(deckToSave);
                        clearQuantities(deckService.getDeck());
                        deckService.getDeck().clear();
                        deckGrid.getFooterRows().get(0).getCells().get(1).setText("0");
                        deckGrid.setItems(deckService.getDeck());
                    });
        });
        return saveDeckButton;
    }

    private TextField getAddDeckNameField() {
        deckNametextField.setClassName("Add-DeckName-Field");
        deckNametextField.setClearButtonVisible(true);
        deckNametextField.setVisible(false);
        deckNametextField.setWidth("120px");
        deckNametextField.setHeight("100px");
        acceptDeckNameButton.addClassNames("Accept-DeckName-Button");
        acceptDeckNameButton.setVisible(false);
        return deckNametextField;
    }

    public void refresh() {
        grid.setItems(cardService.getallCards());
    }

    public Button getRemoveButton() {
        removeButton.setClassName("Remove-Button");
        removeButton.addClickListener(b->{
           if(cardAccess.getQuantity() != 0) {
               cardAccess.quantity--;
           } if (cardAccess.getQuantity() == 0){
               deckService.getDeck().remove(cardAccess);
           }
            deckGrid.setItems(deckService.getDeck());
            deckGrid.getFooterRows().get(0).getCells().get(1).setText(calculateQuantityOfCardsInDeck());
        });
        return removeButton;
    }

    public void update() {
        grid.setItems(cardService.showFilterResults(getFilterTextArea().getValue()));
    }

    public void updateDeckGrid(Card card, int index) {
        List<Card> list = deckService.getDeck();
        String name = card.getName();
        int number = (int) list.stream().filter(card1 -> card1.getName().equals(name)).count();
        if(number==2){list.remove(index);}
        calculateQuantityOfCardsInDeck();
        deckGrid.setItems(list);
    }

    public String calculateQuantityOfCardsInDeck() {
        return String.valueOf(deckService.getDeck().stream().collect(Collectors.summingLong(Card::getQuantity)));
    }

    public void clearQuantities(List<Card> cards) {
        cards.stream().forEach(card -> card.setQuantity(0));
    }
}
