package projekt.projekt.Data.models;

public class Book {
    private int IDBook;
    private String Title;
    private String Author;

    public Book(int IDBook, String title, String author, String description, String ISBN) {
        this.IDBook = IDBook;
        Title = title;
        Author = author;
        Description = description;
        this.ISBN = ISBN;
    }

    private String Description;
    private String ISBN;
    private Boolean isLoaned;

    public Book(int IDBook, String title, String author, String description, String ISBN, Boolean isLoaned) {
        this.IDBook = IDBook;
        Title = title;
        Author = author;
        Description = description;
        this.ISBN = ISBN;
        this.isLoaned = isLoaned;
    }

    public Book(String title, String author, String ISBN, String description) {
        Title = title;
        Author = author;
        this.ISBN = ISBN;
        Description = description;
    }

    public Book(int IDBook) {
        this.IDBook = IDBook;
    }

    public int getIDBook() {
        return IDBook;
    }

    public void setIDBook(int IDBook) {
        this.IDBook = IDBook;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }


    public Boolean getLoaned() {
        return isLoaned;
    }

    public void setLoaned(Boolean loaned) {
        isLoaned = loaned;
    }
}
