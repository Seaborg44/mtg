package MTG.MTG.service;

import MTG.MTG.domain.DragImage;
import com.vaadin.flow.shared.Registration;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Consumer<String>> listeners = new LinkedList<>();
    static LinkedList<Consumer<DragImage>> listeners2 = new LinkedList<>();

    public static synchronized Registration register(
            Consumer<String> listener) {
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

    public static synchronized Registration registerImage(
            Consumer<DragImage> listener2) {
        listeners2.add(listener2);
        return () -> {
            synchronized (Broadcaster.class) {
                listeners.remove(listener2);
            }
        };
    }

    public static synchronized void broadcastImage(DragImage image) {
        for (Consumer<DragImage> listener2 : listeners2) {
            executor.execute(() -> listener2.accept(image));
        }
    }
}

