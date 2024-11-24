package com.example.buysellhouse;


public class PropertyBuyer {
    private String type;
    private String address;
    private String description;
    private String price;

    public PropertyBuyer(String type, String address, String description, String price) {
        this.type = type;
        this.address = address;
        this.description = description;
        this.price = price;
    }

    // Getters


    public String getType() { return type; }
    public String getAddress() { return address; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
}
