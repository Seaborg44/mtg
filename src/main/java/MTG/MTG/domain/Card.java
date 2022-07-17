package MTG.MTG.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@NamedQuery(
        name = "Card.fetchCardByName",
        query = "SELECT id FROM Card WHERE name= :name"
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Card {

    @Id
    private Long id;
    private String name;
    private String uuid;
    private String url;
    private String manacost;
    private String type;
    private String text;
    private String color;
    private String aggro;
    private String defense;

    @Transient
    private String stringId;

    @ManyToMany(mappedBy = "cards",
    cascade = CascadeType.MERGE)
    private List<Deck> decks;

    @Transient
    public int quantity = 0;

    public Card(Long id, String name, String uuid, String url, String manacost, String type, String color, String text, String aggro, String defense) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
        this.url = url;
        this.manacost = manacost;
        this.type = type;
        this.color = color;
        this.text = text;
        this.aggro = aggro;
        this.defense = defense;
    }
}