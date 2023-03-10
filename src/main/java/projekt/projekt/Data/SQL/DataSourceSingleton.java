package projekt.projekt.Data.SQL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;

public class DataSourceSingleton {
    //server name = DESKTOP-7G8UM5O\SQLEXPRESS
    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "JavaFXLibraryApp";
    private static final String USER = "sas";
    private static final String PASSWORD = "SQL";

    private DataSourceSingleton() {
    }

    private static DataSource instance;

    public static DataSource getInstance(){
        if (instance == null){
            instance = createInstance();
        }
        return instance;
    }

    private static DataSource createInstance() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName(SERVER_NAME);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);

        return dataSource;
    }
}