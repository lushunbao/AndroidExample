package com.example.lushunbao.mysqlitetest.model;

/**
 * Created by lushunbao on 2017/3/8.
 */

public class Customer {
    public static final String TB_NAME = "Customer";
    public static final String NAME = "_name";
    public static final String AGE = "_age";
    public static final String ID = "_id";

    private int id;
    private String name;
    private int age;

    public Customer(int id,String name,int age){
        this.id = id;
        this.name = name;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
