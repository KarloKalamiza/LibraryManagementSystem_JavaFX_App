package projekt.projekt.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import projekt.projekt.Constants.Constants;
import projekt.projekt.Data.SQL.SqlRepository;
import projekt.projekt.Data.models.Book;
import projekt.projekt.Data.models.User;
import projekt.projekt.HelloApplication;
import projekt.projekt.Model.RowState;
import projekt.projekt.Model.UsersState;
import projekt.projekt.Utils.AlertUtils;
import projekt.projekt.Utils.ReflectionUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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


    public void initialize() {
        SqlRepository sqlRepository = new SqlRepository();

        showInitiateTab(sqlRepository);
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
                if (formValid()) {
                    String title = tfTitle.getText().trim();
                    String author = tfAuthor.getText().trim();
                    String isbn = tfISBN.getText().trim();
                    String description = tfDescription.getText().trim();

                    Book book = new Book(title, author, isbn, description);

                    try {
                        sqlRepository.createBook(book);
                        AlertUtils.Information("Adding Book Notification", "Book is successfully added!");
                        clearForm();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertUtils.Error("ADD BOOK FAILED", "One or more text field is empty");
                }
            }
        }));
    }

    private void clearForm() {
        tfTitle.setText("");
        tfAuthor.setText("");
        tfISBN.setText("");
        tfDescription.setText("");
    }

    private boolean formValid() {
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
        tabBooks.setOnSelectionChanged(event -> showInitiateTab(sqlRepository));
    }

    private void showInitiateTab(SqlRepository sqlRepository) {
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
        List<User> tableList = tvRegisteredUsers.getItems();

        for(User u : tableList){
            RowState rowState = new RowState(
                   String.valueOf(u.getIDUser()), u.getUsername(), u.getEmail(), u.getContact()
            );
            rowStateList.add(rowState);
        }

       /* RowState rowState = new RowState(
                tcIDUser.getText(), tcUsername.getText(), tcEmailAddress.getText(), tcContactNumber.getText()
        );
        rowStateList.add(rowState);*/
        usersState.setUsersStateList(rowStateList);

        try (ObjectOutputStream serializator = new ObjectOutputStream(new FileOutputStream(Constants.SERIALIZATION_DOCUMENT))) {
            serializator.writeObject(usersState);
        }

        refreshUsersTableView();
        AlertUtils.Information("Save users", "Users have been saved successfully");
    }

    private void refreshUsersTableView() {
        ObservableList<User> tableList = tvRegisteredUsers.getItems();
        tableList.clear();
        //tvRegisteredUsers.refresh();
    }

    public void loadBook() throws IOException, ClassNotFoundException {
        List<User> tableList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.SERIALIZATION_DOCUMENT))) {
            UsersState usersState = (UsersState) ois.readObject();
            for (RowState rowState : usersState.getUsersStateList()){
                User u = new User(Integer.parseInt(rowState.getUserId()), rowState.getUsername(), rowState.getEmail(), rowState.getContact());
                tableList.add(u);
               /* tcIDUser.setText(rowState.getUserId());
                tcUsername.setText(rowState.getUsername());
                tcEmailAddress.setText(rowState.getEmail());
                tcContactNumber.setText(rowState.getContact());*/
            }
        }

        tvRegisteredUsers.setItems(FXCollections.observableArrayList(tableList));

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

            for (Path path : filesList) {

                String fullQualifiedClassName = "";

                System.out.println("Class name: " + path.toString());
                String[] paths = path.toString().split("\\\\");

                Boolean startJoining = false;

                for (String segment : paths) {

                    if ("classes".equals(segment)) {
                        startJoining = true;
                        continue;
                    }

                    if (startJoining) {
                        if (segment.endsWith(Constants.CLASS_EXTENSION)) {
                            fullQualifiedClassName += segment.substring(0, segment.lastIndexOf("."));
                        } else {
                            fullQualifiedClassName += segment + ".";
                        }
                    }
                }

                System.out.println("Full qualified name: " + fullQualifiedClassName);

                Class clazz = Class.forName(fullQualifiedClassName);

                metaInfo.add(clazz);
            }

            String documentationString = "";

            for (Class clazz : metaInfo) {
                documentationString += "<h2>" + clazz.getSimpleName() + "</h2>\n";

                documentationString += "<h3> List of member variables: </h2>\n";

                for (Field memberVariable : clazz.getDeclaredFields()) {
                    documentationString += ReflectionUtils.retrieveModifiers(
                            memberVariable.getModifiers())
                            + memberVariable.getType().getSimpleName() + " "
                            + memberVariable.getName() + "<br />\n";
                }

                Constructor[] constructors = clazz.getConstructors();

                documentationString += "<h3> List of constructors: </h2>\n";

                for (Constructor c : constructors) {

                    documentationString += "<h4>" +
                            ReflectionUtils.retrieveModifiers(c.getModifiers())
                            + c.getName() + "(" + ReflectionUtils.retreiveParameters(c.getParameters()) + ")" + "</h4>\n";
                }

                documentationString += "<h2> List of methods: </h2>\n";

                for (Method method : clazz.getDeclaredMethods()) {

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

        String command = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe " +
                "\"C:\\Users\\Korisnik\\Desktop\\Java2_aplikacija\\documentation.html\"";
        showDocumentationInChromeBrowser(command);
    }

    private static void showDocumentationInChromeBrowser(String command) throws Exception {
        Runtime r = Runtime.getRuntime();
        try {
            r.exec(command);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    @FXML
    public void saveToXML(ActionEvent event) {
        if (formValid()) {
            try {
                String title = tfTitle.getText();
                String author = tfAuthor.getText();
                String description = tfDescription.getText();
                String isbn = tfISBN.getText();

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document xmlDocument = documentBuilder.newDocument();

                Element rootElement = xmlDocument.createElement("Book");
                xmlDocument.appendChild(rootElement);

                Element titleElement = xmlDocument.createElement("Title");
                Node titleTextNode = xmlDocument.createTextNode(title);
                titleElement.appendChild(titleTextNode);
                rootElement.appendChild(titleElement);

                Element authorElement = xmlDocument.createElement("Author");
                Node authorTextNode = xmlDocument.createTextNode(author);
                authorElement.appendChild(authorTextNode);
                rootElement.appendChild(authorElement);

                Element isbnElement = xmlDocument.createElement("ISBN");
                Node isbnTextNode = xmlDocument.createTextNode(isbn);
                isbnElement.appendChild(isbnTextNode);
                rootElement.appendChild(isbnElement);

                Element descriptionElement = xmlDocument.createElement("Description");
                Node descriptionTextNode = xmlDocument.createTextNode(description);
                descriptionElement.appendChild(descriptionTextNode);
                rootElement.appendChild(descriptionElement);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();

                Source xmlSource = new DOMSource(xmlDocument);
                Result xmlResult = new StreamResult(new File("Book.xml"));

                transformer.transform(xmlSource, xmlResult);

                AlertUtils.Information("XML file created", "File book.xml was generated");

                String command = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe " +
                        "\"C:\\Users\\Korisnik\\Desktop\\Java2_aplikacija\\book.xml\"";
                showDocumentationInChromeBrowser(command);

                clearFields();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            AlertUtils.Information("Empty data", "Please fill the text fields!");
        }
    }

    private void clearFields() {
        tfAuthor.clear();
        tfDescription.clear();
        tfTitle.clear();
        tfISBN.clear();
    }

    @FXML
    private void loadXMLFile() {
        try {
            String title;
            String author;
            String isbn;
            String description;
            File file = new File("Book.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nodeList = doc.getElementsByTagName("Book");

            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                //System.out.println("\nNode Name :" + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    title = eElement.getElementsByTagName("Title").item(0).getTextContent();
                    author = eElement.getElementsByTagName("Author").item(0).getTextContent();
                    description = eElement.getElementsByTagName("Description").item(0).getTextContent();
                    isbn = eElement.getElementsByTagName("ISBN").item(0).getTextContent();

                    tfTitle.setText(title);
                    tfAuthor.setText(author);
                    tfDescription.setText(description);
                    tfISBN.setText(isbn);

//                    System.out.println("Title: " + title);
//                    System.out.println("Author: " + author);
//                    System.out.println("Description: " + description);
//                    System.out.println("ISBN: " + isbn);
                }
            }
        } catch (Exception e) {
            AlertUtils.Error("Error occurred!", e.toString());
        }
    }
}