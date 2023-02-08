package projekt.projekt.Data.SQL;

import projekt.projekt.Data.Repo.Repository;
import projekt.projekt.Data.models.Book;
import projekt.projekt.Data.models.User;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlRepository implements Repository {

    //BOOK PARAMS
    public static final String ID_BOOK = "IdBook";
    public static final String TITLE = "Title";
    public static final String AUTHOR = "Author";
    public static final String DESCRIPTION = "bookDesc";
    public static final String ISBN = "ISBN";
    public static final String IS_LOANED = "isLoaned";

    //USER PARAMS
    public static final String ID_USER = "IdUser";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String EMAIL = "Email";
    public static final String CONTACT = "ContactNumber";

    //BUYING BOOK PARAMS
    public static final String BOOK_ID = "BookID";
    public static final String USER_ID = "UserID";

    //BOOK PROCEDURES
    public static final String CREATE_BOOK = "{ CALL CreateBook(?,?,?,?) } ";
    public static final String SELECT_BOOKS = "{ CALL GetBooks } ";

    public static final String SELECTBORROWED_BOOKS = "{ CALL GetBorrowedBooks } ";

    public static final String SELECTnotBORROWED_BOOKS = "{ CALL GetNotBorrowedBooks } ";

    public static final String SELECTMOSTBORROWED_BOOKS = "{ CALL GetMostBorrowedBooks } ";

    //USER PROCEDURES
    public static final String CREATE_USER = " { CALL CreateMember(?,?,?,?) } ";
    public static final String GET_USERS = " { CALL GetUsers } ";

    public static final String GETUSERBYUSERNAMEPASSWORD = " { CALL GetUserByUsernamePassword(?, ?) } ";

    //LOAN PROCEDURES
    public static final String LOAN_BOOK = " { CALL LoanBook(?,?) } ";

    @Override
    public void createBook(Book book) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(CREATE_BOOK)) {

            stmt.setString("@" + TITLE, book.getTitle());
            stmt.setString("@" + AUTHOR, book.getAuthor());
            stmt.setString("@" + DESCRIPTION, book.getDescription());
            stmt.setString("@" + ISBN, book.getISBN());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Book> getBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECT_BOOKS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt(ID_BOOK),
                        rs.getString(TITLE),
                        rs.getString(AUTHOR),
                        rs.getString(DESCRIPTION),
                        rs.getString(ISBN),
                        rs.getBoolean(IS_LOANED)));
            }
        }

        return books;
    }

    @Override
    public List<Book> getPurchasedBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECTBORROWED_BOOKS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt(ID_BOOK),
                        rs.getString(TITLE),
                        rs.getString(AUTHOR),
                        rs.getString(DESCRIPTION),
                        rs.getString(ISBN)
                ));
            }
        }

        return books;
    }

    @Override
    public List<Book> getNotPurchasedBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECTnotBORROWED_BOOKS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt(ID_BOOK),
                        rs.getString(TITLE),
                        rs.getString(AUTHOR),
                        rs.getString(DESCRIPTION),
                        rs.getString(ISBN)
                ));
            }
        }

        return books;
    }

    @Override
    public List<Book> getMostPopularBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECTMOSTBORROWED_BOOKS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt(ID_BOOK),
                        rs.getString(TITLE),
                        rs.getString(AUTHOR),
                        rs.getString(DESCRIPTION),
                        rs.getString(ISBN)
                ));
            }
        }

        return books;
    }

    @Override
    public void createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(CREATE_USER)) {
            stmt.setString("@" + USERNAME, user.getUsername());
            stmt.setString("@" + PASSWORD, user.getPassword());
            stmt.setString("@" + EMAIL, user.getEmail());
            stmt.setString("@" + CONTACT, user.getContact());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<User> getUsers() throws Exception {
        List<User> users = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(GET_USERS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt(ID_USER),
                        rs.getString(USERNAME),
                        rs.getString(PASSWORD),
                        rs.getString(EMAIL),
                        rs.getString(CONTACT)
                ));
            }
        }

        return users;
    }

    @Override
    public User GetUserByUsernamePassword(String username, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(GETUSERBYUSERNAMEPASSWORD)) {

            stmt.setString("@" + USERNAME, username);
            stmt.setString("@" + PASSWORD, password);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getString(EMAIL),
                            rs.getString(CONTACT)
                    );
                }
            }

            return null;
        }
    }

    @Override
    public void BuyBook(int bookID, int userID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(LOAN_BOOK)) {
            stmt.setInt("@" + BOOK_ID, bookID);
            stmt.setInt("@" + USER_ID, userID);
            stmt.executeUpdate();
        }
    }
}