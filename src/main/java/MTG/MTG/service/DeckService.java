package MTG.MTG.service;

import MTG.MTG.domain.Card;
import MTG.MTG.domain.DbConnector;
import MTG.MTG.domain.Deck;
import MTG.MTG.domain.DeckDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeckService {

    @Autowired
    DeckDao deckDao;

    @Autowired
    CardService cardService;

    List<Card> deck = new ArrayList<>();

    public DeckService() {

    }

    public void addCardToDeck (Card card) {
        deck.add(card);
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Long> getDecksIdsOfTheUser(Long userId) {
        List<Long> listOfIds = deckDao.fetchDecksIdsByUserId(userId);
        return listOfIds;
    }

    public List<Deck> getDecksOfTheUser(Long userId) {
        List<Long> listOfIds = deckDao.fetchDecksIdsByUserId(userId);
        List<Deck> decks = new ArrayList<>();

        for (int i = 0; i < listOfIds.size(); i++) {
           int id = Math.toIntExact(listOfIds.get(i));
            Optional<Deck> deck = deckDao.findById(id);
            decks.add(deck.get());
        }

        return decks;
    }

    public List<Card> fetchAllCardsInTheDeck(int deckId) throws SQLException {

        List<Card> list = new ArrayList<>();
        DbConnector dbConnector = DbConnector.getInstance();
        String sql = "select card_id from cards_and_decks where deck_id = " + deckId;
        Statement statement = dbConnector.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while(rs.next()) {
            Card card = cardService.findCardById(rs.getLong("card_id"));
            list.add(card);
        }

        return list;
    }

    public void saveDeck(Deck deck1) {
        deckDao.save(deck1);
    }

    public List<Card> processDeckFromDbToGrid (List<Card> deckcards) {

      List<Card> deckCards = deckcards.stream().map(card -> {
            int i = (int) deckcards.stream()
                    .filter(card1 -> card.getName().equals(card1.getName()))
                    .count();
            card.setQuantity(i);
            return card;
      }).collect(Collectors.toList());

      int deckSize = deckCards.size();

      for (int i = 0; i < deckSize; i++) {
          Card card = deckCards.get(i);
            for (int j = deckSize-1; j >= i; j--) {
                Card card1 = deckCards.get(j);
                if (card.getName().equals(card1.getName()) && deckCards.indexOf(card) != deckCards.indexOf(card1)) {
                    deckCards.remove(card1);
                    deckSize--;
                }
            }
      }

      return deckCards;
    }

    public void deleteDeck (int deckId) throws SQLException {
        DbConnector dbConnector = DbConnector.getInstance();
        String sql = "delete from cards_and_decks where deck_id = " + deckId;
        Statement statement = dbConnector.getConnection().createStatement();
        statement.executeUpdate(sql);
        Statement statement1 = dbConnector.getConnection().createStatement();
        String sql2 = "delete from deck where id = " + deckId;
        statement1.executeUpdate(sql2);
    }

    public List<Card> populateDeck(List<Card> cards) {
        List<Card> list = new ArrayList<>();
        cards.stream().forEach(card -> {
            for (int i = 0; i < card.getQuantity(); i++) {
                list.add(card);
            }
        });

        return list;
    }

    public String calculateQuantityOfCardsInDeck() {
        return String.valueOf(this.getDeck().stream().collect(Collectors.summingLong(Card::getQuantity)));
    }
}
