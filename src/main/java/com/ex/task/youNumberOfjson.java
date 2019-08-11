package com.ex.task;

/**
 * This class is for receiving JSON data.
 *
 * @author Zdornov Maxim
 * @version 1.0
 *
 */

public class youNumberOfjson {

    private String youNumber ="";

    public youNumberOfjson(){}

    public youNumberOfjson(String number){
        this.youNumber = number;
    }

    public String getYouNumber() {
        return youNumber;
    }

    public void setYouNumber(String youNumber) {
        this.youNumber = youNumber;
    }
}
