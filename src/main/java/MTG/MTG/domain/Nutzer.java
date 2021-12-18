package MTG.MTG.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
public class Nutzer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 24)
    private String name;
    @Size(min = 6, max = 16)
    private String password;
    @OneToMany(
            targetEntity = Deck.class,
            mappedBy = "nutzer",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Deck> deckList;

    public Nutzer() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDeckList(List<Deck> deckList) {
        for (Deck deck : deckList){
            deck.setNutzer(this);
        }
        this.deckList = deckList;
    }
}
