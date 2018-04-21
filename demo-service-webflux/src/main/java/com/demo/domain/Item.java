package com.demo.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Item {
    @Id
    private Long id;
    private Long user;
    private BigDecimal amount;

    public Item(){}

    public Item(Long id, Long user, BigDecimal amount){
        this.id = id;
        this.user = user;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Item{id=" + id + ", user=" + user + ", amount=" + amount + '}';
    }
}
