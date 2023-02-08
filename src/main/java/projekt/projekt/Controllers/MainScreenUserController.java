package projekt.projekt.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import projekt.projekt.Constants.Constants;
import projekt.projekt.Data.SQL.SqlRepository;
import projekt.projekt.Data.models.Book;
import projekt.projekt.HelloApplication;
import projekt.projekt.HelloController;
import projekt.projekt.Model.LoggedInUser;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.rmiserver.ChatService;
import projekt.projekt.server.Server;
import projekt.projekt.thread.ClientThread;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainScreenUserController {
    @FXML
    private TableView<Book> tvBooks;

    @FXML
    private TableColumn<Book, Integer> tcIDBook;

    @FXML
    private TableColumn<Book, String> tcTitle;

    @FXML
    private TableColumn<Book, String> tcAuthor;

    @FXML
    private TableColumn<Book, String> tcISBN;

    @FXML
    private TableColumn<Book, String> tcDescription;

    @FXML
    private TableColumn<Book, Book> tcButtonLoan;

    @FXML
    private Tab tabBooks;

    @FXML
    private Tab tabMostBorrowedBooks;

    @FXML
    private TableColumn<Book, Integer> tcMostBorrowedBookID;

    @FXML
    private TableColumn<Book, String> tcMostBorrowedBookTitle;

    @FXML
    private TableColumn<Book, String> tcMostBorrowedBookAuthor;

    @FXML
    private TableColumn<Book, String> tcMostBorrowedBookISBN;

    @FXML
    private TableColumn<Book, String> tcMostBorrowedBookdDescription;

    @FXML
    private TableView<Book> tvMostBorrowedBooks;

    //MESSAGE TABS
    @FXML
    private Tab messagesTab;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private TextField messagesTextField;
    @FXML
    private Button sendMessageButton;

    private ChatService stub;

    private SqlRepository sqlRepository = new SqlRepository();

    public void initialize() {
        showInitiateTab(sqlRepository);
        showAllBooks(sqlRepository);
        showMostBorrowedBooks(sqlRepository);

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            new Thread(() -> refreshMessage()).start();
            stub = (ChatService) registry.lookup(ChatService.REMOTE_OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new ClientThread(this));
    }

    public void refreshMessage() {
        while (true) {
            try {
                Thread.sleep(1000);
                messageTextArea.clear();

                fillTextAreaWithMessages();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillTextAreaWithMessages() {
        try {
            StringBuilder allMessagesBuilder = new StringBuilder();
            stub.receiveAllMessages().forEach(m -> allMessagesBuilder.append(m + "\n"));
            messageTextArea.setText(allMessagesBuilder.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showMostBorrowedBooks(SqlRepository sqlRepository) {
        tabMostBorrowedBooks.setOnSelectionChanged(event -> {
            List<Book> books;

            try {
                books = sqlRepository.getMostPopularBooks();

                tcMostBorrowedBookID.setCellValueFactory(new PropertyValueFactory<>(Constants.IDBOOK_COLUMN));
                tcMostBorrowedBookTitle.setCellValueFactory(new PropertyValueFactory<>(Constants.TITLE_COLUMN));
                tcMostBorrowedBookAuthor.setCellValueFactory(new PropertyValueFactory<>(Constants.AUTHOR_COLUMN));
                tcMostBorrowedBookdDescription.setCellValueFactory(new PropertyValueFactory<>(Constants.DESCRIPTION_COLUMN));
                tcMostBorrowedBookISBN.setCellValueFactory(new PropertyValueFactory<>(Constants.ISBN_COLUMN));

                tvMostBorrowedBooks.setItems(FXCollections.observableArrayList(books));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showAllBooks(SqlRepository sqlRepository) {
        tabBooks.setOnSelectionChanged(event -> showInitiateTab(sqlRepository));
    }

    private void showInitiateTab(SqlRepository sqlRepository) {
        List<Book> books = new ArrayList<>();
        try {
            books = sqlRepository.getNotPurchasedBooks();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tabBooks.isSelected()) {
            try {
                tcIDBook.setCellValueFactory(new PropertyValueFactory<>(Constants.IDBOOK_COLUMN));
                tcTitle.setCellValueFactory(new PropertyValueFactory<>(Constants.TITLE_COLUMN));
                tcAuthor.setCellValueFactory(new PropertyValueFactory<>(Constants.AUTHOR_COLUMN));
                tcDescription.setCellValueFactory(new PropertyValueFactory<>(Constants.DESCRIPTION_COLUMN));
                tcISBN.setCellValueFactory(new PropertyValueFactory<>(Constants.ISBN_COLUMN));

                tvBooks.setItems(FXCollections.observableArrayList(books));

                tcButtonLoan.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(col.getValue()));

                tcButtonLoan.setCellFactory(col -> new TableCell<Book, Book>() {

                    final Button button = new Button("BUY");

                    @Override
                    protected void updateItem(Book book, boolean b) {
                        super.updateItem(book, b);

                        if (book == null) {
                            setGraphic(null);
                            return;
                        }

                        setGraphic(button);
                        button.setPrefWidth(50);
                        button.setPrefHeight(50);
                        button.setVisible(true);

                        button.setOnAction(evt -> {
                            Book item = getTableRow().getItem();
                            try {
                                sqlRepository.BuyBook(item.getIDBook(), LoggedInUser.getLoggedUser().getIDUser());
                                Server.IS_LOANED = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            AlertUtils.Information("Loaning a book", "Book has been successfully loaned");
                        });
                    }
                });
                tvBooks.setItems(FXCollections.observableArrayList(books));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Utils/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getMainStage().setTitle(Constants.APPLICATION_NAME);
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void sendMessage() {

        try {
            stub.sendMessage(messagesTextField.getText(), HelloController.playerUsername, HelloController.dateTime);

            List<String> chatHistory = stub.receiveAllMessages();

            StringBuilder chatHistoryBuilder = new StringBuilder();

            for (String message : chatHistory) {
                chatHistoryBuilder.append(message);
                chatHistoryBuilder.append("\n");
            }

            messageTextArea.setText(chatHistoryBuilder.toString());
            messagesTextField.clear();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTable() {
        System.out.println("Osvjezavanje tablice u controlleru");
        showInitiateTab(sqlRepository);
        System.out.println("Osvjezavanje zavrseno");
    }
}