package com.yyp.xrecyclerview.model;

/**
 * Created by yyp on 2017/7/18.
 */

public class Animals {

    private int id;
    private String name;
    private int weight;
    private String gender;

    public Animals(int id, String name, int weight, String gender){
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.gender = gender;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
