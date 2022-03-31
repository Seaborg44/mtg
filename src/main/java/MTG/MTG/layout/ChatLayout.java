package MTG.MTG.layout;

import MTG.MTG.domain.ChatMessage;
import MTG.MTG.domain.MessageList;
import MTG.MTG.domain.Nutzer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@StyleSheet("../CSS/stylesheet.css")
@Push
@Route("/chat")
public class ChatLayout extends VerticalLayout {

    private final UnicastProcessor<ChatMessage> publisher;
    private final Flux<ChatMessage> messages;
    private final MessageList messageList = new MessageList();

    public ChatLayout(UnicastProcessor<ChatMessage> publisher, Flux<ChatMessage> messages) {
        this.publisher = publisher;
        this.messages = messages;
        this.addClassName("Chat-Layout");
        this.setMaxWidth(250 + "px");
        add(getMessageList());
        add(getInputLayout());
    }

    private Component getInputLayout() {
        HorizontalLayout inputLayout = new HorizontalLayout();
        TextField messageField = new TextField();
        messageField.setMaxWidth(150 + "px");
        Button sendButton = createSendButton();

        inputLayout.add(messageField, sendButton);
        expand(messageField);
        sendButton.addClickListener(event -> {
            publisher.onNext(new ChatMessage(VaadinSession.getCurrent().getAttribute(Nutzer.class).getUsername(),
                    messageField.getValue()));
            messageField.clear();
            messageField.focus();

        });
        return inputLayout;
    }

    private Button createSendButton() {
        Button sendButton = new Button("Send");
        sendButton.addClassName("sendbutton");
        sendButton.setMaxWidth("10%");
        sendButton.getStyle().set("height", 35 + "px");
        sendButton.getStyle().set("bottom", 0 + "px");
        return sendButton;
    }

    private MessageList getMessageList() {
        messageList.addClassName("message-list");
        expand(messageList);
        messages.subscribe(message-> {
            getUI().ifPresent(ui -> ui.access(() -> {
                messageList.add(new Paragraph(
                        message.getTime() + " - " + message.getFrom() + ": " + message.getMessage()
                ));
            }));
        });
        return messageList;
    }
}
