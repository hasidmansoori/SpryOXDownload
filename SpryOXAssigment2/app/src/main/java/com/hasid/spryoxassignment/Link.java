package com.hasid.spryoxassignment;

public class Link {
    int _id;
    String _link;
    public Link(){   }
    public Link(int id, String _phone_number){
        this._id = id;
        this._link = _phone_number;
    }

    public Link(String _link){
        this._link = _link;
    }
    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }


    public String getLink(){
        return this._link;
    }

    public void setLink(String link){
        this._link = link;
    }
}  
