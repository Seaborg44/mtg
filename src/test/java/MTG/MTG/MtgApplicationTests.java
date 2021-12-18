package MTG.MTG;

import MTG.MTG.domain.*;
import MTG.MTG.service.CardService;
import MTG.MTG.service.DeckService;
import MTG.MTG.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
		Card card = new Card(30004l,"test1", "test1", "test1", "test1", "test1", "test1", "test1", "test1", "test1");
		Card card2 = new Card(30005l,"test2", "test2", "test2", "test2", "test2", "test2", "test2", "test2", "test2");
		Card card3 = new Card(30006l,"test3", "test3", "test3", "test3", "test3", "test3", "test3", "test3", "test3");
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck();
		List<Deck> deckList = new ArrayList<>();
		card.setDecks(deckList);
		card2.setDecks(deckList);
		card3.setDecks(deckList);
		cards.add(card);
		cards.add(card2);
		cards.add(card3);
		Nutzer nutzer = new Nutzer();
		nutzer.setId(25966l);
		deck.setDeckName("test1");
		deck.setCards(cards);
		deck.setNutzer(nutzer);
		deckList.add(deck);

		nutzer.setName("viktoria");
		nutzer.setPassword("viktor");
		deckService.saveDeck(deck);
	}

}
