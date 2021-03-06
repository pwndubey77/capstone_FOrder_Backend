package org.upgrad.models;


import javax.persistence.*;


/***
 * State model as per the relation to Restaurants
 *             includes category table and needed column as per schema, including java persistence bindings,
 *                          statename mapped to id,
 *                      default constructor,
 *                      getter and setter
 * **/

@Entity
@Table(name = "state")
public class States {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "state_name")
    private String StateName;

    public States(String state_name) {
        this.StateName = state_name;
    }

    public States() {

    }

    public Integer getId() {
        return id;
    }


    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public void setId(Integer id) {
        this.id = id;

    }




}
