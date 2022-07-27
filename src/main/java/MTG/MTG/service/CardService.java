package MTG.MTG.service;

import MTG.MTG.domain.*;
import MTG.MTG.layout.MainView;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardService {

    Random rnd = new Random();
    private static boolean isExecuted = false;
    Image mainImage;

    @Autowired
    CardDao cardDao;


    public CardService() {

    }

    public CardService(Image mainImage) {
        this.mainImage = mainImage;
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public List<String> getAllCardsNamesAsList() throws IOException {
        JSONObject json = readJsonFromUrl("https://api.scryfall.com/catalog/card-names");
        String ww = json.toString();
        List<String> splitStrings = Arrays.asList(ww.split("\",\""));

        return splitStrings;
    }


    public void insertAllCardsToDb() throws IOException {

        List<Integer> flawedCards = new ArrayList<>();
        List<String> listWithNames = getAllCardsNamesAsList();
        for (int i = 0; i < listWithNames.size(); i++) {
            try {
                JSONObject json = readJsonFromUrl("https://api.scryfall.com/cards/named?fuzzy="
                        + listWithNames.get(i).replace(" ", "%20"));

                if (json.toString().contains("card_faces")) {
                    int numberOfOccurrences = json.optJSONArray("card_faces").length();
                    insertTransformCardsIntoDb(json, numberOfOccurrences);
                    System.out.println(i);
                    if (!isExecuted) {
                        flawedCards.add(i);
                    }
                } else {
                    insertSingleCardIntoDb(json);
                    System.out.println(i);
                    if (!isExecuted) {
                        flawedCards.add(i);
                    }
                }
            } catch (IOException e) {
                continue;
            } catch (NullPointerException e) {
                continue;
            }
        }
        for (Integer i : flawedCards) {
            System.out.println(i + listWithNames.get(i));
        }
    }

    private void insertSingleCardIntoDb(JSONObject json) {

        try {
            isExecuted = false;
            Card card = new Card();
            card.setUuid(json.optString("id"));
            card.setName(json.optString("name"));
            card.setAggro(json.optString("power"));
            card.setDefense(json.optString("toughness"));
            card.setManacost(json.optString("mana_cost"));
            card.setText(json.optString("oracle_text"));
            card.setType(json.optString("type_line"));
            card.setColor(json.optString("color_identity"));
            card.setUrl(json.optJSONObject("image_uris").optString("normal"));
            System.out.println(card);
            cardDao.save(card);
            isExecuted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertTransformCardsIntoDb(JSONObject json, int numberOfJsonObjects) {
        for (int i = 0; i < numberOfJsonObjects; i++) {
            try {
                Card card = new Card();
                isExecuted = false;
                card.setUuid(json.optString("id"));
                card.setName(json.optJSONArray("card_faces").optJSONObject(i).optString("name"));
                card.setAggro(json.optJSONArray("card_faces").optJSONObject(i).optString("power"));
                card.setDefense(json.optJSONArray("card_faces").optJSONObject(i).optString("toughness"));
                card.setManacost(json.optJSONArray("card_faces").optJSONObject(i).optString("mana_cost"));
                card.setText(json.optJSONArray("card_faces").optJSONObject(i).optString("oracle_text"));
                card.setType(json.optJSONArray("card_faces").optJSONObject(i).optString("type_line"));
                card.setColor(json.optJSONArray("card_faces").optJSONObject(i).optString("colors"));
                card.setUrl(json.optJSONArray("card_faces").optJSONObject(0).optJSONObject("image_uris").optString("normal"));

                System.out.println(card);
                cardDao.save(card);
                isExecuted = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void insertDualCardIntoDb() throws IOException {

        File file = new File("C:\\Users\\lug4r\\Desktop\\dual karty.txt");
        FileReader reader = new FileReader(file);
        String all = CardService.readAll(reader);
        List<String> ids = new ArrayList<>();
        ids = Arrays.asList(all.split("\r\n"));
        List<String> listWithNames = getAllCardsNamesAsList();

        for (int i = 0; i < ids.size(); i++) {
            int seekedId = Integer.parseInt(ids.get(i));
            try {
                JSONObject json = readJsonFromUrl("https://api.scryfall.com/cards/named?fuzzy="
                        + listWithNames.get(seekedId).replace(" ", "%20"));
                for (int y = 0; y < 2; y++) {
                    Card card = new Card();
                    card.setUuid(json.optString("id"));
                    card.setName(json.optJSONArray("card_faces").optJSONObject(y).optString("name"));
                    card.setAggro(json.optJSONArray("card_faces").optJSONObject(y).optString("power"));
                    card.setDefense(json.optJSONArray("card_faces").optJSONObject(y).optString("toughness"));
                    card.setManacost(json.optJSONArray("card_faces").optJSONObject(y).optString("mana_cost"));
                    if (card.getManacost() == null) {
                        card.setManacost(json.optJSONArray("card_faces").optJSONObject(0).optString("mana_cost"));
                    }
                    card.setText(json.optJSONArray("card_faces").optJSONObject(y).optString("oracle_text"));
                    if (card.getText() == null) {
                        card.setText(json.optJSONArray("card_faces").optJSONObject(0).optString("oracle_text"));
                    }
                    card.setType(json.optJSONArray("card_faces").optJSONObject(y).optString("type_line"));
                    if (card.getText() == null) {
                        card.setText(json.optJSONArray("card_faces").optJSONObject(0).optString("type_line"));
                    }
                    card.setColor(json.optJSONArray("card_faces").optJSONObject(y).optString("color_identity"));
                    card.setUrl(json.optJSONObject("image_uris").optString("normal"));

                    System.out.println(card);
                    cardDao.save(card);
                }

            } catch (IOException e) {
                continue;
            } catch (NullPointerException e) {
                continue;
            }
        }

    }

    public void setRelationsForCards(Deck deck, List<Deck> deckList) {
        deck.getCards().stream().forEach(card -> card.setDecks(deckList));
    }

    public void clearQuantities(List<Card> cards) {
        cards.stream().forEach(card -> card.setQuantity(0));
    }

    public List<Card> getallCards() {
        List<Card> list = (List<Card>) cardDao.findAll();
        return list;
    }

    public Card findCardById(Long id) {
        return cardDao.findCardById(id);
    }

    public List<Card> showFilterResults(String text) {
        return getallCards().stream()
                .filter(s -> s.getName().contains(text) || s.getText().contains(text) || s.getType().contains(text))
                .collect(Collectors.toList());
    }

    public Long fetchCardByName(String name) {
        Long id = cardDao.fetchCardByName(name);
        return id;
    }

    public DragImage fromCardToDragImage(Card card) {
        DragImage dragImage = new DragImage();
        dragImage.addClassName("hand-image");
        dragImage.setSrc(card.getUrl());
        dragImage.setUrl(card.getUrl());
        dragImage.setManaCost(card.getManacost());
        dragImage.setName(card.getName());
        dragImage.setMaxHeight("100%");
        dragImage.setMaxWidth(60 + "px");
        dragImage.setId(String.valueOf(rnd.nextInt(44, 4444444)));
        dragImage.setIdentificator(dragImage.getId().get());
        dragImage.getStyle().set("bottom", "10%");

        dragImage.addClickListener(clickEvent -> {
            mainImage.setSrc(clickEvent.getSource().getSrc());
            MainView.url = clickEvent.getSource().getSrc();
            MainView.idOfDragImage = clickEvent.getSource().getId().get();
            Notification.show(clickEvent.getSource().getId().get());
        });

        dragImage.addDragStartListener(drag -> {
            DragImage draggedImage = (DragImage) drag.getSource();
            mainImage.setSrc(drag.getSource().getSrc());
            MainView.idOfDragImage =draggedImage.getId().get();
            MainView.dragStartBoardId = draggedImage.getParent().get().getId().orElseThrow(NoSuchElementException::new);
        });

        return dragImage;
    }

    public DragImage cloneDragImage(DragImage dragImageToClone) {
        DragImage dragImage2 = new DragImage();
        dragImage2.setMaxHeight("100%");
        dragImage2.setMaxWidth(60 + "px");
        dragImage2.setId(dragImageToClone.getId().get());
        dragImage2.setSrc(dragImageToClone.getSrc());
        dragImage2.setManaCost(dragImageToClone.getManaCost());
        dragImage2.setName(dragImageToClone.getName());
        dragImage2.setUrl(dragImageToClone.getUrl());

        dragImage2.addClickListener(clickEvent -> {
            mainImage.setSrc(clickEvent.getSource().getSrc());
            Notification.show(clickEvent.getSource().getId().get());
        });

        dragImage2.addDragStartListener(drag -> {
            mainImage.setSrc(drag.getSource().getSrc());
            Notification.show(drag.getSource().getId().get());
            MainView.idOfDragImage = String.valueOf(drag.getSource().getId().get());
            MainView.dragStartBoardId = drag.getSource().getParent().get().getId().orElseThrow(NoSuchElementException::new);
        });

        return dragImage2;
    }
}
