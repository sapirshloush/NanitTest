package com.sapir.nanittest.models;

import java.io.Serializable;
import java.util.Date;

public class BabyUser implements Serializable {

    private String name;
    private String imagePath;
    private final int age;
    private final String ageType;
    private final Date birthday;

    public BabyUser(String name, String imagePath, int age, String ageType, Date birthday) {
        this.name = name;
        this.imagePath = imagePath;
        this.age = age;
        this.ageType = ageType;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getAge() {
        return age;
    }

    public String getAgeType() {
        return ageType;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

}
