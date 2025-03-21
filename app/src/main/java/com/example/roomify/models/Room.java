package com.example.roomify.models;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String id;
    private String roomNumber;
    private double price;
    private String type;
    private String description;
    public Room() {
        amenities = new HashMap<>();
    }

    public Room(String roomNumber, double price, String type, String description) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.type = type;
        this.description = description;
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.type = type;
        this.description = description;
        this.isAvailable = isAvailable;
        this.amenities = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    }

    public Map<String, Object> getAmenities() {
        return amenities;
    }

    public void setAmenities(Map<String, Object> amenities) {
        this.amenities = amenities;
    }

    public void addAmenity(String key, Object value) {
        if (this.amenities == null) {
            this.amenities = new HashMap<>();
        }
        this.amenities.put(key, value);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomNumber", roomNumber);
        result.put("price", price);
        result.put("type", type);
        result.put("description", description);
        result.put("amenities", amenities);
        if (imageUrl != null) {
            result.put("imageUrl", imageUrl);
        }
        return result;
    }
