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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes WHERE description LIKE '%Пирог%'");
            int id_recip;
            String description;
            String url_image;
            List<String> listSalat = new ArrayList<>();
            while (resultSet.next()) {
                id_recip = resultSet.getInt(1);
                description = resultSet.getString(2);
                url_image = resultSet.getString(3);
                listSalat.add(id_recip+"). "+description+ "\n"+url_image);
            }
            if (listSalat.size() == 0){
                return List.of("Извините, рецепт не найден");
            }
            return listSalat;


        } catch (SQLException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    public List<String> findRecipes(String id) {
        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ingredients WHERE id_ingr = '"+id+"'");
            String description;
            String coockmethod;
            List<String> listSalat = new ArrayList<>();
            while (resultSet.next()) {
                description = resultSet.getString(2);
                coockmethod = resultSet.getString(3);
                listSalat.add(description +"\n"+ coockmethod);
            }
            return listSalat;


        } catch (SQLException e) {
            return Collections.singletonList(e.getMessage());
        }
    }
}
