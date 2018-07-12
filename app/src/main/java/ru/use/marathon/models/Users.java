package ru.use.marathon.models;

/**
 * Created by ilyas on 10-Jul-18.
 */

public class Users {

   private int id;
   private String name,image;
   private boolean selected;

    public Users(int id, String name, String image, boolean selected) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
