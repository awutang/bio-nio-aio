/**
 * Author: Tang Yuqian
 * Date: 2020/11/11
 */
package com.jvm.learning;

import java.io.Serializable;

public class Teacher implements Serializable {

    private static final long serialVersionUID = 6827990589066623556L;

    private String name;

    private int age;

    private SexEnum sex;

    private Nation nation;

    public Teacher(String name, int age, SexEnum sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Teacher(String name, int age, SexEnum sex, Nation nation) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.nation = nation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }
}
