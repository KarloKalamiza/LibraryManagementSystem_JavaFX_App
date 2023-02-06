package projekt.projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import projekt.projekt.Constants.Constants;
import projekt.projekt.Controllers.MainScreenUserController;
import projekt.projekt.Data.SQL.SqlRepository;
import projekt.projekt.Data.models.User;
import projekt.projekt.Model.LoggedInUser;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.Utils.ScreenUtils;
import projekt.projekt.clientModels.ClientModel;

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
    public static final int PORT = 59001;

    public static String playerUsername = null;

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

        try (Socket clientSocket = new Socket(HOST, PORT)) {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            System.out.println("Player 1 thread is starting");

            ExecutorService executor = Executors.newSingleThreadExecutor();
            //executor.execute(new ClientThread(new ClientModel()));

            System.out.println("Player 1 thread started");

            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            oos.writeObject(new ClientModel(clientSocket.getLocalPort(),
                    clientSocket.getLocalAddress().getHostName(),
                    tfUsername.getText()));

            System.out.println("PlayerData Object sent to server");

            ClientModel currentApplicationMetaData = (ClientModel) ois.readObject();

            System.out.println("Player ONE port:: " + currentApplicationMetaData.getPort());

            while (true) {
                System.out.println("Waiting for data...");
                if (ois.readObject() != null) {
                    System.out.println("Data available!");
                    String returnMessage = (String) ois.readObject();
                    System.out.println(returnMessage);

                    if (returnMessage.equals("Refresh")){
                        //MainScreenUserController.refreshTable();
                        System.out.println("Ispis");
                    }
                }
                if (MainScreenUserController.isLoaned == true){
                    oos.writeObject("Loan");
                    MainScreenUserController.isLoaned = false;
                }
            }

            } catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        private void ClearTextFields () {
            tfPassword.clear();
            tfUsername.clear();
        }
    }