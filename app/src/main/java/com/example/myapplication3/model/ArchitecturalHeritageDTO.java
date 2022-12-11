package com.example.myapplication3.model;

public class ArchitecturalHeritageDTO implements StandardModel{

    private String name;
    private int image;
    private int yer;

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getYer() {
        return yer;
    }


    public ArchitecturalHeritageDTO(int image, int year, String name) {
        this.image = image;
        this.name = name;
        this.yer = year;
    }


}
