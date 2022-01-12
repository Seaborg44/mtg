package MTG.MTG.domain;

import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
public class Nutzer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(min = 3, max = 24)
    private String username;
    @Size(min = 6, max = 32)
    private String password;
    @OneToMany(
            targetEntity = Deck.class,
            mappedBy = "nutzer",
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY
    )
    private List<Deck> deckList;


    public Nutzer() {
    }

    public Nutzer(@Size(min = 3, max = 12) String username, @Size(min = 6, max = 32) String password) {
        this.username = username;
        this.password = DigestUtils.sha1Hex(password);
        this.deckList = deckList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean passwordCheck(String password1) {
        return DigestUtils.sha1Hex(password1).equals(password);
    }


}
