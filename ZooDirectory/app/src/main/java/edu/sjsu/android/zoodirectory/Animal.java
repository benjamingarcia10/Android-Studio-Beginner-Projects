package edu.sjsu.android.zoodirectory;

import android.graphics.drawable.Drawable;

public class Animal {
    private String name;
    private String description;
    private int imageResource;

    public Animal(String name, String description, int imageResource) {
        this.name = name;
        this.description = description;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }
}
