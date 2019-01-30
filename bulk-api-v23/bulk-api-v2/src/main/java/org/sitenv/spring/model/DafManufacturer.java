package org.sitenv.spring.model;

import javax.persistence.*;

@Entity
@Table(name = "manufacturer")
public class DafManufacturer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id=1;

    @Column(name = "name")
    private String name="ABC";

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

}
