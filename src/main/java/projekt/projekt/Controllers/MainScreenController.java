package projekt.projekt.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import projekt.projekt.Data.models.User;
import projekt.projekt.HelloApplication;
import projekt.projekt.Model.RowState;
import projekt.projekt.Model.UsersState;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.Utils.ReflectionUtils;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainScreenController {
    //BOOKS TAB
    @FXML
    private TableColumn<Book, Integer> tcIDbook;
    @FXML
    private TableColumn<Book, String> tcTitle;
    @FXML
    private TableColumn<Book, String> tcAuthor;
    @FXML
    private TableColumn<Book, String> tcISBN;
    @FXML
    private TableColumn<Book, String> tcDescription;

    //ADD BOOK TAB
    @FXML
    private Button btnAddBook;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfISBN;
    @FXML
    private TextField tfDescription;

    //BORROWED BOOKS TAB
    @FXML
    private TableView<Book> tvBooks;
    @FXML
    private TableView<Book> tvBorrowedBooks;
    @FXML
    private TableColumn<Book, Integer> tcBorrowedBookID;
    @FXML
    private TableColumn<Book, String> tcBorrowedTitle;
    @FXML
    private TableColumn<Book, String> tcBorrowedAuthor;
    @FXML
    private TableColumn<Book, String> tcBorrowedISBN;
    @FXML
    private TableColumn<Book, String> ctBorrowedDescription;
    @FXML
    private Tab tabBooks;
    @FXML
    private Tab tabBorrowedBooks;
    @FXML
    private Tab tabAddBook;

    //REGISTERED USERS TAB
    @FXML
    private Tab tabRegisteredMembers;
    @FXML
    private TableView<User> tvRegisteredUsers;
    @FXML
    private TableColumn<User, Integer> tcIDUser;
    @FXML
    private TableColumn<User, String> tcUsername;
    @FXML
    private TableColumn<User, String> tcEmailAddress;
    @FXML
    private TableColumn<User, String> tcContactNumber;

    //MESSAGES TAB
    @FXML
    private Tab tabMessages;

    @FXML
    private TextArea messageTextArea;
    @FXML
    private TextField messageTextField;
    @FXML
    private Button sendMessageButton;


    public void initialize(){
        SqlRepository sqlRepository = new SqlRepository();

        ShowIniliateTab(sqlRepository);
        showAllBooks(sqlRepository);
        showBorrowedBooks(sqlRepository);
        showAddBookTab(sqlRepository);
        showRegisteredMembers(sqlRepository);
    }

    private void showRegisteredMembers(SqlRepository sqlRepository) {
        tabRegisteredMembers.setOnSelectionChanged(new EventHandler<>() {
            List<User> registeredUsers = null;

            @Override
            public void handle(Event event) {
                try {
                    registeredUsers = sqlRepository.getUsers();

                    tcIDUser.setCellValueFactory(new PropertyValueFactory<>(Constants.IDUSER_COLUMN));
                    tcUsername.setCellValueFactory(new PropertyValueFactory<>(Constants.USERNAME_COLUMN));
                    tcEmailAddress.setCellValueFactory(new PropertyValueFactory<>(Constants.EMAIL_COLUMN));
                    tcContactNumber.setCellValueFactory(new PropertyValueFactory<>(Constants.CONTACT_COLUMN));

                    tvRegisteredUsers.setItems(FXCollections.observableArrayList(registeredUsers));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAddBookTab(SqlRepository sqlRepository) {
        tabAddBook.setOnSelectionChanged(event -> btnAddBook.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (FormValid()) {
                    String title = tfTitle.getText().trim();
                    String author = tfAuthor.getText().trim();
                    String isbn = tfISBN.getText().trim();
                    String description = tfDescription.getText().trim();

                    Book book = new Book(title, author, isbn, description);

                    try {
                        sqlRepository.createBook(book);
                        AlertUtils.Information("Adding Book Notification", "Book is successfully added!");
                        ClearForm();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertUtils.Error("ADD BOOK FAILED", "One or more text field is empty");
                }
            }
        }));
    }

    private void ClearForm() {
        tfTitle.setText("");
        tfAuthor.setText("");
        tfISBN.setText("");
        tfDescription.setText("");
    }

    private boolean FormValid() {
        return !tfTitle.getText().isEmpty() && !tfAuthor.getText().isEmpty()
                && !tfISBN.getText().isEmpty() && !tfDescription.getText().isEmpty();
    }

    private void showBorrowedBooks(SqlRepository sqlRepository) {
        tabBorrowedBooks.setOnSelectionChanged(new EventHandler<>() {
            List<Book> borrowedBooks = null;

            @Override
            public void handle(Event event) {
                try {
                    borrowedBooks = sqlRepository.getBorrowedBooks();

                    tcBorrowedBookID.setCellValueFactory(new PropertyValueFactory<>(Constants.IDBOOK_COLUMN));
                    tcBorrowedTitle.setCellValueFactory(new PropertyValueFactory<>(Constants.TITLE_COLUMN));
                    tcBorrowedAuthor.setCellValueFactory(new PropertyValueFactory<>(Constants.AUTHOR_COLUMN));
                    tcBorrowedISBN.setCellValueFactory(new PropertyValueFactory<>(Constants.ISBN_COLUMN));
                    ctBorrowedDescription.setCellValueFactory(new PropertyValueFactory<>(Constants.DESCRIPTION_COLUMN));

                    tvBorrowedBooks.setItems(FXCollections.observableArrayList(borrowedBooks));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAllBooks(SqlRepository sqlRepository) {
        tabBooks.setOnSelectionChanged(event -> ShowIniliateTab(sqlRepository));
    }

    private void ShowIniliateTab(SqlRepository sqlRepository) {
        List<Book> books;
        try {
            books = sqlRepository.getBooks();

            tcIDbook.setCellValueFactory(new PropertyValueFactory<>(Constants.IDBOOK_COLUMN));
            tcTitle.setCellValueFactory(new PropertyValueFactory<>(Constants.TITLE_COLUMN));
            tcAuthor.setCellValueFactory(new PropertyValueFactory<>(Constants.AUTHOR_COLUMN));
            tcDescription.setCellValueFactory(new PropertyValueFactory<>(Constants.DESCRIPTION_COLUMN));
            tcISBN.setCellValueFactory(new PropertyValueFactory<>(Constants.ISBN_COLUMN));

            tvBooks.setItems(FXCollections.observableArrayList(books));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Utils/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getMainStage().setTitle(Constants.APPLICATION_NAME);
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void saveBook() throws IOException {

        UsersState usersState = new UsersState();
        List<RowState> rowStateList = new ArrayList<>();

        RowState rowState = new RowState(
          tcIDUser.getText(), tcUsername.getText(), tcEmailAddress.getText(), tcContactNumber.getText()
        );
        rowStateList.add(rowState);
        usersState.setUsersStateList(rowStateList);

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.SERIALIZATION_DOCUMENT)))
        {
            oos.writeObject(usersState);
        }

        refreshUsersTableView();
        AlertUtils.Information("Save users", "Users have been saved successfully");
    }

    private void refreshUsersTableView() {
        tcIDUser.setText("");
        tcUsername.setText("");
        tcEmailAddress.setText("");
        tcContactNumber.setText("");
    }

    public void loadBook() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.SERIALIZATION_DOCUMENT)))
        {
            UsersState usersState = (UsersState) ois.readObject();
            for (RowState rowState : usersState.getUsersStateList()){
                tcIDUser.setText(rowState.getUserId());
                tcUsername.setText(rowState.getUsername());
                tcEmailAddress.setText(rowState.getEmail());
                tcContactNumber.setText(rowState.getContact());
            }
        }

        AlertUtils.Information("Load users", "Users have been loaded successfully");
    }

    public void generateDocumentation() throws Exception {
        try {
            List<Path> filesList = Files.walk(Path.of("."))
                    .filter(p ->
                            (p.getFileName().toString().endsWith(Constants.CLASS_EXTENSION)
                                    && !p.getFileName().toString().contains("module-info")))
                    .collect(Collectors.toList());

            List<Class> metaInfo = new ArrayList<>();

            for(Path path : filesList) {

                String fullQualifiedClassName = "";

                System.out.println("Class name: " + path .toString());
                String[] paths = path.toString().split("\\\\");

                Boolean startJoining = false;

                for(String segment : paths) {

                    if("classes".equals(segment)) {
                        startJoining = true;
                        continue;
                    }

                    if(startJoining) {
                        if(segment.endsWith(Constants.CLASS_EXTENSION)) {
                            fullQualifiedClassName += segment.substring(0, segment.lastIndexOf("."));
                        }
                        else {
                            fullQualifiedClassName += segment + ".";
                        }
                    }
                }

                System.out.println("Full qualified name: " + fullQualifiedClassName);

                Class clazz = Class.forName(fullQualifiedClassName);

                metaInfo.add(clazz);
            }

            String documentationString = "";

            for(Class clazz : metaInfo) {
                documentationString += "<h2>" + clazz.getSimpleName() + "</h2>\n";

                documentationString += "<h3> List of member variables: </h2>\n";

                for(Field memberVariable : clazz.getDeclaredFields()) {
                    documentationString += ReflectionUtils.retrieveModifiers(
                            memberVariable.getModifiers())
                            + memberVariable.getType().getSimpleName() + " "
                            + memberVariable.getName() + "<br />\n";
                }

                Constructor[] constructors = clazz.getConstructors();

                documentationString += "<h3> List of constructors: </h2>\n";

                for(Constructor c : constructors) {

                    documentationString += "<h4>" +
                            ReflectionUtils.retrieveModifiers(c.getModifiers())
                            + c.getName() + "(" + ReflectionUtils.retreiveParameters(c.getParameters()) + ")" + "</h4>\n";
                }

                documentationString += "<h2> List of methods: </h2>\n";

                for(Method method : clazz.getDeclaredMethods()) {

                    documentationString += "<h4>" + ReflectionUtils.retrieveModifiers(method.getModifiers())
                            + method.getReturnType().getSimpleName() + " "
                            + method.getName() + "("
                            + ReflectionUtils.retreiveParameters(method.getParameters()) + ")" + "</h4>\n";
                }
            }

            String content = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Class documentation</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>List of classes</h1>\n" +
                    documentationString +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            Files.writeString(Path.of(Constants.DOCUMENTATION_PAGE), content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        AlertUtils.Information("Documentation generation", "Documentation generated successfully");

        showDocumentationInChromeBrowser();
    }

    private static void showDocumentationInChromeBrowser() throws Exception {
        Runtime r = Runtime.getRuntime();
        try{
            r.exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe " +
                    "\"C:\\Users\\karlo\\OneDrive\\Radna površina\\ALgebra-5semestar\\Java2\\ProjektniZadatak\\Projekt\\documentation.html\"");
        } catch(IOException e) {
            throw new Exception(e);
        }
    }
}