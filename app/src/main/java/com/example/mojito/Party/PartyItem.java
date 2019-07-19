package com.example.mojito.Party;

import android.graphics.Bitmap;

import java.io.Serializable;


public class PartyItem implements Serializable {
    private String destination;
    private String User_Name;
    private String attribute;
    private String description;
    private int id;
    private Bitmap photo;
    private long photo_id=0, person_id=0; // this for identifying photo
    public PartyItem(){}

    public PartyItem(String destination, String User_Name, String attribute, String description, Bitmap photo){
        this.destination = destination;
        this.User_Name = User_Name;
        this.attribute = attribute;
        this.description = description;
        this.photo = photo;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getDestination(){
        return destination;
    }

    public void setUser_Name(String string){
        this.User_Name = string;
    }
    public String getUser_Name(){
        return User_Name;
    }

    public void setAttribute(String string){
        this.attribute= attribute;
    }
    public String getAttribute(){
        return attribute;
    }

    public void setDescription(String string) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setPhoto_id(long id){
        this.photo_id = id;
    }
    public void setPerson_id(long id){
        this.person_id = id;
    }

    public long getPhoto_id(){
        return photo_id;
    }
    public long getPerson_id(){
        return person_id;
    }

    public void setUser_photo(Bitmap photo){
        this.photo = photo;
    }
    public Bitmap getUser_photo(){
        return  photo;
    }


}
