/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.resource;

import java.sql.Date;

/**
 *
 * @author milo9
 */
public class PlanDetail {

    private int id;
    private PlanHeader planHeader;
    private Shift shift;
    private Date date;
    private int quantity;
    
    public Product getProduct() {
        return planHeader.getProduct(); // Assuming planHeader has getProduct()
    }

    public Shift getShift() {
        return shift;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlanHeader getPlanHeader() {
        return planHeader;
    }

    public void setPlanHeader(PlanHeader planHeader) {
        this.planHeader = planHeader;
    }


    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

 
    
   
    

}
