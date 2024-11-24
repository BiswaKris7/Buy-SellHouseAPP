package com.example.buysellhouse;
public class Property {

    private String PropertyAddress;

    private String Beds;

    private String type;

    private String price;
    private String Baths;

    private String area;



    private String uri;

    public Property(String propertyAddress, String beds, String baths, String type, String area,String price,  String uri) {
        this.PropertyAddress = propertyAddress;
        this.Beds = beds;
        this.type = type;
        this.price = price;
        this.Baths = baths;
        this.uri = uri;
        this.area = area;
    }

    public String getArea() {
        return area;
    }
    public String getPropertyAddress() {
        return PropertyAddress;
    }

    public String getBeds() {
        return Beds;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getBaths() {
        return Baths;
    }

    public String getUri() {
        return uri;
    }




}
