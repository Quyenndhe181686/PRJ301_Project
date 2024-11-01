/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.resource;

import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author milo9
 */
public class Plan {

    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private Department dept;
    private ArrayList<PlanHeader> headers = new ArrayList<>();

    public ArrayList<PlanHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<PlanHeader> headers) {
        this.headers = headers;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(Department detp) {
        this.dept = detp;
    }

}
