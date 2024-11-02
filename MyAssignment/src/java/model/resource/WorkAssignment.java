/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.resource;

/**
 *
 * @author milo9
 */
public class WorkAssignment {

    private int id;
    private PlanDetail planDetail;
    private Employee e;
    private int quatity;

    public WorkAssignment() {
    }

    public WorkAssignment(int id, PlanDetail planDetail, Employee e, int quatity) {
        this.id = id;
        this.planDetail = planDetail;
        this.e = e;
        this.quatity = quatity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlanDetail getPlanDetail() {
        return planDetail;
    }

    public void setPlanDetail(PlanDetail planDetail) {
        this.planDetail = planDetail;
    }

    public Employee getE() {
        return e;
    }

    public void setE(Employee e) {
        this.e = e;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

}
