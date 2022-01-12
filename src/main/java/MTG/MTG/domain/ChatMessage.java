package MTG.MTG.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChatMessage {
    private String from;
    private String time;
    private String message;

    public ChatMessage(String from, String message) {
        this.from = from;
        this.message = message;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).substring(0,8);
    }
}
