package projekt.projekt.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import projekt.projekt.Constants.Constants;
import projekt.projekt.Data.SQL.SqlRepository;
import projekt.projekt.Data.models.Book;
import projekt.projekt.Data.models.Loan;
import projekt.projekt.HelloApplication;
import projekt.projekt.HelloController;
import projekt.projekt.Model.LoggedInUser;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.rmiserver.ChatService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

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
    private Tab tabMyBorrowedBooks;

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

    //MYBORROWEDBOOKSTAB
    @FXML
    private TableView<Book> tvMyBorrowedBooks;
    @FXML
    private TableColumn<Book, Integer> tcMyBorrowedBookID;
    @FXML
    private TableColumn<Book, String> tcmyBorrowedBooksTitle;
    @FXML
    private TableColumn<Book, String> tcmyBorrowedBooksAuthor;
    @FXML
    private TableColumn<Book, String> tcmyBorrowedBooksISBN;
    @FXML
    private TableColumn<Book, String> tcmyBorrowedBooksDescription;
    @FXML
    private TableColumn<Book, Book> tcReturnButton;

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

    public static boolean isLoaned = false;

    public void initialize() {
        SqlRepository sqlRepository = new SqlRepository();

        ShowInitiateTab(sqlRepository);
        showAllBooks(sqlRepository);
        showMyBorrowedBooks(sqlRepository);
        showMostBorrowedBooks(sqlRepository);

        try {
            //Registry registry = LocateRegistry.getRegistry();
            // the very same thing
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            new Thread(() -> refreshMessage()).start();
            stub = (ChatService) registry.lookup(ChatService.REMOTE_OBJECT_NAME);



        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void refreshMessage(){
        while (true){
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

    private void showMyBorrowedBooks(SqlRepository sqlRepository) {
        tabMyBorrowedBooks.setOnSelectionChanged(new EventHandler<>() {
            List<Book> myBorrowedBooks;

            @Override
            public void handle(Event event) {
                try {
                    Loan loan = new Loan();
                    //LoggedInUser user = new LoggedInUser();
                    myBorrowedBooks = sqlRepository.getMyBorrowedBooks(loan.getUserID());

                    tcMyBorrowedBookID.setCellValueFactory(new PropertyValueFactory<>(Constants.IDBOOK_COLUMN));
                    tcmyBorrowedBooksTitle.setCellValueFactory(new PropertyValueFactory<>(Constants.TITLE_COLUMN));
                    tcmyBorrowedBooksAuthor.setCellValueFactory(new PropertyValueFactory<>(Constants.AUTHOR_COLUMN));
                    tcmyBorrowedBooksDescription.setCellValueFactory(new PropertyValueFactory<>(Constants.DESCRIPTION_COLUMN));
                    tcmyBorrowedBooksISBN.setCellValueFactory(new PropertyValueFactory<>(Constants.ISBN_COLUMN));

                    tcReturnButton.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(col.getValue()));
                    tcReturnButton.setCellFactory(col -> new TableCell<Book, Book>() {

                        final Button button = new Button("Return");

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
                                    sqlRepository.ReturnBook(item.getIDBook(), LoggedInUser.getLoggedUser().getIDUser());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    });

                    tvMyBorrowedBooks.setItems(FXCollections.observableArrayList(myBorrowedBooks));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showMostBorrowedBooks(SqlRepository sqlRepository) {
        tabMostBorrowedBooks.setOnSelectionChanged(event -> {
            List<Book> books;

            try {
                books = sqlRepository.getMostBorrowedBooks();

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
        tabBooks.setOnSelectionChanged(event -> ShowInitiateTab(sqlRepository));
    }

    private void ShowInitiateTab(SqlRepository sqlRepository) {
        List<Book> books = new ArrayList<>();
        try {
            books = sqlRepository.getnOTBorrowedBooks();
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

                    final Button button = new Button("loan");

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
                                sqlRepository.LoanBook(item.getIDBook(), LoggedInUser.getLoggedUser().getIDUser());
                                isLoaned = true;
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

    public void Logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Utils/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getMainStage().setTitle(Constants.APPLICATION_NAME);
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void sendMessage() {

        try {
            stub.sendMessage(messagesTextField.getText(), HelloController.playerUsername);

            List<String> chatHistory = stub.receiveAllMessages();

            StringBuilder chatHistoryBuilder = new StringBuilder();

            for(String message : chatHistory) {
                chatHistoryBuilder.append(message);
                chatHistoryBuilder.append("\n");
            }

            messageTextArea.setText(chatHistoryBuilder.toString());
            messagesTextField.clear();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTable(){
        tvBooks.refresh();
    }
}