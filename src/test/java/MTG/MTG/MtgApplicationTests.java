package MTG.MTG;

import MTG.MTG.domain.*;
import MTG.MTG.service.CardService;
import MTG.MTG.service.DeckService;
import MTG.MTG.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MtgApplicationTests {
	private static boolean isExecuted = false;
	@Autowired
	CardService cardService;
	@Autowired
	CardDao cardDao;
	@Autowired
	UserDao userDao;
	@Autowired
	DeckDao deckDao;
	@Autowired
	DeckService deckService;
	@Autowired
	UserService userService;
	@Test
	void contextLoads() {
	}

	@Test
	public void testFlawedCards() {
		Card card = cardDao.findCardById(1312l);
		Card card2 = cardDao.findCardById(1116l);
		Card card3 = cardDao.findCardById(3004l);
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck();
		List<Deck> deckList = new ArrayList<>();

		Nutzer nutzer = new Nutzer("viktoria", "casablanca");
		deck.setDeckName("test1");
		deck.setCards(cards);
		deck.setNutzer(nutzer);
		card.setDecks(deckList);
		card2.setDecks(deckList);
		card3.setDecks(deckList);
		cards.add(card);
		cards.add(card);
		cards.add(card2);
		cards.add(card3);
		deckList.add(deck);
		userService.saveUser(nutzer);
		deckService.saveDeck(deck);
	}

	@Test
	public void testFindCardByName() {
		System.out.println(cardService.fetchCardByName("Magical Hacker"));
	}

	@Test
	public void fetchCardsForUser() throws SQLException {
		Nutzer nutzer = userService.findUserByName("Gregory");
		List<Long> list = deckService.getDecksIdsOfTheUser(nutzer.getId());
		System.out.println(list.size());
		System.out.println(deckService.fetchAllCardsInTheDeck(26045));
		System.out.println(deckService.getDecksOfTheUser(nutzer.getId()).size());
	}

	@Test
	public void deckFromDbToGrid() throws SQLException {
		List<Card> cards = deckService.fetchAllCardsInTheDeck(26045);
		deckService.setDeck(deckService.processDeckFromDbToGrid(cards)) ;
		System.out.println(deckService.getDeck().size());
	}

	@Test
	public void deleteTest() throws SQLException {
		deckService.deleteDeck(26074);
	}

	@Test
	public void addDeckMultipleTimes() throws SQLException {

		for (int i = 9; i < 15; i++) {
			Card card = cardDao.findCardById(1542l);
			Card card2 = cardDao.findCardById(1313l);
			card.setQuantity(4);
			card2.setQuantity(3);
			deckService.getDeck().add(card);
			deckService.getDeck().add(card2);

			Deck deckToSave = new Deck();
			List<Deck> deckList = new ArrayList<>();
			Nutzer nutzer = userService.findUserByName("Gregory");
			deckToSave.setNutzer(nutzer);
			deckToSave.setDeckName("test " + i);
			deckToSave.setCards(deckService.populateDeck(deckService.getDeck()));
			cardService.setRelationsForCards(deckToSave, deckList);
			deckService.saveDeck(deckToSave);
			deckService.getDeck().clear();

		}

	}

}
