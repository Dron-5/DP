package project.ChefBot.Sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Sqlite {

    public static void main(String[] args) {
        DB db = new DB();

        try {
            Statement statement = DB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes WHERE description LIKE '%Салат%'");
            ResultSet resultSet1 = statement.executeQuery("SELECT * FROM ingredients WHERE id_ingr = '10'");
            while (resultSet.next()){

                int id_recip = resultSet.getInt(1);
                String description = resultSet.getString(2);
                String url_image = resultSet.getString(3);
                System.out.printf("%d. %s \n%s \n", id_recip, description, url_image);
            }
            while (resultSet1.next()){
                int id_ingr = resultSet1.getInt(1);
                String description = resultSet1.getString(2);
                String cooking_method = resultSet1.getString(3);
                System.out.printf("%d. %s \n%s \n", id_ingr, description, cooking_method );
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
