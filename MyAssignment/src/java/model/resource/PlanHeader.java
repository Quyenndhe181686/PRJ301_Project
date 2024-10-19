/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.resource;

import java.util.ArrayList;

/**
 *
 * @author milo9
 */
public class PlanHeader {
    private int id;
    private ArrayList<PlanDetail> planDetails;
    private ArrayList<Product> products;
    private int quatity;
    private float estimatedEffort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<PlanDetail> getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(ArrayList<PlanDetail> planDetails) {
        this.planDetails = planDetails;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

    public float getEstimatedEffort() {
        return estimatedEffort;
    }

    public void setEstimatedEffort(float estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }
    
    
    
}
