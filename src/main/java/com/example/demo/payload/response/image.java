package com.example.demo.payload.response;

public class image {
    private String name;
    private String type;
    private String picByte;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicByte() {
        return picByte;
    }

    public void setPicByte(String picByte) {
        this.picByte = picByte;
    }

    public image(String name, String type, String picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }
}
