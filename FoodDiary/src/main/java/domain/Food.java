/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author sperande
 */
public class Food {
    private int id;
    private String name;
    private double carb;
    private double protein;
    private double fat;
    
    public Food(int id, String name, double carb, double protein, double fat) {
        this.id = id;
        this.name = name;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
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
    
    
}
