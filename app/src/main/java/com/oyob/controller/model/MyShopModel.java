package com.oyob.controller.model;

/**
 * Created by Piyush on 10/10/2017.
 */

public class MyShopModel {

    private int  cart_item_id;
    private String categoryName;
    private String itemName;
    private String price;
    private int cart_id;
    private int shippingtype;
    private int max_group_ship_cost;
    private int pid;
    private int qty;
    private String discount;
    private String cart_product_id;

    public String getWarehouse_product_quantity() {
        return warehouse_product_quantity;
    }

    public void setWarehouse_product_quantity(String warehouse_product_quantity) {
        this.warehouse_product_quantity = warehouse_product_quantity;
    }

    private String warehouse_product_quantity;

    public String getCart_product_id() {
        return cart_product_id;
    }

    public void setCart_product_id(String cart_product_id) {
        this.cart_product_id = cart_product_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public MyShopModel(int cart_item_id, String categoryName, String itemName, String price, int cart_id, int shippingtype, int max_group_ship_cost, int pid, int qty, String discount, String cart_product_id, String warehouse_product_quantity) {
        this.cart_item_id = cart_item_id;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.price = price;
        this.cart_id = cart_id;
        this.shippingtype = shippingtype;
        this.max_group_ship_cost = max_group_ship_cost;
        this.pid = pid;
        this.qty = qty;
        this.discount=discount;
        this.cart_product_id=cart_product_id;
        this.warehouse_product_quantity=warehouse_product_quantity;
    }

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getShippingtype() {
        return shippingtype;
    }

    public void setShippingtype(int shippingtype) {
        this.shippingtype = shippingtype;
    }

    public int getMax_group_ship_cost() {
        return max_group_ship_cost;
    }

    public void setMax_group_ship_cost(int max_group_ship_cost) {
        this.max_group_ship_cost = max_group_ship_cost;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
