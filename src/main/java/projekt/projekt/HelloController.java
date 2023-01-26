package projekt.projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import projekt.projekt.ApplicationData.ApplicationMetaData;
import projekt.projekt.ApplicationData.ClientData;
import projekt.projekt.Constants.Constants;
import projekt.projekt.Data.SQL.SqlRepository;
import projekt.projekt.Data.models.User;
import projekt.projekt.Model.LoggedInUser;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.Utils.ScreenUtils;
import projekt.projekt.thread.ClientThread;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public static final int PORT = 2000;

    public void signUp() throws Exception{
        ScreenUtils.setNewSceneToStage("Utils/sign-up.fxml", Constants.APPLICATION_NAME);
    }
    public void login() throws Exception{

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
/*
        try (Socket clientSocket = new Socket(HOST, PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            System.out.println("Player 1 thread is starting");

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new ClientThread(new ApplicationMetaData()));

            System.out.println("Player 1 thread started");

            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            oos.writeObject(new ClientData(clientSocket.getLocalPort(),
                    clientSocket.getLocalAddress().getHostName(),
                    tfUsername.getText(),
                    tfPassword.getText()));

            System.out.println("PlayerData Object sent to server");

            ApplicationMetaData currentApplicationMetaData = (ApplicationMetaData) ois.readObject();

            System.out.println("Player ONE port:: " + currentApplicationMetaData.getClientOneData().getPort());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

 */
    }

    private void ClearTextFields() {
        tfPassword.clear();
        tfUsername.clear();
    }
}