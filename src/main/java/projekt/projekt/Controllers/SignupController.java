package projekt.projekt.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import projekt.projekt.Constants.Constants;
import projekt.projekt.Data.SQL.SqlRepository;
import projekt.projekt.Data.models.User;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.Utils.ScreenUtils;

public class SignupController {

    @FXML
    private Button btnSignUp;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfContact;

    public void signUpMember() throws Exception {
        SqlRepository sqlRepository = new SqlRepository();

        String username = tfUsername.getText().trim();
        String password = tfPassword.getText().trim();
        String email = tfEmail.getText().trim();
        String contact = tfContact.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || contact.isEmpty()) {
            AlertUtils.Error(Constants.SIGNUP_FAILED, "Insert credentials");
        } else {
            User user = new User(username, password, email, contact);
            try {
                sqlRepository.createUser(user);
                ScreenUtils.setNewSceneToStage("Utils/hello-view.fxml", Constants.APPLICATION_NAME);
                AlertUtils.Information(Constants.SIGNUP_SUCCESSED, "You are ready to LOG IN!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
