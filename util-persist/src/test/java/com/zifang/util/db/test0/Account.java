package com.zifang.util.db.test0;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
