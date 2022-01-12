package MTG.MTG.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQuery(
        name = "Deck.fetchDecksIdsByUserId",
        query = "SELECT id FROM Deck WHERE nutzer_id= :userId"
)
@Entity
@Getter
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany (
            cascade = CascadeType.MERGE
    )
    @JoinTable(
            name = "CardsAndDecks",
            joinColumns = {@JoinColumn(name = "DECK_ID", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "CARD_ID", referencedColumnName = "id", unique = false)}
    )
    private List<Card> cards = new ArrayList<>();

    private String deckName;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "NUTZER_ID")
    private Nutzer nutzer;

    public Deck() {
    }

    public Deck(List<Card> cards, String name, Nutzer nutzer) {
        this.cards = cards;
        this.nutzer = nutzer;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }
}