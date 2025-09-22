package com.order.dto;

public class OrderDto {
    private String id;
    private String item;
    private int quantity;
    private String status;

    public OrderDto() {}

    public OrderDto(String id, String item, int quantity, String status) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
