package com.myplaces.myplaces;

public class MyPlaces
{
    private String title;
    private String description;
    private String location;
    private String category;

    public MyPlaces(String title, String description, String location)
    {
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



}
