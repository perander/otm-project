/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.sql.Date;

/*
 * A food added to a users collection
 */

/**
 *
 * @author sperande
 */
public class Entry {
    private Integer id;
    private Integer userId;
    private Integer foodId;
    private Date date;
    private Double amount;
    //maybe also time

    public Entry(Integer id, Integer userId, Integer foodId, Date date, Double amount) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.date = date;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public Date getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }
    
    
    
}
