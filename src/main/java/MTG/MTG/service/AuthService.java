package MTG.MTG.service;

import MTG.MTG.domain.Nutzer;
import MTG.MTG.layout.MainView;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public class AuthException extends Exception {

    }

    public void authenticate(String username, String password) throws AuthException {
        Nutzer user = userService.findUserByName(username);
        if(user != null && user.passwordCheck(password)) {
            VaadinSession.getCurrent().setAttribute(Nutzer.class, user);
            if(MainView.player1 == null) {
                MainView.player1 = VaadinSession.getCurrent().getAttribute(Nutzer.class);
                MainView.numberOfPlayers.add(MainView.player1);
            }else {
                MainView.player2 = VaadinSession.getCurrent().getAttribute(Nutzer.class);
                MainView.numberOfPlayers.add(MainView.player2);
            }
        } else {
            throw new AuthException();
        }
    }
}
