package MTG.MTG.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "CardsAndDecks",
            joinColumns = {@JoinColumn(name = "CARD_ID", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "DECK_ID", referencedColumnName = "id")}
    )
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

    public Card() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getManacost() {
        return manacost;
    }

    public void setManacost(String manacost) {
        this.manacost = manacost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }



    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAggro() {
        return aggro;
    }

    public void setAggro(String aggro) {
        this.aggro = aggro;
    }

    public String getDefense() {
        return defense;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Transient
    public int getQuantity() {
        return quantity;
    }


    public void setDefense(String defense) {
        this.defense = defense;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", id='" + uuid + '\'' +
                ", url='" + url + '\'' +
                ", manacost='" + manacost + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", color='" + color + '\'' +
                ", aggro='" + aggro + '\'' +
                ", defense='" + defense + '\'' +
                '}';
    }
}