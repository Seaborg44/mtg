package MTG.MTG.service;

import MTG.MTG.domain.Card;
import MTG.MTG.domain.CardGraveYardInfo;
import MTG.MTG.layout.MainView;
import com.vaadin.flow.shared.Registration;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {

    static Executor executor = Executors.newSingleThreadExecutor();
    static LinkedList<Consumer<String>> listeners = new LinkedList<>();
    public static LinkedList<Consumer<String>> listeners2 = new LinkedList<>();
    public static LinkedList<Consumer<CardGraveYardInfo>> listeners3 = new LinkedList<>();

    public static synchronized Registration register(Consumer<String> listener) {
        listeners.add(listener);
        return () -> {
            synchronized (Broadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void broadcast(String message) {
        for (Consumer<String> listener : listeners) {
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


}



