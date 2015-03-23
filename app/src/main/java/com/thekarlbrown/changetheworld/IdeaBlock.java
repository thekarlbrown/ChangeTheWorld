package com.thekarlbrown.changetheworld;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
this is a container for ideas
 will likely need time, location, ?userid? if it cannot be done alternatively
 */
public class IdeaBlock implements Serializable {
    String ideas,authors,titles;
    int tups,tdowns,numbers,categorys,subcategorys;

    public IdeaBlock(){
        ideas="";
        authors="";
        titles="";
        tups=-1;
        tdowns=-1;
        numbers=-1;
        categorys=-1;
    }
    public IdeaBlock(String title,String idea,String author, int tup, int tdown, int number, int category, int subcategory)
    {
        ideas=idea;
        tups=tup;
        tdowns=tdown;
        authors=author;
        titles=title;
        numbers=number;
        categorys=category;
        subcategorys=subcategory;
    }
  /**
   * Get Methods
   */
    public String getAuthor(){        return authors;}
    public String getIdea(){       return ideas;   }
    public String getTitle(){        return titles; }
    public int getTup(){       return tups;}
    public int getTdown(){return tdowns; }
    public int getNumber(){return numbers; }
    public int getCategory(){return categorys;}
    public int getSubcategory(){return subcategorys;}

}