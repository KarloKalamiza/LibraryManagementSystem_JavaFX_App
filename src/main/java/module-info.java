module projekt.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;
    requires java.rmi;
    exports projekt.projekt.rmiserver;

    exports projekt.projekt;
    exports projekt.projekt.Controllers;
    opens projekt.projekt.Controllers to javafx.fxml;
    opens projekt.projekt.Data.models to javafx.base;
    opens projekt.projekt to javafx.fxml, javafx.base;
    exports projekt.projekt.Utils;
    opens projekt.projekt.Utils to javafx.fxml;
    exports projekt.projekt.Model;
    opens projekt.projekt.Model to javafx.base, javafx.fxml;
}