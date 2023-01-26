package projekt.projekt.Model;

import projekt.projekt.Data.models.User;

public class LoggedInUser {
    private static User loggedUser;
    private int IDLoggedUser;
    private String UsernameLoggedUser;
    private String EmailLoggedUser;
    private String ContactLoggedUser;

    public LoggedInUser(int IDLoggedUser, String usernameLoggedUser, String emailLoggedUser, String contactLoggedUser) {
        this.IDLoggedUser = IDLoggedUser;
        UsernameLoggedUser = usernameLoggedUser;
        EmailLoggedUser = emailLoggedUser;
        ContactLoggedUser = contactLoggedUser;
    }

    public LoggedInUser(int IDLoggedUser) {
        this.IDLoggedUser = IDLoggedUser;
    }

    public LoggedInUser(String usernameLoggedUser, String emailLoggedUser, String contactLoggedUser) {
        UsernameLoggedUser = usernameLoggedUser;
        EmailLoggedUser = emailLoggedUser;
        ContactLoggedUser = contactLoggedUser;
    }
    public LoggedInUser() {
    }

    public int getIDLoggedUser() {
        return IDLoggedUser;
    }

    public String getUsernameLoggedUser() {
        return UsernameLoggedUser;
    }

    public void setUsernameLoggedUser(String usernameLoggedUser) {
        UsernameLoggedUser = usernameLoggedUser;
    }

    public String getEmailLoggedUser() {
        return EmailLoggedUser;
    }

    public void setEmailLoggedUser(String emailLoggedUser) {
        EmailLoggedUser = emailLoggedUser;
    }

    public String getContactLoggedUser() {
        return ContactLoggedUser;
    }

    public void setContactLoggedUser(String contactLoggedUser) {
        ContactLoggedUser = contactLoggedUser;
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }
}
