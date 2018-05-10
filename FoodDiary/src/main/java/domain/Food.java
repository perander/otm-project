/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Objects;

/**
 *
 * @author sperande
 */
public class Food {

    private int id;
    private int userId;
    private String name;
    private double carb;
    private double protein;
    private double fat;

    public Food(Integer userId, String name, double carb, double protein, double fat) {
        this.userId = userId;
        this.name = name;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
    }

    public Integer getUserId() {
        return userId;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCarb() {
        return carb;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Food other = (Food) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Double.doubleToLongBits(this.carb) != Double.doubleToLongBits(other.carb)) {
            return false;
        }
        if (Double.doubleToLongBits(this.protein) != Double.doubleToLongBits(other.protein)) {
            return false;
        }
        if (Double.doubleToLongBits(this.fat) != Double.doubleToLongBits(other.fat)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
