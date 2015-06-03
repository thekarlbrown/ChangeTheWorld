package com.thekarlbrown.changetheworld;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of MergeSort for my custom data container
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class MergeSortTopRated {

    private List<IdeaBlock> list;
    private List<IdeaBlock> tempMergeList;
    private int length;

    /**
     * Public method call to sort the List
     * @param input IdeaBlock to be sorted
     */
    public void sort(List<IdeaBlock> input) {
        this.list = input;
        this.length = input.size();
        this.tempMergeList = new ArrayList<IdeaBlock>();
        doMergeSort(0, length - 1);
    }

    /**
     * Custom Comparison for MergeSort
     * @param reference List of IdeaBlocks
     * @param first IdeaBlock at first position
     * @param second IdeaBlock at second position
     * @return True if Thumbs Up/Down Ratio at first less than/equal to second Ratio, False if greater
     */
    public boolean mergeComparison(List<IdeaBlock> reference,int first,int second){
        if((((double)reference.get(first).getTup()/(double)reference.get(first).getTdown())
                <=((double)reference.get(second).getTup()/(double)reference.get(second).getTdown()))
                &&(reference.get(first).getTup()<=reference.get(second).getTup())){
            return false;
        }
        return true;
    }

    /**
     * Internal method call uses the recursive divide-and-conquer MergeSort method
     * @param lowerIndex Lower index of the List when separating
     * @param higherIndex Higher index of the List when separating
     */
    private void doMergeSort(int lowerIndex, int higherIndex) {
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            //Sort Left Side of List
            doMergeSort(lowerIndex, middle);
            //Sort Right Side of List
            doMergeSort(middle + 1, higherIndex);
            //Merge both sides
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }

    /**
     * Internal method call to merge each part that has been sorted
     * @param lowerIndex Lower index of the List
     * @param middle Middle index of the List
     * @param higherIndex Higher index of the List
     */
    private void mergeParts(int lowerIndex, int middle, int higherIndex) {
        tempMergeList.clear();
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergeList.add(list.get(i));
        }
        int i = 0;
        int j = middle + 1 - lowerIndex;
        int midAdjusted=middle-lowerIndex;
        int highAdjusted=higherIndex-lowerIndex;
        int k = lowerIndex;
        while (i <= midAdjusted && j <= highAdjusted) {
            if (mergeComparison(tempMergeList,i,j)) {
                list.set(k,tempMergeList.get(i));
                i++;
            } else {
                list.set(k,tempMergeList.get(j));
                j++;
            }
            k++;
        }
        while (i <= midAdjusted) {
            list.set(k,tempMergeList.get(i));
            k++;
            i++;
        }
    }
}
