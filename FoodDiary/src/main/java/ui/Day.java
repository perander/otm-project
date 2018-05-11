/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.Food;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author sperande
 */
public class Day {

    private LocalDate date;
    private VBox day;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    public Day(LocalDate date) {
        this.day = createDay(date);
    }

    public VBox getDay() {
        return day;
    }

    /**
     * creates a vbox with a date on top
     * @param date
     * @return 
     */
    public VBox createDay(LocalDate date) {
        VBox box = new VBox();
        box.getChildren().add(dateStamp(date));
        return box;
    }

    /**
     * adds food to a day pane
     * @param f food to be added
     */
    public void addFood(Food f) {
        this.day.getChildren().add(createFoodNode(f));
    }

    /**
     * create an line with the entered food and its nutritional value
     *
     * @param food food to be displayed
     * @return the view with a food name and amount of carbohydrates, protein
     * and fat
     */
    public Node createFoodNode(Food food) {

        Double amount = food.getAmount() / 100;
        HBox box = new HBox(50);
        Label label = new Label(food.getName() + ", " + food.getAmount()
                + " grams\n(carbohydrates: " + df2.format(food.getCarb() * amount) + "g, protein: "
                + df2.format(food.getProtein() * amount) + "g, fat: "
                + df2.format(food.getFat() * amount) + "g)");

        label.setMinHeight(28);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));

        box.getChildren().addAll(label, spacer);
        return box;
    }

    /**
     * creates a node with the current date
     *
     * @param date date to be displayed in the node
     * @return node with the current date
     */
    public Node dateStamp(LocalDate date) {

        HBox box = new HBox(50);
        Label label = new Label(date.toString());

        label.setMinHeight(28);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));

        box.getChildren().addAll(label, spacer);
        return box;
    }

}
