package project.ChefBot.Sqlite;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "recipes")
public class Recipes {

    @Id
    private int id_recip;
    private String description;
    private String image_url;

    public int getId_recip() {
        return id_recip;
    }

    public void setId_recip(int id_recip) {
        this.id_recip = id_recip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
