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
    List<String>ideas,authors,titles;
    List<Integer>tups,tdowns,numbers,categorys,subcategorys;

    public IdeaBlock()
    {
        ideas=new ArrayList<>();
        tups=new ArrayList<>();
        tdowns=new ArrayList<>();
        authors=new ArrayList<>();
        titles=new ArrayList<>();
        numbers=new ArrayList<>();
        categorys=new ArrayList<>();
        subcategorys=new ArrayList<>();
    }
    public IdeaBlock(List<String> title,List<String> idea,List<String> author, List<Integer> tup, List<Integer> tdown,List<Integer>number,List<Integer>category,List<Integer>subcategory)
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
    public IdeaBlock(String title,String idea,String author, int tup, int tdown, int number, int category, int subcategory)
    {
        ideas=new ArrayList<>();
        ideas.add(idea);
        tups=new ArrayList<>();
        tups.add(tup);
        tdowns=new ArrayList<>();
        tdowns.add(tdown);
        authors=new ArrayList<>();
        authors.add(author);
        titles=new ArrayList<>();
        titles.add(title);
        numbers=new ArrayList<>();
        numbers.add(number);
        categorys=new ArrayList<>();
        categorys.add(category);
        subcategorys=new ArrayList<>();
        subcategorys.add(subcategory);

    }
    public IdeaBlock( String[] title,String[] idea,String[] author, int[] tup, int[] tdown,  int[]number, int[]category,int[]subcategory)
    {
        ideas=Arrays.asList(idea);
        tups = new ArrayList<>(tup.length);
        tdowns=new ArrayList<>(tdown.length);
        numbers=new ArrayList<>(number.length);
        categorys=new ArrayList<>(category.length);
        subcategorys=new ArrayList<>(subcategory.length);
        for (int i = 0; i < tup.length; i++) {
            tups.add(tup[i]);
            tdowns.add(tdown[i]);
            numbers.add(number[i]);
            categorys.add(category[i]);
            subcategorys.add(subcategory[i]);
        }
        authors=Arrays.asList(author);
        titles=Arrays.asList(title);
    }
    //check if this is right
    public int size()
    {
        return ideas.size();
    }
    public IdeaBlock atPosition(int position)
    {
        return new IdeaBlock(titles.get(position),ideas.get(position),authors.get(position),tups.get(position),tdowns.get(position),numbers.get(position),categorys.get(position),subcategorys.get(position));
    }
    //individual components
    public String getAuthor(int position)
    {
        return authors.get(position);
    }
    public String getIdea(int position)
    {
       return ideas.get(position);
    }
    public String getTitle(int position)
    {
        return titles.get(position);
    }
    public int getTup(int position)
    {
        return tups.get(position);
    }
    public int getTdown(int position)
    {
        return tdowns.get(position);
    }
    public int getNumber(int position)
    {
        return numbers.get(position);
    }
    public int getCategory(int position){return categorys.get(position);}
    public int getSubcategory(int position)
    {
        return subcategorys.get(position);
    }
    //adding to the almighty container
    public void add(List<String> title,List<String> idea,List<String> author, List<Integer> tup, List<Integer> tdown,List<Integer>number,List<Integer>category,List<Integer>subcategory)
    {
        ideas.addAll(idea);
        tups.addAll(tup);
        tdowns.addAll(tdown);
        authors.addAll(author);
        titles.addAll(title);
        numbers.addAll(number);
        categorys.addAll(category);
        subcategorys.addAll(subcategory);
    }
    public void add(String title,String idea,String author, int tup, int tdown, int number, int category, int subcategory)
    {
        ideas.add(idea);
        tups.add(tup);
        tdowns.add(tdown);
        authors.add(author);
        titles.add(title);
        numbers.add(number);
        categorys.add(category);
        subcategorys.add(subcategory);
    }
    public void add( String[] title,String[] idea,String[] author, int[] tup, int[] tdown,  int[]number, int[]category,int[]subcategory)
    {
        ideas.addAll((Arrays.asList(idea)));
        for (int i = 0; i < tup.length; i++) {
            tups.add(tup[i]);
            tdowns.add(tdown[i]);
            numbers.add(number[i]);
            categorys.add(category[i]);
            subcategorys.add(subcategory[i]);
        }
        authors.addAll(Arrays.asList(author));
        titles.addAll(Arrays.asList(title));
    }
}