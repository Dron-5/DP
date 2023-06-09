package project.ChefBot.Sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class Sqlite {
    @Autowired
    DB db;

    public List<String> findSal() {
        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes WHERE description LIKE '%Салат%'");
            int id_recip;
            String description;
            String url_image;
            List<String> listSalat = new ArrayList<>();
            while (resultSet.next()) {
                id_recip = resultSet.getInt(1);
                description = resultSet.getString(2);
                url_image = resultSet.getString(3);
                listSalat.add("Блюдо "+id_recip +"\n"+ description+ "\n"+url_image +
                        "\nЕсли вам понравилось это блюдо,\nнапишите боту его порядковый номер");
            }
            if (listSalat.size() == 0){
                return List.of("Извините, рецепт не найден");
            }
            return listSalat;

        } catch (SQLException e) {
            return Collections.singletonList(e.getMessage());
        }
    }


    public List<String> findSup(){
        try{
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes WHERE description LIKE '%Суп%'");
            int id_recip;
            String description;
            String url_image;
            List<String> listSup = new ArrayList<>();
            while (resultSet.next()) {
                id_recip = resultSet.getInt(1);
                description = resultSet.getString(2);
                url_image = resultSet.getString(3);
                listSup.add("Блюдо "+id_recip+"\n"+description+ "\n"+url_image +
                        "\nЕсли вам понравилось это блюдо,\nнапишите боту его порядковый номер");
            }
            if (listSup.size() == 0){
                return List.of("Извините, рецепт не найден");
            }
            return listSup;
        }catch (SQLException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    public List<String> findPir(){
        try{
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes WHERE description LIKE '%Пирог%'");
            int id_recip;
            String description;
            String url_image;
            List<String> listPirog = new ArrayList<>();
            while (resultSet.next()) {
                id_recip = resultSet.getInt(1);
                description = resultSet.getString(2);
                url_image = resultSet.getString(3);
                listPirog.add("Блюдо "+id_recip+"\n"+description+ "\n"+url_image +
                        "\nЕсли вам понравилось это блюдо,\nнапишите боту его порядковый номер");
            }
            if (listPirog.size() == 0){
                return List.of("Извините, рецепт не найден");
            }
            return listPirog;
        }catch (SQLException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    public List<String> findTor(){
        try{
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes WHERE description LIKE '%Торт%'");
            int id_recip;
            String description;
            String url_image;
            List<String> listTor = new ArrayList<>();
            while (resultSet.next()) {
                id_recip = resultSet.getInt(1);
                description = resultSet.getString(2);
                url_image = resultSet.getString(3);
                listTor.add("Блюдо "+id_recip+"\n"+description+ "\n"+url_image +
                        "\nЕсли вам понравилось это блюдо,\nнапишите боту его порядковый номер");
            }
            if (listTor.size() == 0){
                return List.of("Извините, рецепт не найден");
            }
            return listTor;
        }catch (SQLException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    public List<String> findRecipes(String id) {
        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ingredients WHERE id_ingr = '"+id+"'");
            String description;
            String coockmethod;
            List<String> listRecip = new ArrayList<>();
            while (resultSet.next()) {
                description = resultSet.getString(2);
                coockmethod = resultSet.getString(3);
                listRecip.add("Ингредиенты:\n"+description +"\n\nСпособ приготовления:\n"+coockmethod);
            }
            return listRecip;



        } catch (SQLException e) {
            return Collections.singletonList(e.getMessage());
        }
    }
}
