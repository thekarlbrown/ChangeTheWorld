package com.thekarlbrown.changetheworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that saves/loads List<IdeaBlock>'s to the Android filesystem
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class LocalIdeas {
    private List<IdeaBlock> ideaBlock;

    /**
     * Method that writes an List<IdeaBlock> that is serializable to the Android file system
     * @param dst File to be saving to
     * @param ib List<IdeaBlock> to be writing to File
     * @return boolean indicating the success or failure of writing to the system
     */
    private boolean saveIdeaBlock(File dst,List<IdeaBlock> ib) {
        boolean res = false;
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(dst));
            output.writeObject(ib);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
            res=false;
        }finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                res=false;
            }
        }
        return res;
    }

    /**
     * Loading list of IdeaBlock's from the Android filesystem
     * @param src File that indicates the location of the drafts
     * @return List<IdeaBlock> that contains all IdeaBlock drafts, or null
     */
    @SuppressWarnings({ "unchecked" })
    public List<IdeaBlock> loadIdeaBlock(File src) {
        ObjectInputStream input = null;
        List<IdeaBlock> ideaBlock;
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            ideaBlock=(List<IdeaBlock>) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            ideaBlock=null;
        }finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return ideaBlock;
    }

    /**
     * Adds on an item to the saved List<IdeaBlock> stored locally
     * @param src File to save to
     * @param title Title of the idea
     * @param idea Description of the idea
     * @param author username of individual who wrote draft
     * @param tup Number of thumbs up (should be 0)
     * @param tdown Number of thumbs down (should be 0)
     * @param number Database number (default value only of 11, assigned dynamically when added)
     * @param category Category chosen (starting at 0)
     * @param subcategory Subcategory chosen (starting at 0)
     * @return Boolean indicating if saving ideas to filesystem was successful
     */
    public boolean saveDraft(File src, String title,String idea,String author, int tup, int tdown, int number, int category, int subcategory)
    {
        ideaBlock =loadIdeaBlock(src);
       if(ideaBlock==null)
        {
            ideaBlock=new ArrayList<>();
       }
        ideaBlock.add(new IdeaBlock(title,idea,author,tup,tdown,number,category,subcategory));
        return saveIdeaBlock(src,ideaBlock);
    }
}
