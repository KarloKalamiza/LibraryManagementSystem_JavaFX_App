package projekt.projekt.Data.Repo;

import projekt.projekt.Data.models.Book;
import projekt.projekt.Data.models.User;

import java.util.List;

public interface Repository {
    //book
    void createBook(Book book) throws Exception;
    List<Book> getBooks() throws Exception;

    List<Book> getPurchasedBooks() throws Exception;

    List<Book> getNotPurchasedBooks() throws Exception;

    List<Book> getMostPopularBooks() throws Exception;

    //user
    void createUser(User user) throws Exception;
    List<User> getUsers() throws Exception;

    User GetUserByUsernamePassword(String username, String password) throws  Exception;

    //buying books
    void BuyBook(int bookID, int userID) throws Exception;
}
