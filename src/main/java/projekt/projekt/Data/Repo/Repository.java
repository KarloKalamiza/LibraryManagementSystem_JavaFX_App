package projekt.projekt.Data.Repo;

import projekt.projekt.Data.models.Book;
import projekt.projekt.Data.models.User;

import java.util.List;
import java.util.Optional;

public interface Repository {
    //book
    void createBook(Book book) throws Exception;
    void createBooks(List<Book> books) throws Exception;
    void updateBook(int id, Book data) throws Exception;
    void deleteBook(int id) throws Exception;
    Optional<Book> getBook(int id) throws Exception;
    List<Book> getBooks() throws Exception;

    List<Book> getBorrowedBooks() throws Exception;

    List<Book> getnOTBorrowedBooks() throws Exception;

    List<Book> getMostBorrowedBooks() throws Exception;

    List<Book> getMyBorrowedBooks(int userId) throws Exception;

    //user
    void createUser(User user) throws Exception;
    void deleteUser(int id) throws Exception;
    Optional<User> getUser(int id) throws Exception;
    List<User> getUsers() throws Exception;

    User GetUserByUsernamePassword(String username, String password) throws  Exception;

    //loan
    void LoanBook(int bookID, int userID) throws Exception;
    void ReturnBook(int bookID, int userID) throws Exception;
}
