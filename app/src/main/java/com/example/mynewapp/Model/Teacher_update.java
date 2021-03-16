package com.example.mynewapp.Model;

import java.io.Serializable;

public class Teacher_update implements Serializable {
    public String name;
    public String imageURL;
    public String email;

    public Teacher_update(String name, String imageURL, String email) {
        this.name = name;
        this.imageURL = imageURL;
        this.email = email;
    }

    public Teacher_update() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
