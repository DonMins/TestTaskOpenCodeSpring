package com.ex.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * A simple JavaBean domain object representing a rating table which stores auxiliary data for its calculation
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "countgame")
    private Long countgame;

    @Column(name = "allAttempt")
    private Long allAttempt;

    @Column(name = "username")
    private String username;

    @Column(name = "youNumber")
    private String youNumber;

    public Rating() {
    }

    public Rating(Long countgame, Long allAttempt, String username, String youGenNumber) {
        this.countgame = countgame;
        this.allAttempt = allAttempt;
        this.username = username;
        this.youNumber = youGenNumber;
    }

    public String getYouGenNumber() {
        return youNumber;
    }

    public void setYouGenNumber(String youNumber) {
        this.youNumber = youNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAllAttempt() {
        return allAttempt;
    }

    public void setAllAttempt(Long allAttempt) {
        this.allAttempt = allAttempt;
    }

    public Long getCountgame() {
        return countgame;
    }

    public void setCountgame(Long countgame) {
        this.countgame = countgame;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
