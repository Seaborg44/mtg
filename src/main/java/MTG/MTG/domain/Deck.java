package MTG.MTG.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToMany (
            mappedBy = "decks",
            cascade = CascadeType.ALL
    )
    private List<Card> cards;
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