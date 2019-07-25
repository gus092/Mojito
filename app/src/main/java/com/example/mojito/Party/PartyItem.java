package com.example.mojito.Party;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PartyItem  {
    private String destination;
    private String user_Name;
    private String attribute;
    private String description;
    private Integer capacity;
    private Integer num_people;
    private ArrayList<String> members;

    public String mKey;

    public PartyItem(){}

    public PartyItem(String User_Name, String destination, String attribute, String description, Integer capacity, ArrayList<String> members){
        this.destination = destination;
        this.user_Name = User_Name;
        this.attribute = attribute;
        this.description = description;
        this.capacity = capacity;
        this.members = members;
        this.num_people = members.size(); }

    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getDestination(){
        Log.e("destination","...."+destination);
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

//    public void setNum_people(Integer integer){this.num_people = integer;}
    public Integer getNum_people(){return members.size();}

    public void setMembers(ArrayList<String> arrayList){members = arrayList;}

    public ArrayList<String> getMembers(){
//        for(int i=0;i<members.size();i++)
//        Log.e("members","...."+members.get(i));
        return members;}

    @Exclude
    public String getKey(){
        return mKey;
    }
    @Exclude
    public void setKey(String key){
        mKey =key;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("user_Name", user_Name);
//        result.put("destination", destination);
//        result.put("attribute", attribute);
//        result.put("description", description);
//        result.put("capacity",capacity);
//        result.put("num_people",num_people);
//        result.put("members",members);
//        return result;
//    }


}
