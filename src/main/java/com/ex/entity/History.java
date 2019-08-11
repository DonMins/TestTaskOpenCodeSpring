package com.ex.entity;

import javax.persistence.*;

/**
 * A simple JavaBean domain object representing the game history of different users
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "data")
    private String data;

    public History(){}

    public History(String username, String data) {
        this.username = username;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
