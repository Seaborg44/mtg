package MTG.MTG.layout;

import MTG.MTG.domain.Nutzer;
import MTG.MTG.service.AuthService;
import MTG.MTG.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("/")
@StyleSheet("../CSS/stylesheet.css")
public class LoginView extends Div {

    private TextField userName= new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private H1 header = new H1("Welcome");
    private Button buttonLogin = new Button("Login");
    private Button buttonRegister = new Button("Register");
    private final AuthService authService;
    private UserService userService;

    public LoginView(AuthService authService, UserService userService) {
        this.userService = userService;
        this.authService = authService;
        addClassName("login-view");
        add(getHeader(), getUserName(), getPassword(), getButtonLogin(), getButtonRegister());
    }

    public TextField getUserName() {
        userName.addClassName("username-textfield");
        userName.getStyle().set("top", "20%");
        return userName;
    }

    public H1 getHeader() {
        header.addClassName("login-view-header");
        header.getStyle().set("top", "0%");
        return header;
    }

    public PasswordField getPassword() {
        password.addClassName("passwordfield");
        password.getStyle().set("top", "30%");
        return password;
    }

    public Button getButtonLogin() {
        buttonLogin.addClassName("Login-Button");
        buttonLogin.getStyle().set("top", "44%");
        buttonLogin.addClickListener(buttonClickEvent -> {
            try {
                authService.authenticate(userName.getValue(), password.getValue());
                UI.getCurrent().navigate("/main");
            } catch (AuthService.AuthException e) {
                e.printStackTrace();
                Notification.show("wrong credentials");
            }
        });
        return buttonLogin;
    }

    public Button getButtonRegister() {
        buttonRegister.addClassName("Register-Button");
        buttonRegister.getStyle().set("top", "55%");
        buttonRegister.addClickListener(buttonClickEvent -> {
            try {
                if (userService.findUserByName(userName.getValue()) == null) {
                    Nutzer nutzer = new Nutzer(userName.getValue(), password.getValue());
                    userService.saveUser(nutzer);
                    VaadinSession.getCurrent().setAttribute(Nutzer.class, nutzer);
                    Notification.show("successfully created the user: " +nutzer.getUsername());
                    UI.getCurrent().navigate("/deck");
                } else {
                    Notification.show("this name is either incorrect or already taken");
                    UI.getCurrent().navigate("/");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("wrong credentials");
            }
        });
        return buttonRegister;
    }
}
