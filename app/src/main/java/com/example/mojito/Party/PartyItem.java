package com.example.mojito.Party;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PartyItem implements Serializable {
    public String destination;
    public String user_Name;
    public String attribute;
    public String description;
    public Integer capacity;
    public Integer num_people;

    public PartyItem(){}

    public PartyItem(String User_Name, String destination, String attribute, String description, Integer capacity, Integer num_people){
        this.destination = destination;
        this.user_Name = User_Name;
        this.attribute = attribute;
        this.description = description;
        this.capacity = capacity;
        this.num_people = num_people;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getDestination(){
        return destination;
    }

    public void setUser_Name(String string){
        this.user_Name = string;
    }
    public String getuser_Name(){
        return user_Name;
    }

    public void setAttribute(String string){
        this.attribute= string;
    }
    public String getAttribute(){
        return attribute;
    }

    public void setDescription(String string) {
        this.description = string;
    }
    public String getDescription() {
        return description;
    }

    public void setCapacity(Integer integer){this.capacity = integer;}
    public Integer getCapacity(){return capacity;}

    public void setNum_people(Integer integer){this.num_people = integer;}
    public Integer getNum_people(){return num_people;}


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_Name", user_Name);
        result.put("destination", destination);
        result.put("attribute", attribute);
        result.put("description", description);
        result.put("capacity",capacity);
        result.put("num_people",num_people);
        return result;
    }


}
