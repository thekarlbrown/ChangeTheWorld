package com.thekarlbrown.changetheworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * data contaienr for leaderboard
 check if .size is correct

 */
public class LeaderBlock  {
    List<String>accurs,addeds,qualitys,comments;
    List<Double>accurp,qualityp,commentp;
    List<Integer>addedn;

    public LeaderBlock(List<String> accur1,List<String> added1,List<String> quality1, List<String> comment1,List<Double> accur2, List<Integer>added2,List<Double> quality2,List<Double>comment2)
    {
        accurs=accur1;
        accurp=accur2;
        qualityp=quality2;
        addeds=added1;
        qualitys=quality1;
        commentp=comment2;
        comments=comment1;
        addedn=added2;
    }
    public LeaderBlock(String accur1,String added1,String quality1, String comment1,double accur2, int added2,double quality2,double comment2)
    {
        accurs=new ArrayList<>();
        accurs.add(accur1);
        accurp=new ArrayList<>();
        accurp.add(accur2);
        qualityp=new ArrayList<>();
        qualityp.add(quality2);
        addeds=new ArrayList<>();
        addeds.add(added1);
        qualitys=new ArrayList<>();
        qualitys.add(quality1);
        commentp=new ArrayList<>();
        commentp.add(comment2);
        addedn=new ArrayList<>();
        addedn.add(added2);
        comments=new ArrayList<>();
        comments.add(comment1);

    }
    public LeaderBlock(String[] accur1,String[] added1,String[] quality1, String[] comment1,double[] accur2, int[] added2,double[] quality2,double[] comment2)
    {
        accurs=Arrays.asList(accur1);
        addeds=Arrays.asList(added1);
        qualitys=Arrays.asList(quality1);
        comments=Arrays.asList(comment1);
        accurp = new ArrayList<>(accur2.length);
        qualityp=new ArrayList<>(quality2.length);
        commentp=new ArrayList<>(comment2.length);
        addedn=new ArrayList<>(added2.length);

        for (int i = 0; i < accur2.length; i++) {
            accurp.add(accur2[i]);
            qualityp.add(quality2[i]);
            commentp.add(comment2[i]);
            addedn.add(added2[i]);
        }

    }
    //check if this is right
    public int size()
    {
        return accurs.size();
    }
    public LeaderBlock atPosition(int position)
    {
        return new LeaderBlock(accurs.get(position),addeds.get(position),qualitys.get(position),comments.get(position),accurp.get(position),addedn.get(position),qualityp.get(position),commentp.get(position));
    }

    public String getAddedName(int position)
    {
        return addeds.get(position);
    }
    public String getAccurName(int position)
    {
        return accurs.get(position);
    }
    public String getQualityName(int position)
    {
        return qualitys.get(position);
    }
    public String getCommentName(int position)
    {
        return comments.get(position);
    }
    public double getAccurPer(int position)
    {
        return accurp.get(position);
    }
    public double getQualityPer(int position)
    {
        return qualityp.get(position);
    }
    public double getCommentPer(int position)
    {
        return commentp.get(position);
    }
    public int getAddedNum(int position) {
        return addedn.get(position);
    }

    //adding to the almighty container
    public void add(List<String> accur1,List<String> added1,List<String> quality1, List<String> comment1,List<Double> accur2, List<Integer>added2,List<Double> quality2,List<Double>comment2)
    {
        accurs.addAll(accur1);
        accurp.addAll(accur2);
        qualityp.addAll(quality2);
        addeds.addAll(added1);
        qualitys.addAll(quality1);
        commentp.addAll(comment2);
        addedn.addAll(added2);
        comments.addAll(comment1);
    }
    public void add(String accur1,String added1,String quality1, String comment1,double accur2, int added2,double quality2,double comment2)
    {
        accurs.add(accur1);
        accurp.add(accur2);
        qualityp.add(quality2);
        addeds.add(added1);
        qualitys.add(quality1);
        commentp.add(comment2);
        addedn.add(added2);
        comments.add(comment1);
    }
    public void add(String[] accur1,String[] added1,String[] quality1, String[] comment1,double[] accur2, int[] added2,double[] quality2,double[] comment2)
    {
        accurs.addAll((Arrays.asList(accur1)));
        addeds.addAll(Arrays.asList(added1));
        qualitys.addAll(Arrays.asList(quality1));
        comments.addAll(Arrays.asList(comment1));
        for (int i = 0; i < accur2.length; i++) {
            accurp.add(accur2[i]);
            qualityp.add(quality2[i]);
            commentp.add(comment2[i]);
            addedn.add(added2[i]);
        }

    }
}