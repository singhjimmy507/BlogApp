package com.example.jimmy.lestdomay;

/**
 * Created by Jimmy on 5/3/2017.
 */
//Getters and Setters
    //Constructors
public class Blog {
    private String Title;
    private String Description;
    private String Image; // Need to be exactly the same as fields in the firebase database columns

    //Important to include constructor without arguments
    public Blog(){

    }

    public Blog(String title, String description, String image) {
        this.Title = title;
        this.Description = description;
        this.Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}
