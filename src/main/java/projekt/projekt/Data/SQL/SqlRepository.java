package projekt.projekt.Data.SQL;

import projekt.projekt.Data.Repo.Repository;
import projekt.projekt.Data.models.Book;
import projekt.projekt.Data.models.User;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlRepository implements Repository {

    //BOOK PARAMS
    public static final String ID_BOOK="IdBook";
    public static final String TITLE="Title";
    public static final String AUTHOR="Author";
    public static final String DESCRIPTION="bookDesc";
    public static final String ISBN="ISBN";
    public static final String IS_LOANED="isLoaned";

    //USER PARAMS
    public static final String ID_USER = "IdUser";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String EMAIL = "Email";
    public static final String CONTACT = "ContactNumber";

    //LOAN PARAMS
    public static final String ID_LOAN = "IDLoan";
    public static final String BOOK_ID = "BookID";
    public static final String USER_ID = "UserID";

    //BOOK PROCEDURES
    public static final String CREATE_BOOK = "{ CALL CreateBook(?,?,?,?) } ";
    public static final String UPDATE_BOOK = "{ CALL UpdateBook(?,?,?,?, ?) } ";
    public static final String DELETE_BOOK = "{ CALL DeleteBook(?) } ";
    public static final String SELECT_BOOK = "{ CALL GetBookByID(?) } ";
    public static final String SELECT_BOOKS = "{ CALL GetBooks } ";

    public static final String SELECTBORROWED_BOOKS = "{ CALL GetBorrowedBooks } ";

    public static final String SELECTnotBORROWED_BOOKS = "{ CALL GetNotBorrowedBooks } ";

    public static final String SELECTMOSTBORROWED_BOOKS = "{ CALL GetMostBorrowedBooks } ";

    public static final String SELECTMYBORROWED_BOOKS = "{ CALL GetMyBorrowedBooks(?) } ";


    //USER PROCEDURES
    public static final String CREATE_USER = " { CALL CreateMember(?,?,?,?) } ";
    public static final String GET_USERS = " { CALL GetUsers } ";
    public static final String GET_USER = " { CALL GetUserByID(?) } ";
    public static final String DELETE_USER = " { CALL GetUserByID(?) } ";

    public static final String GETUSERBYUSERNAMEPASSWORD = " { CALL GetUserByUsernamePassword(?, ?) } ";

    //LOAN PROCEDURES
    public static final String LOAN_BOOK = " { CALL LoanBook(?,?) } ";
    public static final String RETURN_BOOK = " { CALL ReturnBook(?,?) } ";

    @Override
    public void createBook(Book book) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(CREATE_BOOK)){

            stmt.setString("@" + TITLE, book.getTitle());
            stmt.setString("@" + AUTHOR, book.getAuthor());
            stmt.setString("@" + DESCRIPTION, book.getDescription());
            stmt.setString("@" + ISBN, book.getISBN());

            stmt.executeUpdate();
        }
    }

    @Override
    public void createBooks(List<Book> books) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(CREATE_BOOK)){
            for (Book book : books){
                stmt.setString("@" + TITLE, book.getTitle());
                stmt.setString("@" + AUTHOR, book.getAuthor());
                stmt.setString("@" + DESCRIPTION, book.getDescription());
                stmt.setString("@" + ISBN, book.getISBN());
                stmt.setString(Types.INTEGER, "@" + ID_BOOK);
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateBook(int id, Book data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(UPDATE_BOOK)){

            stmt.setString("@" + TITLE, data.getTitle());
            stmt.setString("@" + AUTHOR, data.getAuthor());
            stmt.setString("@" + DESCRIPTION, data.getDescription());
            stmt.setString("@" + ISBN, data.getISBN());
            stmt.setString(id, "@" + ID_BOOK);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteBook(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(DELETE_BOOK)){

            stmt.setString(id, "@" + ID_BOOK);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Book> getBook(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(SELECT_BOOK))   {
            stmt.setInt("@" + ID_BOOK, id);
            try (ResultSet rs = stmt.executeQuery()){

                if (rs.next()){
                    return Optional.of(new Book(
                            rs.getInt(ID_BOOK),
                            rs.getString(TITLE),
                            rs.getString(AUTHOR),
                            rs.getString(DESCRIPTION),
                            rs.getString(ISBN),
                            rs.getBoolean(IS_LOANED))
                    );
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Book> getBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(SELECT_BOOKS);
            ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
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
    public List<Book> getBorrowedBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECTBORROWED_BOOKS);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
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
    public List<Book> getnOTBorrowedBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECTnotBORROWED_BOOKS);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
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
    public List<Book> getMostBorrowedBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECTMOSTBORROWED_BOOKS);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
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
    public List<Book> getMyBorrowedBooks(int userId) throws Exception {
        List<Book> books = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(SELECTMYBORROWED_BOOKS)){
            stmt.setInt("@" + USER_ID, userId);
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
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
    }

    @Override
    public void createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(CREATE_USER)){
            stmt.setString("@" + USERNAME, user.getUsername());
            stmt.setString("@" + PASSWORD, user.getPassword());
            stmt.setString("@" + EMAIL, user.getEmail());
            stmt.setString("@" + CONTACT, user.getContact());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(DELETE_USER)){
            stmt.setInt("@" + ID_USER, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<User> getUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(GET_USER)) {

            stmt.setInt("@" + ID_USER, id);
            try (ResultSet rs = stmt.executeQuery()){

                if (rs.next()){
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getString(EMAIL),
                            rs.getString(CONTACT)
                    ));
                }
            }

            return Optional.empty();
        }
    }

    @Override
    public List<User> getUsers() throws Exception {
        List<User> users = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(GET_USERS);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
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
    public void LoanBook(int bookID, int userID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(LOAN_BOOK)){
                stmt.setInt("@" + BOOK_ID, bookID);
                stmt.setInt("@" + USER_ID, userID);
                stmt.executeUpdate();
        }
    }

    @Override
    public void ReturnBook(int bookID, int userID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall(RETURN_BOOK)){
                stmt.setInt("@" + BOOK_ID, bookID);
                stmt.setInt("@" + USER_ID, userID);
        }
    }
}