package projekt.projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import projekt.projekt.Constants.Constants;
import projekt.projekt.Data.SQL.SqlRepository;
import projekt.projekt.Data.models.User;
import projekt.projekt.Model.LoggedInUser;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.Utils.ScreenUtils;
import java.time.LocalDateTime;

public class HelloController {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;

    public static final String HOST = "localhost";
    public static final int PORT = 59001;

    public static String playerUsername = null;
    public static LocalDateTime dateTime = LocalDateTime.now();

    public void signUp() throws Exception {
        ScreenUtils.setNewSceneToStage("Utils/sign-up.fxml", Constants.APPLICATION_NAME);
    }

    public void login() throws Exception {

        final String username = tfUsername.getText().trim();
        final String password = tfPassword.getText().trim();
        final String ADMIN_USERNAME = "Admin";
        final String ADMIN_PASSWORD = "Admin";

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtils.Error(Constants.LOGIN_FAILED, "Please insert credentials");
        } else if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            ScreenUtils.setNewSceneToStage("Utils/main-screen.fxml", Constants.APPLICATION_NAME);
        } else {
            SqlRepository sqlRepository = new SqlRepository();
            try {
                User user = sqlRepository.GetUserByUsernamePassword(username, password);
                if (user == null) {
                    AlertUtils.Error(Constants.LOGIN_FAILED, "User does not exist");
                    ClearTextFields();
                } else {
                    LoggedInUser.setLoggedUser(user);
                    ScreenUtils.setNewSceneToStage("Utils/main-scrennUser.fxml", Constants.APPLICATION_NAME);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        playerUsername = username;
    }

    private void ClearTextFields() {
        tfPassword.clear();
        tfUsername.clear();
    }
}