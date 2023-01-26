package projekt.projekt.Data.models;

public class User {
    private int IDUser;
    private String Username;
    private String Password;
    private String Email;
    private String Contact;

    public User() {

    }

    public User(int IDUser, String username, String password, String email, String contact) {
        this.IDUser = IDUser;
        Username = username;
        Password = password;
        Email = email;
        Contact = contact;
    }

    public User(String username, String password, String email, String contact) {
        Username = username;
        Password = password;
        Email = email;
        Contact = contact;
    }

    public User(int IDUser) {
        this.IDUser = IDUser;
    }


    public int getIDUser() {
        return IDUser;
    }

    public void setIDUser(int IDUser) {
        this.IDUser = IDUser;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }
}
