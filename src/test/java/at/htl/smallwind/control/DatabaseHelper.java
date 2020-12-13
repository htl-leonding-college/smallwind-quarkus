package at.htl.smallwind.control;

import org.apache.derby.jdbc.ClientDataSource;

import javax.sql.DataSource;

public class DatabaseHelper {

    public static final String CUSTOMER_TABLE = "SW_CUSTOMER";
    public static final String PRODUCT_TABLE = "SW_PRODUCT";
    public static final String ORDERING_TABLE = "SW_ORDERING";
    public static final String ITEM_TABLE = "SW_ITEM";

    public static final String USERNAME = "app";
    public static final String PASSWORD = "app";
    public static final String DATABASE = "db";

    public static DataSource getDatasource() {
        ClientDataSource dataSource = new ClientDataSource();
        dataSource.setServerName("localhost");   // ist default Wert
        dataSource.setDatabaseName(DATABASE);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }
}
