package projekt.projekt.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsersState implements Serializable
{
    private List<RowState> usersStateList;

    public UsersState() {
        this.usersStateList = new ArrayList<>();
    }

    public List<RowState> getUsersStateList() {
        return usersStateList;
    }

    public void setUsersStateList(List<RowState> usersStateList) {
        this.usersStateList = usersStateList;
    }
}
