package com.zifang.util.db.entity1;


import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "c")
public class User {

    @Column(name = "name")
    private String name;

    @Column(name = "pass")
    private String pass;
}
