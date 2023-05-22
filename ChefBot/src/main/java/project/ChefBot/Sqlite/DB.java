package project.ChefBot.Sqlite;

import java.sql.*;
public class DB {
    private final String HOST = "jdbc:sqlite:D:\\Project\\BOT10\\BOT10\\Recipes.db";

    private static Connection connection;

    public DB(){
        try{
            connection = DriverManager.getConnection(HOST);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


}
