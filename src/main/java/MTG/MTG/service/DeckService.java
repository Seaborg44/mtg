package MTG.MTG.service;

import MTG.MTG.domain.Card;
import MTG.MTG.domain.Deck;
import MTG.MTG.domain.DeckDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService {
    @Autowired
    DeckDao deckDao;
    List<Card> deck = new ArrayList<>();

    public DeckService() {
    }

    public void addCardToDeck (Card card) {
        deck.add(card);
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void saveDeck(Deck deck1) {
        deckDao.save(deck1);
    }
}
