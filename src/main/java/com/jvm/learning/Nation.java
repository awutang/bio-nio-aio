/**
 * Author: Tang Yuqian
 * Date: 2020/11/11
 */
package com.jvm.learning;

import java.io.Serializable;

public class Nation implements Serializable {


    private static final long serialVersionUID = -1572850327659968066L;
    private String nationName;

    private String nationAge;

    public Nation(String nationName, String nationAge) {
        this.nationName = nationName;
        this.nationAge = nationAge;
    }


    public String getNationAge() {
        return nationAge;
    }

    public void setNationAge(String nationAge) {
        this.nationAge = nationAge;
    }

    public Nation(String nationName) {
        this.nationName = nationName;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }
}
