package MTG.MTG.service;

import MTG.MTG.domain.BroadcastCardTemplate;
import MTG.MTG.domain.Card;
import MTG.MTG.domain.CardGraveYardInfo;
import MTG.MTG.domain.DragImage;
import MTG.MTG.layout.MainView;
import com.vaadin.flow.shared.Registration;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {

    static Executor executor = Executors.newSingleThreadExecutor();
    static LinkedList<Consumer<DragImage>> listeners = new LinkedList<>();
    static LinkedList<Consumer<String>> listeners2 = new LinkedList<>();
    static LinkedList<Consumer<CardGraveYardInfo>> listeners3 = new LinkedList<>();
    static LinkedList<Consumer<BroadcastCardTemplate>> listeners4 = new LinkedList<>();

    public static synchronized Registration register(Consumer<DragImage> listener) {
        listeners.add(listener);
        return () -> {
            synchronized (Broadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void broadcast(DragImage message) {
        for (Consumer<DragImage> listener : listeners) {
            executor.execute(() -> listener.accept(message));
        }
    }

    public static synchronized Registration register2(Consumer<String> listener2) {
        listeners2.add(listener2);
        return () -> {
            synchronized (Broadcaster.class) {
                listeners2.remove(listener2);
            }
        };
    }

    public static synchronized void broadcast2(String id, int playerNr) {
        executor.execute(() -> listeners2.get(playerNr).accept(id));
    }

    public static synchronized Registration register3(Consumer<CardGraveYardInfo> listener3) {
        listeners3.add(listener3);
        return () -> {
            synchronized (Broadcaster.class) {
                listeners3.remove(listener3);
            }
        };
    }

    public static synchronized void broadcast3(CardGraveYardInfo info, int playerNr) {
        executor.execute(() -> listeners3.get(playerNr).accept(info));
    }

    public static synchronized Registration register4(Consumer<BroadcastCardTemplate> listener4) {
        listeners4.add(listener4);
        return () -> {
            synchronized (Broadcaster.class) {
                listeners4.remove(listener4);
            }
        };
    }

    public static synchronized void broadcast4(BroadcastCardTemplate card, int playerNr) {
        executor.execute(() -> listeners4.get(playerNr).accept(card));
    }
}



