package com.demo.domain;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public class Order {
    @Id
    private Long id;
    private Long user;
    private BigDecimal amount;

    public Order(){}

    public Order(Long id, Long user, BigDecimal amount){
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
        return "Order{id=" + id + ", user=" + user + ", amount=" + amount + '}';
    }
}
