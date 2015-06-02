package com.thekarlbrown.changetheworld;

import java.io.Serializable;

/**
 * Here is my custom Data Container for ideas, implementing Serializable so it can be saved locally
 * By Karl Brown ( thekarlbrown )
 */
public class IdeaBlock implements Serializable {

    private String ideas,authors,titles;
    private int tups,tdowns,numbers,categorys,subcategorys;

    public IdeaBlock(){
        ideas="";
        authors="";
        titles="";
        tups=-1;
        tdowns=-1;
        numbers=-1;
        categorys=-1;
    }
    public IdeaBlock(String title,String idea,String author, int tup, int tdown, int number, int category, int subcategory) {
        ideas=idea;
        tups=tup;
        tdowns=tdown;
        authors=author;
        titles=title;
        numbers=number;
        categorys=category;
        subcategorys=subcategory;
    }

    public String getAuthor(){ return authors;}
    public String getIdea(){ return ideas;   }
    public String getTitle(){ return titles; }
    public int getTup(){ return tups;}
    public int getTdown(){ return tdowns; }
    public int getNumber(){ return numbers; }
    public int getCategory(){ return categorys;}
    public int getSubcategory(){ return subcategorys;}

}