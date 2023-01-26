package projekt.projekt.Data.models;

public class Loan {
    private int IDLoan;
    private int UserID;
    private int BookID;

    public Loan() {
    }
    public Loan(int IDLoan, int userID, int bookID) {
        this.IDLoan = IDLoan;
        UserID = userID;
        BookID = bookID;
    }

    public int getIDLoan() {
        return IDLoan;
    }

    public int getUserID() {
        return UserID;
    }

    public int getBookID() {
        return BookID;
    }
}
